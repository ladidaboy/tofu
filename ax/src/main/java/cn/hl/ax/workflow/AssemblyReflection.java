/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2020 All Rights Reserved.
 */
package cn.hl.ax.workflow;

import cn.hl.ax.clone.ReflectionUtils;
import cn.hl.ax.data.DataUtils;
import cn.hl.ax.workflow.bean.Example;
import cn.hl.ax.workflow.bean.FlowRefectionMapper;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <b>装配流程-反射机制</b><br/>
 * 主要是将指定的`源数据对象`按照`数据转换映射配置`装配成指定的`目标数据对象`<br/>
 * @author hyman
 * @date 2020-03-03 10:59:52
 * @version $ Id: AssemblyReflection.java, v 0.1  hyman Exp $
 */
public class AssemblyReflection {
    /**
     * 处理 数据转换映射流程 <br/>
     * 将source中的数据按照mappers映射到target中 <br>
     * 具体映射规则参见 FlowRefectionMapper
     * @see FlowRefectionMapper
     * @param source
     * @param target
     * @param mappers
     */
    public static void doProcess(Object source, Object target, List<FlowRefectionMapper> mappers) {
        if (source == null || target == null || DataUtils.isInvalid(mappers)) {
            return;
        }

        for (FlowRefectionMapper mapper : mappers) {
            String targetKey = mapper.getTargetKey();
            String[] sourceKeys = mapper.getSourceKeys();

            if (DataUtils.isInvalid(sourceKeys)) {
                continue;
            }

            for (String sourceKey : sourceKeys) {
                FieldValue fieldValue = getFieldValue(source, sourceKey);
                setFieldValue(target, targetKey, fieldValue, mapper.isFilterNull());
            }

        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final Pattern COMMON_FIELD_KEY     = Pattern.compile("([a-zA-Z]+)\\[(.*?)]");
    private static final Pattern EXTEND_FIELD_KEY     = Pattern.compile("([a-zA-Z]+)\\[\\*]\\{(.*?)}");
    private static final Pattern ACTUAL_GENERIC_CLASS = Pattern.compile("\\<(.*?)>");
    private static final String  HASH_MAP_INIT_KEY    = "^HASH*MAP#INIT&KEY$";

    public static class FieldValue {
        private Object   value;
        private boolean  needMap;
        private String[] attributes;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public boolean isNeedMap() {
            return needMap;
        }

        public void setNeedMap(boolean needMap) {
            this.needMap = needMap;
        }

        public String[] getAttributes() {
            return attributes;
        }

        public void setAttributes(String[] attributes) {
            this.attributes = attributes;
        }

        @Override
        public String toString() {
            return "FieldValue{" + "value=" + value + ", needMap=" + needMap + ", attributes=" + attributes + '}';
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected static FieldValue getFieldValue(Object source, String sourceField) {
        FieldValue fieldValue = new FieldValue();
        if (source == null || DataUtils.isInvalid(sourceField)) {
            return fieldValue;
        }

        sourceField = DataUtils.compressText(sourceField);
        Matcher matcher = EXTEND_FIELD_KEY.matcher(sourceField);
        if (matcher.find()) {
            if (matcher.end() < sourceField.length()) {
                throw new AssemblyException("INVALID_FIELD_KEY(e.g. End with: a[*]{b,c})");
            }
            // = 扩展路径
            String name = matcher.group(1), keys = matcher.group(2);
            // >> 获取列表属性key以及属性值
            sourceField = sourceField.substring(0, matcher.start()) + name;
            Object value = getObjectFieldValue(source, sourceField, false);
            // >> 获取列表内对象的字段列表
            String[] attributes = keys.split(",");
            //
            fieldValue.setValue(value);
            fieldValue.setNeedMap(true);
            fieldValue.setAttributes(attributes);
        } else {
            // = 普通路径
            Object value = getObjectFieldValue(source, sourceField, false);
            fieldValue.setValue(value);
            fieldValue.setNeedMap(false);
        }
        return fieldValue;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    protected static void setFieldValue(Object target, String targetField, FieldValue sourceValue, boolean filterNull) {
        if (target == null || DataUtils.isInvalid(targetField) || sourceValue == null) {
            return;
        }

        targetField = DataUtils.compressText(targetField);
        Object value = sourceValue.getValue();
        if (value == null && filterNull) {
            return;
        }

        if (sourceValue.isNeedMap()) {
            setFieldValueExtend(target, targetField, value, sourceValue.getAttributes(), filterNull);
        } else {
            setFieldValueNormal(target, targetField, value);
        }
    }

    private static void setFieldValueNormal(Object rootTarget, String targetField, Object sourceValue) {
        // 获取`待设置值属性`的`父级属性`名称，以及其自身属性名称
        int pos;
        String parentFieldName = null, selfFieldName = targetField;
        if (targetField.endsWith("]")) {
            pos = targetField.lastIndexOf("[");
            parentFieldName = targetField.substring(0, pos);
            selfFieldName = targetField.substring(pos + 1, targetField.length() - 1);
        } else {
            pos = targetField.lastIndexOf(".");
            if (pos >= 1) {
                parentFieldName = targetField.substring(0, pos);
                selfFieldName = targetField.substring(pos + 1);
            }
        }

        // 获取目标属性的父级对象
        Object parentObject;
        if (parentFieldName == null) {
            parentObject = rootTarget;
        } else {
            parentObject = getObjectFieldValue(rootTarget, parentFieldName, true);
        }
        if (parentObject == null) {
            return;
        }

        // 设置目标属性值
        if (parentObject instanceof Map) {
            ((Map) parentObject).put(selfFieldName, sourceValue);
        } else {
            ReflectUtil.setFieldValue(parentObject, selfFieldName, sourceValue);
        }
    }

    @SuppressWarnings("unchecked")
    private static void setFieldValueExtend(Object rootTarget, String targetField, Object sourceValue, String[] sourceAttributes,
                                            boolean filterNull) {
        Matcher matcher = EXTEND_FIELD_KEY.matcher(targetField);
        boolean validFieldKey = matcher.find() && matcher.end() == targetField.length();
        if (sourceAttributes.length == 0 || !validFieldKey) {
            throw new AssemblyException("INVALID_FIELD_KEY(e.g. End with: a[*]{b,c})");
        }

        // = 扩展路径
        String name = matcher.group(1), keys = matcher.group(2);
        // >> 获取列表属性key以及属性值
        String parentFieldName = targetField.substring(0, matcher.start()) + name;
        // >> 获取列表内对象的字段列表
        String[] targetAttributes = DataUtils.isValid(keys) ? keys.split(",") : new String[0];

        int maxLength = Math.min(sourceAttributes.length, targetAttributes.length);

        // 获取目标属性列表对象
        Object parentObject = getObjectFieldValue(rootTarget, parentFieldName, true);
        if (parentObject == null) {
            return;
        }

        List parentList = (List) parentObject;
        Class<?> clz = parentList.get(0).getClass();
        parentList.clear();

        if (sourceValue instanceof JSONArray) {
            JSONArray sourceArray = ((JSONArray) sourceValue);
            for (int row = 0; row < sourceArray.size(); row++) {
                if (maxLength == 0) {
                    // anchorKey = tt[*]{} <-- sourceKey = ss[*]{fd}
                    parentList.add(getObjectFieldValue(sourceArray.getJSONObject(row), sourceAttributes[0], false));
                } else {
                    // anchorKey = tt[*]{aa, b, c} <-- sourceKey = ss[*]{x, y, zz}
                    Object element = ReflectUtil.newInstanceIfPossible(clz);
                    parentList.add(element);
                    for (int col = 0; col < maxLength; col++) {
                        copyData(sourceArray.getJSONObject(row), sourceAttributes[col], element, targetAttributes[col], filterNull);
                    }
                }
            }
        } else if (sourceValue instanceof List) {
            List sourceList = (List) sourceValue;
            for (int row = 0; row < sourceList.size(); row++) {
                if (maxLength == 0) {
                    // anchorKey = tt[*]{} <-- sourceKey = ss[*]{fd}
                    parentList.add(getObjectFieldValue(sourceList.get(row), sourceAttributes[0], false));
                } else {
                    // anchorKey = tt[*]{aa, b, c} <-- sourceKey = ss[*]{x, y, zz}
                    Object element = ReflectUtil.newInstanceIfPossible(clz);
                    parentList.add(element);
                    for (int col = 0; col < maxLength; col++) {
                        copyData(sourceList.get(row), sourceAttributes[col], element, targetAttributes[col], filterNull);
                    }
                }
            }
        } else {
            if (!filterNull) {
                throw new AssemblyException("INVALID_SOURCE_TYPE(Support for ClassType: List/FastJSON.JSONArray)");
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Map对象仅支持 *Map&lt;String, ?&gt;
     * @param object
     * @param fieldName
     * @param newInstanceWhenNull
     * @return
     */
    protected static Object getObjectFieldValue(Object object, String fieldName, boolean newInstanceWhenNull) {
        String[] fields = fieldName.split("[.]");
        if (fields.length > 1) {
            object = getObjectFieldValue(object, fields[0], newInstanceWhenNull);
            if (object == null) {
                return null;
            }
            String subField = fieldName.substring(fieldName.indexOf(".") + 1);
            return getObjectFieldValue(object, subField, newInstanceWhenNull);
        }

        // 处理 List/Map 对象的取值
        Matcher matcher = COMMON_FIELD_KEY.matcher(fieldName);
        if (matcher.find()) {
            //group #0 - all; #1 - (name); #2 - (index/key)
            String name = matcher.group(1);
            try {
                // 处理 List 对象的取值
                int index = Integer.parseInt(matcher.group(2));
                return getObject4ListField(object, name, index, newInstanceWhenNull);
            } catch (Exception ex) {
                // 处理 Map 对象的取值
                String key = matcher.group(2);
                return getObject4MapField(object, name, key, newInstanceWhenNull);
            }
        }
        // 处理 普通Java 对象的取值
        else {
            return getObject4NormalField(object, fieldName, newInstanceWhenNull);
        }
    }

    /**
     * 当newInstanceWhenNull为真，但数组下标越界时，返回 null。
     */
    private static Object getObject4ListField(Object parentObject, String selfFieldName, int index, boolean newInstanceWhenNull) {
        if (parentObject instanceof JSONObject) {
            // FastJSON::JSONArray
            JSONArray jsonArray = ((JSONObject) parentObject).getJSONArray(selfFieldName);

            if (jsonArray == null && newInstanceWhenNull) {
                jsonArray = new JSONArray();
                ReflectUtil.setFieldValue(parentObject, selfFieldName, jsonArray);
            }

            if (jsonArray != null && jsonArray.size() > index) {
                return jsonArray.getJSONObject(index);
            } else {
                return null;
            }
        } else {
            // Java::List
            List javaList = (List) ReflectUtil.getFieldValue(parentObject, selfFieldName);

            if (javaList == null && newInstanceWhenNull) {
                javaList = new ArrayList();
                ReflectUtil.setFieldValue(parentObject, selfFieldName, javaList);
            }

            if (javaList != null && javaList.size() > index) {
                return javaList.get(index);
            } else {
                return null;
            }
        }
    }

    private static Object getObject4MapField(Object parentObject, String selfFieldName, String key, boolean newInstanceWhenNull) {
        if (parentObject instanceof JSONObject) {
            // FastJSON::JSONObject
            JSONObject jsonObject = ((JSONObject) parentObject).getJSONObject(selfFieldName);

            if (jsonObject == null && newInstanceWhenNull) {
                jsonObject = new JSONObject();
                ReflectUtil.setFieldValue(parentObject, selfFieldName, jsonObject);
            }

            if (jsonObject != null) {
                return jsonObject.getJSONObject(key);
            } else {
                return null;
            }
        } else {
            // Java::Map
            Map javaMap = (Map) ReflectUtil.getFieldValue(parentObject, selfFieldName);

            if (javaMap == null && newInstanceWhenNull) {
                javaMap = new HashMap();
                ReflectUtil.setFieldValue(parentObject, selfFieldName, javaMap);
            }

            if (javaMap != null) {
                Object value = javaMap.get(key);
                if (value == null && newInstanceWhenNull) {
                    // 获取 Map 泛型的实际类型
                    Field selfField = ReflectUtil.getField(parentObject.getClass(), selfFieldName);
                    String selfFieldActualGenericClass = selfField.getAnnotatedType().getType().getTypeName();
                    Matcher matcher = ACTUAL_GENERIC_CLASS.matcher(selfFieldActualGenericClass);
                    if (matcher.find()) {
                        selfFieldActualGenericClass = matcher.group(1);
                        int pos = selfFieldActualGenericClass.indexOf(",");
                        selfFieldActualGenericClass = selfFieldActualGenericClass.substring(pos + 1).trim();
                        value = ReflectUtil.newInstance(selfFieldActualGenericClass);
                        javaMap.put(key, value);
                    }
                }
                return value;
            } else {
                return null;
            }
        }
    }

    private static Object getObject4NormalField(Object parentObject, String selfFieldName, boolean newInstanceWhenNull) {
        Object value = null;
        // 处理 FastJSON 数据类型
        if (parentObject instanceof JSONObject) {
            try {
                return ((JSONObject) parentObject).getJSONObject(selfFieldName);
            } catch (Exception ex1) {
                try {
                    return ((JSONObject) parentObject).getJSONArray(selfFieldName);
                } catch (Exception ex2) {
                    return ((JSONObject) parentObject).get(selfFieldName);
                }
            }
        } else if (parentObject instanceof Map) {
            // TODO: parentObject 为 Map 时，需要获取到map的泛型的具体类型，以便在获取不到数据时创建数据
            return ((Map) parentObject).get(selfFieldName);
        } else {
            value = ReflectUtil.getFieldValue(parentObject, selfFieldName);
        }

        if (value == null && newInstanceWhenNull) {
            Field fieldObject = ReflectUtil.getField(parentObject.getClass(), selfFieldName);
            Class<?> fieldClass = fieldObject.getType();
            String fieldActualGenericClass = fieldObject.getAnnotatedType().getType().getTypeName();
            Matcher matcher = ACTUAL_GENERIC_CLASS.matcher(fieldActualGenericClass);
            if (matcher.find()) {
                fieldActualGenericClass = matcher.group(1);
            }

            if (List.class.isAssignableFrom(fieldClass)) {
                // 处理 List : 默认添加一个元素，以方便后续获取 list 内对象类型
                Object ele = ReflectUtil.newInstanceIfPossible(ClassUtil.loadClass(fieldActualGenericClass));
                value = new ArrayList<>();
                ((List) value).add(ele);
            } else if (Map.class.isAssignableFrom(fieldClass)) {
                // 处理 Map : 默认添加一个元素，以方便后续获取 map 内对象类型
                //int pos = fieldActualGenericClass.indexOf(",");
                //fieldActualGenericClass = fieldActualGenericClass.substring(pos + 1).trim();
                //Object ele = ReflectUtil.newInstance(fieldActualGenericClass);
                value = new HashMap<>();
                //((Map) value).put(null, ele);
            } else if (!ReflectionUtils.isBasicDataType(fieldClass)) {
                value = ReflectUtil.newInstanceIfPossible(fieldClass);
            }
            if (value != null) {
                ReflectUtil.setFieldValue(parentObject, fieldObject, value);
            }

        }

        return value;
    }

    public static void copyData(Object source, String sourceField, Object target, String targetField, boolean filterNull) {
        if (source == null || target == null || DataUtils.isInvalid(sourceField) || DataUtils.isInvalid(targetField)) {
            return;
        }

        Object value = getObjectFieldValue(source, sourceField, false);
        /*if (source instanceof JSONObject) {
            value = ((JSONObject) source).get(sourceField);
        } else if (source instanceof Map) {
            value = ((Map) source).get(sourceField);
        } else {
            value = ReflectUtil.getFieldValue(source, sourceField);
        }*/
        if (filterNull && value == null) {
            return;
        }
        ReflectUtil.setFieldValue(target, targetField, value);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        String jsonChar = "{\"biz_pw_cx_acceptance\":{\"acceptance\":{\"OperateRecord\":\" Operate Record - acceptance\"}},"
                + "\"ui_jumpTo\":{\"id\":\"task/pw/cloud_exchange/:id\",\"title\":\"IPT 工单\",\"url\":\"https://app.zen"
                + ".dev/oss_vendor/task/pw/cloud_exchange/:id\"},\"biz_pw_cx_SAN\":{\"SAN_form1\":{\"Vendor\":\"CHINA UNICOM (AMERICAS) "
                + "OPERATIONS LTD.\",\"ISPResourceID\":\"1\",\"circuitType\":\"1\",\"sofTerm\":\"3\",\"DataCenter\":{\"label\":\"US-Los "
                + "Angeles (Coresite)\",\"key\":\"6\"},\"Device\":{\"label\":\"US-LAX2-CR-1-old\",\"key\":1},"
                + "\"DevicePort\":{\"label\":\"Any2-1-selfport\",\"key\":2},\"DeliveryDate\":\"2020-02-26 00:00:00\","
                + "\"BillingDate\":\"2020-02-28 00:00:00\",\"SANRadio\":\"true\",\"Remark\":\"12121\"},"
                + "\"SAN_form2\":{\"tableList\":[{\"ContactPhone\":\"4\",\"ContactMail\":\"3\",\"EscalationLevel\":\"1\","
                + "\"ContactPerson\":\"2\"}]}},\"biz_pw_cx_monitor\":{\"cxMonitor\":{\"OperateRecord\":\"Operate Record - cxMonitor\"}},"
                + "\"biz_pw_cx_QA\":{\"qaForm0\":{\"OperateRecord\":\"Operate Record - cxQA\"}},"
                + "\"biz_pw_cx_inputResourceData\":{\"resourceForm2\":{\"upload\":[{\"uid\":\"vc-upload-1582776790387-2\",\"size\":10478,"
                + "\"lastModifiedDate\":\"2019-09-10T09:06:25.109Z\",\"response\":{\"url\":\"https://oss-attach.oss-cn-hongkong.aliyuncs"
                + ".com/vendor/oss-static-origin/Fo5VSyw8Meky6-PhYLvAjJjp3vOg.jpeg\"},\"name\":\"3.jpeg\",\"lastModified\":1568106385109,"
                + "\"type\":\"image/jpeg\",\"percent\":0,\"url\":\"https://oss-attach.oss-cn-hongkong.aliyuncs"
                + ".com/vendor/oss-static-origin/Fo5VSyw8Meky6-PhYLvAjJjp3vOg.jpeg\","
                + "\"originFileObj\":{\"uid\":\"vc-upload-1582776790387-2\"},\"status\":\"done\"}],"
                + "\"tableList\":[{\"ContactPhone\":\"4\",\"ContactMail\":\"3\",\"EscalationLevel\":\"1\",\"ContactPerson\":\"2\"}],"
                + "\"Remark\":\"1\"},\"resourceForm1\":{\"SOFTerm\":\"3\",\"DataCenter\":{\"label\":\"US-Los Angeles (Coresite)\","
                + "\"key\":\"6\"},\"TicketOpenMethod\":\"1\",\"CommitmentBandwidth\":{\"CommitmentBandwidth1\":\"12\","
                + "\"CommitmentBandwidth2\":\"1\"},\"ISPResourceID\":\"1\",\"ISP\":\"1\",\"DevicePort\":{\"label\":\"Any2-1-selfport\","
                + "\"key\":2},\"DeliveryDate\":\"2020-02-26 00:00:00\",\"BillingDate\":\"2020-02-28 00:00:00\",\"CircuitType\":\"1\","
                + "\"device\":{\"label\":\"US-LAX2-CR-1-old\",\"key\":1},\"burstBandwidth\":{\"BurstBandwidth1\":\"1\","
                + "\"BurstBandwidth2\":\"2\"}}},\"biz_workflow_release_instance_id\":20368,"
                + "\"applicant\":\"59f1d850c7cb489ca0e169678b33aa92\",\"biz_workflow_release_instance_code\":\"PW0000000339\","
                + "\"biz_pw_cx_uploadDocument\":{\"upload\":{\"billingDate\":\"2020-02-28 00:00:00\",\"ISPResourceID\":\"1\","
                + "\"ticketOpenMethod\":\"1\",\"remark\":\"1\",\"deliveryDate\":\"2020-02-26 00:00:00\",\"circuitType\":\"1\","
                + "\"sofTerm\":\"3\"}},\"web_formConfig\":{\"resourceForm2\":{\"col\":\"12\",\"formLayout\":\"horizontal\","
                + "\"fixedLabelWidth\":\"180px\",\"columns\":[{\"col\":\"24\",\"en\":\"Support Info\",\"rules\":[],\"label\":\"Support "
                + "Info\",\"type\":\"table-list\",\"zh\":\"Support Info\",\"props\":\"tableList\",\"subColumns\":[{\"en\":\"Escalation "
                + "Level\",\"rules\":[{\"message\":\"此字段不能为空\",\"required\":true}],\"label\":\"Escalation Level\",\"isDisabled\":true,"
                + "\"type\":\"input\",\"zh\":\"Escalation Level\",\"props\":\"EscalationLevel\"},{\"en\":\"Contact Person\","
                + "\"rules\":[{\"message\":\"此字段不能为空\",\"required\":true}],\"label\":\"Contact Person\",\"isDisabled\":true,"
                + "\"type\":\"input\",\"zh\":\"Contact Person\",\"props\":\"ContactPerson\"},{\"en\":\"Contact Mail\","
                + "\"rules\":[{\"message\":\"此字段不能为空\",\"required\":true}],\"label\":\"Contact Mail\",\"isDisabled\":true,"
                + "\"type\":\"input\",\"zh\":\"Contact Mail\",\"props\":\"ContactMail\"},{\"en\":\"Contact Phone\","
                + "\"rules\":[{\"message\":\"此字段不能为空\",\"required\":true}],\"label\":\"Contact Phone\",\"isDisabled\":true,"
                + "\"type\":\"input\",\"zh\":\"Contact Phone\",\"props\":\"ContactPhone\"}],\"options\":[],\"name\":\"Support Info\","
                + "\"checked\":false,\"defaultNewItem\":{},\"isDisabled\":true},{\"col\":\"16\",\"en\":\"Support Remark\",\"rules\":[],"
                + "\"label\":\"Support Remark\",\"type\":\"oss-upload\",\"listType\":\"text\",\"zh\":\"Support Remark\","
                + "\"props\":\"upload\",\"maxUpload\":1,\"options\":[],\"name\":\"Support Remark\",\"checked\":false,\"uploading\":false,"
                + "\"isDisabled\":true},{\"options\":[],\"name\":\"Remark\",\"checked\":false,\"en\":\"Remark\",\"rules\":[{\"max\":100,"
                + "\"message\":\"Maximum 100 characters\"}],\"label\":\"Remark\",\"tag\":\"textarea\",\"isDisabled\":true,"
                + "\"type\":\"textarea\",\"zh\":\"Remark\",\"props\":\"Remark\"}]},\"acceptance\":{\"col\":\"12\","
                + "\"formLayout\":\"horizontal\",\"fixedLabelWidth\":\"180px\",\"columns\":[{\"options\":[],\"name\":\"Operate Record\","
                + "\"checked\":false,\"en\":\"Operate Record\",\"rules\":[{\"max\":100,\"message\":\"Maximum 100 characters\"},"
                + "{\"message\":\"This filed cannot be empty\",\"required\":true},{\"max\":100,\"message\":\"Maximum 100 characters\"}],"
                + "\"label\":\"Operate Record\",\"type\":\"textarea\",\"zh\":\"Operate Record\",\"props\":\"OperateRecord\","
                + "\"isDisabled\":true}]},\"upload\":{\"col\":\"12\",\"formLayout\":\"horizontal\",\"fixedLabelWidth\":\"180px\","
                + "\"columns\":[{\"col\":\"12\",\"name\":\"Circuit Type\",\"en\":\"Circuit Type\",\"rules\":[{\"message\":\"此字段不能为空\","
                + "\"required\":true}],\"label\":\"Circuit Type\",\"isDisabled\":true,\"type\":\"input\",\"zh\":\"Circuit Type\","
                + "\"props\":\"circuitType\"},{\"col\":\"12\",\"options\":[],\"name\":\"SOF Term\",\"en\":\"SOF Term\",\"rules\":[],"
                + "\"label\":\"SOF Term\",\"isDisabled\":true,\"type\":\"input\",\"zh\":\"SOF Term\",\"props\":\"sofTerm\"},"
                + "{\"col\":\"12\",\"name\":\"Ticket Open  Method\",\"en\":\"Ticket Open  Method\",\"rules\":[],\"label\":\"Ticket Open  "
                + "Method\",\"isDisabled\":true,\"type\":\"input\",\"zh\":\"Ticket Open  Method\",\"props\":\"ticketOpenMethod\"},"
                + "{\"col\":\"12\",\"name\":\"ISP Resource ID\",\"en\":\"ISP Resource ID\",\"rules\":[{\"message\":\"此字段不能为空\","
                + "\"required\":true}],\"label\":\"ISP Resource ID\",\"isDisabled\":true,\"type\":\"input\",\"zh\":\"ISP Resource ID\","
                + "\"props\":\"ISPResourceID\"},{\"col\":\"12\",\"options\":[],\"name\":\"Delivery date\",\"checked\":false,"
                + "\"en\":\"Delivery date\",\"rules\":[{\"message\":\"This filed cannot be empty\",\"required\":true}],"
                + "\"label\":\"Delivery date\",\"placeholder\":\"chose date\",\"isDisabled\":true,\"type\":\"datepicker\","
                + "\"zh\":\"Delivery date\",\"props\":\"deliveryDate\"},{\"col\":\"12\",\"options\":[],\"name\":\"Billing date\","
                + "\"checked\":false,\"en\":\"Billing date\",\"rules\":[],\"label\":\"Billing date\",\"placeholder\":\"选择日期\","
                + "\"isDisabled\":true,\"type\":\"datepicker\",\"zh\":\"Billing date\",\"props\":\"billingDate\"},{\"col\":\"24\","
                + "\"name\":\"Remark\",\"en\":\"Remark\",\"rules\":[{\"max\":100,\"message\":\"输入的最大长度为100\"}],\"label\":\"Remark\","
                + "\"tag\":\"textarea\",\"isDisabled\":true,\"type\":\"textarea\",\"zh\":\"Remark\",\"props\":\"remark\"}]},"
                + "\"xConnect\":{\"col\":\"12\",\"formLayout\":\"horizontal\",\"fixedLabelWidth\":\"180px\",\"columns\":[{\"col\":\"16\","
                + "\"labelInValue\":true,\"alreadyAll\":false,\"searchFetch\":true,\"fieldNames\":{\"text\":\"id|deviceName|portName\","
                + "\"value\":\"id\"},\"fetchParams\":{\"exactValue\":\"id:1\",\"pageSize\":10,\"queryValue\":\"\",\"pageNum\":1},"
                + "\"en\":\"Cloud/Platfrom Port\",\"rules\":[{\"message\":\"This filed cannot be empty\",\"required\":true}],"
                + "\"label\":\"Cloud/Platfrom Port\",\"type\":\"select\",\"searchApi\":\"/resourceApi/uplink\",\"zh\":\"Port Code\","
                + "\"props\":\"RelatedPort\",\"useToken\":\"jwt\",\"options\":[{\"text\":\"1-US-LAX1-XR-1-下线-TenGigE0/5/0/0\","
                + "\"value\":1}],\"name\":\"Cloud/Platfrom Port\",\"checked\":false,\"reset\":false,\"isDisabled\":true,"
                + "\"searchLoading\":true}]},\"cxMonitor\":{\"col\":\"12\",\"formLayout\":\"horizontal\",\"fixedLabelWidth\":\"180px\","
                + "\"columns\":[{\"options\":[],\"name\":\"Operate Record\",\"checked\":false,\"en\":\"Operate Record\","
                + "\"rules\":[{\"max\":100,\"message\":\"Maximum 100 characters\"},{\"message\":\"This filed cannot be empty\","
                + "\"required\":true},{\"max\":100,\"message\":\"Maximum 100 characters\"}],\"label\":\"Operate Record\","
                + "\"isDisabled\":true,\"type\":\"textarea\",\"zh\":\"Operate Record\",\"props\":\"OperateRecord\"}]},"
                + "\"qaForm0\":{\"col\":\"12\",\"formLayout\":\"horizontal\",\"fixedLabelWidth\":\"180px\",\"columns\":[{\"options\":[],"
                + "\"name\":\"Operate Record\",\"checked\":false,\"en\":\"Operate Record\",\"rules\":[{\"max\":100,\"message\":\"Maximum "
                + "100 characters\"},{\"message\":\"This filed cannot be empty\",\"required\":true},{\"max\":100,\"message\":\"Maximum "
                + "100 characters\"}],\"label\":\"Operate Record\",\"isDisabled\":true,\"type\":\"textarea\",\"zh\":\"Operate Record\","
                + "\"props\":\"OperateRecord\"}]},\"nppConfig\":{\"col\":\"12\",\"formLayout\":\"horizontal\","
                + "\"fixedLabelWidth\":\"180px\",\"columns\":[{\"options\":[{\"label\":\"Checked\",\"value\":\"Checked\"}],"
                + "\"name\":\"Layer 2 Senssion\",\"checked\":false,\"en\":\"Layer 2 Senssion\",\"rules\":[{\"message\":\"This filed "
                + "cannot be empty\",\"required\":true}],\"label\":\"Layer 2 Senssion\",\"isDisabled\":true,\"type\":\"checkboxGroup\","
                + "\"list\":[\"Checked\"],\"zh\":\"Layer 2 Senssion\",\"props\":\"Layer2Senssion\"},{\"col\":\"16\",\"options\":[],"
                + "\"name\":\"Optical Power\",\"checked\":false,\"en\":\"Optical Power\",\"rules\":[{\"message\":\"This filed cannot be "
                + "empty\",\"required\":true}],\"label\":\"Optical Power\",\"isDisabled\":true,\"type\":\"input\",\"zh\":\"Optical "
                + "Power\",\"props\":\"OpticalPower\"},{\"col\":\"16\",\"options\":[],\"name\":\"CRC\",\"checked\":false,\"en\":\"CRC\","
                + "\"rules\":[{\"message\":\"This filed cannot be empty\",\"required\":true}],\"label\":\"CRC\",\"isDisabled\":true,"
                + "\"type\":\"input\",\"zh\":\"CRC\",\"props\":\"CRC\"},{\"options\":[],\"name\":\"Remark\",\"checked\":false,"
                + "\"en\":\"Remark\",\"rules\":[{\"message\":\"This filed cannot be empty\",\"required\":true},{\"max\":100,"
                + "\"message\":\"Maximum 100 characters\"}],\"label\":\"Remark\",\"tag\":\"textarea\",\"isDisabled\":true,"
                + "\"type\":\"textarea\",\"zh\":\"Remark\",\"props\":\"Remark\"}]},\"assignPort\":{\"col\":\"12\","
                + "\"formLayout\":\"horizontal\",\"fixedLabelWidth\":\"180px\",\"columns\":[{\"col\":\"24\",\"labelInValue\":true,"
                + "\"searchFetch\":true,\"fieldNames\":{\"text\":\"facName\",\"value\":\"facId\"},"
                + "\"fetchParams\":[{\"text\":\"pageSize\",\"value\":10},{\"text\":\"pageNum\",\"value\":1},{\"text\":\"status\","
                + "\"value\":\"0,2\"}],\"en\":\"Data Center\",\"rules\":[],\"label\":\"Data Center\",\"type\":\"select\","
                + "\"searchApi\":\"/resourceApi/facility\",\"zh\":\"Data Center\",\"props\":\"DataCenter\",\"useToken\":\"jwt\","
                + "\"options\":[{\"text\":\"US-Los Angeles (Coresite)\",\"value\":\"6\"}],\"name\":\"Data Center\",\"checked\":false,"
                + "\"reset\":false,\"isDisabled\":true,\"id\":3,\"searchLoading\":false},{\"col\":\"24\",\"name\":\"Device\","
                + "\"en\":\"Device\",\"rules\":[{\"message\":\"此字段不能为空\",\"required\":true},{\"max\":100,\"message\":\"输入的最大长度为100\"},"
                + "{\"message\":\"此字段不能为空\",\"required\":true},{\"max\":100,\"message\":\"输入的最大长度为100\"}],\"label\":\"Device\","
                + "\"tag\":\"textarea\",\"isDisabled\":true,\"type\":\"textarea\",\"zh\":\"Device\",\"props\":\"Device\"},"
                + "{\"col\":\"24\",\"options\":[],\"name\":\"Description\",\"checked\":false,\"en\":\"Description\","
                + "\"rules\":[{\"max\":200,\"message\":\"Maximum 200 characters\"},{\"max\":200,\"message\":\"Maximum 200 characters\"}],"
                + "\"label\":\"Description\",\"tag\":\"textarea\",\"isDisabled\":true,\"type\":\"textarea\",\"zh\":\"备注\","
                + "\"props\":\"remark\"}]},\"resourceForm1\":{\"col\":\"12\",\"formLayout\":\"horizontal\",\"fixedLabelWidth\":\"180px\","
                + "\"columns\":[{\"col\":\"24\",\"labelInValue\":true,\"searchFetch\":true,\"fieldNames\":{\"text\":\"facName\","
                + "\"value\":\"facId\"},\"fetchParams\":{\"pageSize\":10,\"pageNum\":1,\"status\":\"0,2\"},\"en\":\"Data Center\","
                + "\"label\":\"Data Center\",\"type\":\"select\",\"searchApi\":\"/resourceApi/facility\",\"zh\":\"Data Center\","
                + "\"props\":\"DataCenter\",\"useToken\":\"jwt\",\"options\":[{\"text\":\"US-Los Angeles (Coresite)\",\"value\":\"6\"}],"
                + "\"name\":\"Data Center\",\"checked\":false,\"reset\":false,\"isDisabled\":true,\"searchLoading\":false},"
                + "{\"col\":\"12\",\"labelInValue\":true,\"alreadyAll\":false,\"childProps\":{\"DevicePort\":\"deviceId\"},"
                + "\"searchFetch\":true,\"fieldNames\":{\"text\":\"devDesc\",\"value\":\"devId\"},"
                + "\"fetchParams\":{\"exactValue\":\"devId:1\",\"pageSize\":10,\"queryValue\":\"\",\"type\":1,\"pageNum\":1},"
                + "\"en\":\"Device\",\"rules\":[{\"message\":\"This filed cannot be empty\",\"required\":true}],\"label\":\"Device\","
                + "\"type\":\"select\",\"searchApi\":\"/resourceApi/device\",\"zh\":\"Device\",\"props\":\"device\",\"useToken\":\"jwt\","
                + "\"options\":[{\"text\":\"US-LAX2-CR-1-old\",\"value\":1}],\"name\":\"Device\",\"checked\":false,\"reset\":false,"
                + "\"isDisabled\":true,\"allowClear\":true,\"searchLoading\":false},{\"col\":\"12\",\"labelInValue\":true,"
                + "\"alreadyAll\":false,\"searchFetch\":true,\"fieldNames\":{\"text\":\"name\",\"value\":\"deviceInterfaceId\"},"
                + "\"fetchParams\":{\"exactValue\":\"deviceInterfaceId:2\",\"pageSize\":10,\"queryValue\":\"\",\"type\":1,\"pageNum\":1,"
                + "\"deviceId\":0},\"en\":\"Device Port\",\"rules\":[{\"message\":\"This filed cannot be empty\",\"required\":true}],"
                + "\"label\":\"Device Port\",\"type\":\"select\",\"searchApi\":\"/resourceApi/deviceInterface\",\"zh\":\"Device Port\","
                + "\"props\":\"DevicePort\",\"useToken\":\"jwt\",\"options\":[{\"text\":\"Any2-1-selfport\",\"value\":2}],"
                + "\"name\":\"Device Port\",\"checked\":false,\"reset\":false,\"isDisabled\":true,\"searchLoading\":false},"
                + "{\"col\":\"12\",\"options\":[],\"name\":\"ISP\",\"checked\":false,\"en\":\"ISP\",\"rules\":[{\"message\":\"This filed "
                + "cannot be empty\",\"required\":true}],\"label\":\"ISP\",\"isDisabled\":true,\"type\":\"input\",\"zh\":\"ISP\","
                + "\"props\":\"ISP\"},{\"col\":\"12\",\"options\":[],\"name\":\"ISP Resource ID\",\"checked\":false,\"en\":\"ISP Resource"
                + " ID\",\"label\":\"ISP Resource ID\",\"isDisabled\":true,\"type\":\"input\",\"zh\":\"ISP Resource ID\","
                + "\"props\":\"ISPResourceID\"},{\"col\":\"12\",\"subColumns\":[{\"col\":\"12\",\"en\":\"Commitment Bandwidth Input\","
                + "\"rules\":[{\"message\":\"This filed cannot be empty\",\"required\":true}],\"label\":\"Commitment Bandwidth Input\","
                + "\"isDisabled\":true,\"type\":\"input-number\",\"zh\":\"Commitment Bandwidth Input\","
                + "\"props\":\"CommitmentBandwidth1\"},{\"options\":[{\"text\":\"MB\",\"value\":\"1\"},{\"text\":\"GB\",\"value\":\"2\"},"
                + "{\"text\":\"TB\",\"value\":\"3\"}],\"en\":\"Commitment Bandwidth Sel\",\"reset\":false,\"rules\":[{\"message\":\"This "
                + "filed cannot be empty\",\"required\":true}],\"label\":\"Commitment Bandwidth Sel\",\"isDisabled\":true,"
                + "\"type\":\"select\",\"zh\":\"Commitment Bandwidth Sel\",\"props\":\"CommitmentBandwidth2\"}],\"options\":[],"
                + "\"name\":\"Commitment Bandwidth\",\"checked\":false,\"en\":\"Commitment Bandwidth\",\"rules\":[{\"message\":\"This "
                + "filed cannot be empty\",\"required\":true}],\"label\":\"Commitment Bandwidth\",\"isDisabled\":true,\"type\":\"group\","
                + "\"zh\":\"Commitment Bandwidth\",\"props\":\"CommitmentBandwidth\"},{\"col\":\"12\",\"subColumns\":[{\"col\":\"12\","
                + "\"options\":[],\"checked\":false,\"en\":\"Burst Bandwidth Input\",\"rules\":[],\"label\":\"Burst Bandwidth Input\","
                + "\"isDisabled\":true,\"type\":\"input-number\",\"zh\":\"Burst Bandwidth Input\",\"props\":\"BurstBandwidth1\"},"
                + "{\"col\":\"12\",\"options\":[{\"text\":\"MB\",\"value\":\"1\"},{\"text\":\"GB\",\"value\":\"2\"},{\"text\":\"TB\","
                + "\"value\":\"3\"}],\"checked\":false,\"en\":\"Burst Bandwidth Sel\",\"reset\":false,\"rules\":[],\"label\":\"Burst "
                + "Bandwidth Sel\",\"isDisabled\":true,\"type\":\"select\",\"zh\":\"Burst Bandwidth Sel\","
                + "\"props\":\"BurstBandwidth2\"}],\"options\":[],\"name\":\"Burst Bandwidth\",\"checked\":false,\"en\":\"Burst "
                + "Bandwidth\",\"label\":\"Burst Bandwidth\",\"isDisabled\":true,\"type\":\"group\",\"zh\":\"Burst Bandwidth\","
                + "\"props\":\"burstBandwidth\"},{\"col\":\"12\",\"options\":[],\"name\":\"Delivery Date\",\"checked\":false,"
                + "\"en\":\"Delivery Date\",\"rules\":[{\"message\":\"This filed cannot be empty\",\"required\":true}],"
                + "\"label\":\"Delivery Date\",\"placeholder\":\"选择日期\",\"isDisabled\":true,\"type\":\"datepicker\",\"zh\":\"Delivery "
                + "Date\",\"props\":\"DeliveryDate\"},{\"col\":\"12\",\"options\":[],\"name\":\"Billing Date\",\"checked\":false,"
                + "\"en\":\"Billing Date\",\"label\":\"Billing Date\",\"placeholder\":\"选择日期\",\"isDisabled\":true,"
                + "\"type\":\"datepicker\",\"zh\":\"Billing Date\",\"props\":\"BillingDate\"},{\"col\":\"12\",\"options\":[],"
                + "\"name\":\"Circuit Type\",\"checked\":false,\"en\":\"Circuit Type\",\"label\":\"Circuit Type\",\"isDisabled\":true,"
                + "\"type\":\"input\",\"zh\":\"Circuit Type\",\"props\":\"CircuitType\"},{\"col\":\"12\",\"options\":[],\"name\":\"SOF "
                + "Term\",\"checked\":false,\"en\":\"SOF Term\",\"label\":\"SOF Term\",\"isDisabled\":true,\"type\":\"input\","
                + "\"zh\":\"SOF Term\",\"props\":\"SOFTerm\"},{\"col\":\"12\",\"options\":[],\"name\":\"Ticket Open Method\","
                + "\"checked\":false,\"en\":\"Ticket Open Method\",\"label\":\"Ticket Open Method\",\"isDisabled\":true,"
                + "\"type\":\"input\",\"zh\":\"Ticket Open Method\",\"props\":\"TicketOpenMethod\"}]},"
                + "\"SAN_form1\":{\"formLayout\":\"horizontal\",\"col\":\"12\",\"fixedLabelWidth\":\"180px\","
                + "\"columns\":[{\"col\":\"12\",\"options\":[],\"name\":\"Vendor\",\"checked\":false,\"en\":\"Vendor\",\"rules\":[],"
                + "\"label\":\"Vendor\",\"isDisabled\":true,\"type\":\"input\",\"zh\":\"Vendor\",\"props\":\"Vendor\"},{\"col\":\"12\","
                + "\"options\":[],\"name\":\"ISP Resource ID\",\"checked\":false,\"en\":\"ISP Resource ID\",\"rules\":[],\"label\":\"ISP "
                + "Resource ID\",\"isDisabled\":true,\"type\":\"input\",\"zh\":\"ISP Resource ID\",\"props\":\"ISPResourceID\"},"
                + "{\"col\":\"12\",\"options\":[],\"name\":\"Circuit Type\",\"checked\":false,\"en\":\"Circuit Type\",\"rules\":[],"
                + "\"label\":\"Circuit Type\",\"type\":\"input\",\"zh\":\"Circuit Type\",\"props\":\"circuitType\"},{\"col\":\"12\","
                + "\"options\":[],\"name\":\"SOF Term\",\"checked\":false,\"en\":\"SOF Term\",\"rules\":[],\"label\":\"SOF Term\","
                + "\"type\":\"input\",\"zh\":\"SOF Term\",\"props\":\"sofTerm\"},{\"col\":\"12\",\"labelInValue\":true,"
                + "\"searchFetch\":true,\"fieldNames\":{\"text\":\"facName\",\"value\":\"facId\"},\"fetchParams\":{\"pageSize\":10,"
                + "\"pageNum\":1,\"status\":\"0,2\"},\"en\":\"Data Center\",\"rules\":[],\"label\":\"Data Center\",\"type\":\"select\","
                + "\"searchApi\":\"/resourceApi/facility\",\"zh\":\"Data Center\",\"props\":\"DataCenter\",\"useToken\":\"jwt\","
                + "\"options\":[{\"text\":\"US-Los Angeles (Coresite)\",\"value\":\"6\"}],\"name\":\"Data Center\",\"checked\":false,"
                + "\"isDisabled\":true,\"searchLoading\":false},{\"col\":\"12\",\"labelInValue\":true,\"searchFetch\":true,"
                + "\"fieldNames\":{\"text\":\"devDesc\",\"value\":\"devId\"},\"fetchParams\":{\"pageSize\":10,\"queryValue\":\"\","
                + "\"type\":1,\"pageNum\":1},\"en\":\"Device\",\"rules\":[],\"label\":\"Device\",\"type\":\"select\","
                + "\"searchApi\":\"/resourceApi/device\",\"zh\":\"Device\",\"props\":\"Device\",\"useToken\":\"jwt\","
                + "\"options\":[{\"text\":\"US-LAX2-CR-1-old\",\"value\":1}],\"name\":\"Device\",\"checked\":false,\"isDisabled\":true,"
                + "\"searchLoading\":false},{\"labelInValue\":true,\"searchFetch\":true,\"fieldNames\":{\"text\":\"name\","
                + "\"value\":\"deviceInterfaceId\"},\"fetchParams\":{\"pageSize\":10,\"queryValue\":\"\",\"type\":1,\"pageNum\":1},"
                + "\"en\":\"Device Port\",\"rules\":[],\"label\":\"Device Port\",\"type\":\"select\","
                + "\"searchApi\":\"/resourceApi/deviceInterface\",\"zh\":\"Device Port\",\"props\":\"DevicePort\",\"useToken\":\"jwt\","
                + "\"options\":[{\"text\":\"Any2-1-selfport\",\"value\":2}],\"name\":\"Device Port\",\"checked\":false,"
                + "\"isDisabled\":true,\"searchLoading\":false},{\"col\":\"12\",\"options\":[],\"name\":\"Delivery Date\","
                + "\"checked\":false,\"en\":\"Delivery Date\",\"rules\":[],\"label\":\"Delivery Date\",\"isDisabled\":true,"
                + "\"placeholder\":\"选择日期\",\"type\":\"datepicker\",\"zh\":\"Delivery Date\",\"props\":\"DeliveryDate\"},{\"col\":\"12\","
                + "\"options\":[],\"name\":\"Billing Date\",\"checked\":false,\"en\":\"Billing Date\",\"rules\":[],\"label\":\"Billing "
                + "Date\",\"isDisabled\":true,\"placeholder\":\"选择日期\",\"type\":\"datepicker\",\"zh\":\"Billing Date\","
                + "\"props\":\"BillingDate\"},{\"col\":\"8\",\"options\":[{\"label\":\"是\",\"value\":\"true\"},{\"label\":\"否\","
                + "\"value\":\"false\"}],\"name\":\"Whether to send SAN\",\"checked\":false,\"en\":\"Whether to send SAN\",\"rules\":[],"
                + "\"label\":\"Whether to send SAN\",\"type\":\"radioGroup\",\"zh\":\"是否发SAN\",\"props\":\"SANRadio\"},"
                + "{\"slotName\":\"biz_sanTemplatePreview\",\"col\":\"4\",\"options\":[],\"name\":\"\",\"checked\":false,\"en\":\"\","
                + "\"label\":\"\",\"type\":\"slot\",\"zh\":\"\",\"props\":\"biz_sanTemplatePreview\"},{\"col\":\"24\",\"options\":[],"
                + "\"name\":\"Remark\",\"checked\":false,\"en\":\"Remark\",\"rules\":[],\"label\":\"Remark\",\"type\":\"textarea\","
                + "\"zh\":\"Remark\",\"props\":\"Remark\"}]},\"SAN_form2\":{\"formLayout\":\"horizontal\",\"col\":\"12\","
                + "\"fixedLabelWidth\":\"180px\",\"columns\":[{\"col\":\"24\",\"subColumns\":[{\"en\":\"Escalation Level\","
                + "\"rules\":[{\"message\":\"此字段不能为空\",\"required\":true}],\"label\":\"Escalation Level\",\"type\":\"input\","
                + "\"zh\":\"Escalation Level\",\"props\":\"EscalationLevel\"},{\"en\":\"Contact Person\","
                + "\"rules\":[{\"message\":\"此字段不能为空\",\"required\":true}],\"label\":\"Contact Person\",\"type\":\"input\","
                + "\"zh\":\"Contact Person\",\"props\":\"ContactPerson\"},{\"en\":\"Contact Mail\",\"rules\":[{\"message\":\"此字段不能为空\","
                + "\"required\":true}],\"label\":\"Contact Mail\",\"type\":\"input\",\"zh\":\"Contact Mail\",\"props\":\"ContactMail\"},"
                + "{\"en\":\"Contact Phone\",\"rules\":[{\"message\":\"此字段不能为空\",\"required\":true}],\"label\":\"Contact Phone\","
                + "\"type\":\"input\",\"zh\":\"Contact Phone\",\"props\":\"ContactPhone\"}],\"options\":[],\"name\":\"Support Info\","
                + "\"checked\":false,\"defaultNewItem\":{},\"en\":\"Support Info\",\"rules\":[],\"label\":\"Support Info\","
                + "\"type\":\"table-list\",\"zh\":\"Support Info\",\"props\":\"tableList\"}]}},"
                + "\"biz_pw_ipt_xConnect\":{\"xConnect\":{\"RelatedPort\":{\"label\":\"1-US-LAX1-XR-1-下线-TenGigE0/5/0/0\",\"key\":1}}},"
                + "\"biz_pw_cx_nppConfig\":{\"nppConfig\":{\"CRC\":\"1\",\"Layer2Senssion\":[\"Checked\"],\"OpticalPower\":\"1\","
                + "\"Remark\":\"1\"}},\"biz_pw_cx_assignPort\":{\"assignPort\":{\"DataCenter\":{\"label\":\"US-Los Angeles (Coresite)\","
                + "\"key\":\"6\"},\"Device\":\"1\",\"remark\":\"1\"}}}\n";

        /*String sourceKey = "biz_pw_cx_SAN.SAN_form2.tableList[*] { ContactPhone, ContactMail, EscalationLevel, ContactPerson }";
        //String sourceKey = "biz_pw_cx_QA.qaForm0.OperateRecord";
        String targetKey = "parent.children[*]{ remark, email, classNumber, name }";
        //String targetKey = "parent.family[record].remark";

        JSONObject source = JSON.parseObject(jsonChar);
        FieldValue fieldValue = getFieldValue(source, sourceKey);
        System.out.println(fieldValue);

        Example target = new Example();
        setFieldValue(target, targetKey, fieldValue, true);
        System.out.println(target);*/

        FlowRefectionMapper mapper;
        List<FlowRefectionMapper> mappers = new ArrayList<>();
        // 1
        mapper = new FlowRefectionMapper();
        mapper.setTargetKey("parent.children[*]{ remark, email, classNumber, name }");
        mapper.setSourceKeys(
                new String[] {"biz_pw_cx_SAN.SAN_form2.tableList[*] { ContactPhone, ContactMail, EscalationLevel, ContactPerson }"});
        mapper.setFilterNull(false);
        mappers.add(mapper);
        // 2
        mapper = new FlowRefectionMapper();
        mapper.setTargetKey("parent.family[record].remark");
        mapper.setSourceKeys(new String[] {"biz_pw_cx_QA.qaForm0.OperateRecord", "biz_pw_cx_monitor.cxMonitor.OperateRecord",
                "biz_pw_cx_acceptance.acceptance1.OperateRecord"});
        mapper.setFilterNull(true);
        mappers.add(mapper);
        // 3
        mapper = new FlowRefectionMapper();
        mapper.setTargetKey("parent.family[record].name");
        mapper.setSourceKeys(new String[] {"biz_pw_cx_inputResourceData.resourceForm1.device.label"});
        mapper.setFilterNull(false);
        mappers.add(mapper);

        JSONObject source = JSON.parseObject(jsonChar);
        Example target = new Example();
        doProcess(source, target, mappers);
        System.out.println("➣ source \n" + JSON.toJSONString(source));
        System.out.println("➢ target \n" + JSON.toJSONString(target, SerializerFeature.PrettyFormat));
    }
}
