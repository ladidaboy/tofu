package cn.hl.ax.spring.base.select;

import cn.hl.ax.data.DataUtils;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 关键词查询
 * <ol>
 * <li>Field.ClassType == String : 模糊匹配(like)</li>
 * <li>Field.ClassType != String : 精确匹配(若keyword值转字段类型失败时则忽略此字段)</li>
 * </ol>
 *
 * @author hyman
 * @date 2019-11-29 10:17:19
 */
public interface SelectKeywordQO extends SelectBaseQO {
    /**
     * <p>是否开启关键词查询</p>
     *
     * @return openSearch : true.开启关键词查询; false.关闭关键词查询
     */
    default boolean isOpenSearch() {
        return true;
    }

    /**
     * <p>获取 关键词查询使用的字段列表</p>
     * <br/>若为空，Dao层会自动获取具体的DO类型中使用`@Keyword`注解的字段
     *
     * @return fields
     */
    default List<String> getFields() {
        return null;
    }

    /**
     * <p>获取 关键词值</p>
     *
     * @return keyword
     */
    default String getKeyword() {
        return null;
    }

    /**
     * <p>是否开启模糊查询(模糊查询时, 需要前后添加通配符)</p>
     *
     * @return fuzzyQuery : true.模糊匹配, false.精确匹配
     */
    default boolean isFuzzyQuery() {
        return true;
    }

    /**
     * <p>获取 自定义查询条件( field1 : value1, value2; field2 : value3 )</p>
     *
     * @return condition
     */
    default String getCondition() {
        return null;
    }

    /**************************************************************
     * <p>获取 DO模型中使用`@Keyword`注解的字符类型的属性名称</p>
     * @param clz DO 模型
     * @return fields
     */
    static KeywordInfo getFields(Class clz) {
        KeywordInfo info = new KeywordInfo();
        info.setAllFields(new ArrayList<>());
        info.setFuzzyMatches(new ArrayList<>());
        info.setExactMatches(new ArrayList<>());
        Field[] fields = ReflectUtil.getFields(clz);
        if (DataUtils.isValid(fields)) {
            for (Field field : fields) {
                if (field.getAnnotation(Keyword.class) != null) {
                    info.getAllFields().add(field.getName());
                    if (field.getType() == String.class) {
                        info.getFuzzyMatches().add(field.getName());
                    } else {
                        info.getExactMatches().add(field.getName());
                    }
                }
            }
        }
        return info;
    }

    /**************************************************************
     * <p>获取 DO模型中使用`@Keyword`注解的字符类型的属性名称备注说明</p>
     * @param clz DO 模型
     * @return 备注说明
     */
    static String getFieldsRemarks(Class clz) {
        return getFieldsRemarks(clz, true);
    }

    /**
     * <p>获取 DO模型中使用`@Keyword`注解的字符类型的属性名称备注说明</p>
     *
     * @param clz    DO 模型
     * @param langCN true.中文; false.英文
     * @return 备注说明
     */
    static String getFieldsRemarks(Class clz, boolean langCN) {
        List<String> fuzzy = new ArrayList<>(), precise = new ArrayList<>(), values = new ArrayList<>();
        Field[] fields = ReflectUtil.getFields(clz);
        if (DataUtils.isValid(fields)) {
            for (Field field : fields) {
                if (field.getAnnotation(Keyword.class) != null) {
                    if (field.getType() == String.class) {
                        fuzzy.add(field.getName());
                    } else {
                        precise.add(field.getName());
                    }
                }
            }
        }

        String fieldValues;
        if (fuzzy.size() > 0) {
            fieldValues = ArrayUtil.join(fuzzy.toArray(new String[0]), "/");
            values.add((langCN ? "模糊匹配" : "Fuzzy matching") + ": " + fieldValues);
        }
        if (precise.size() > 0) {
            fieldValues = ArrayUtil.join(precise.toArray(new String[0]), "/");
            values.add((langCN ? "精确匹配" : "Exact matching") + ": " + fieldValues);
        }
        if (values.size() > 0) {
            fieldValues = ArrayUtil.join(values.toArray(new String[0]), "; ");
        } else {
            fieldValues = null;
        }

        return (langCN ? "查询关键词" : "Query keywords ") + (fieldValues == null ? "" : ("(" + fieldValues + ")"));
    }

    /**************************************************************
     * <p>设置字段的ApiModelProperty属性值</p>
     * @param field 字段
     * @param value 备注
     */
    static void setApiModelPropertyValue(Field field, Object value) {
        try {
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            InvocationHandler handler = Proxy.getInvocationHandler(annotation);
            Field memberValuesField = handler.getClass().getDeclaredField("memberValues");
            memberValuesField.setAccessible(true);
            Map memberValues = (Map) memberValuesField.get(handler);
            memberValues.put("value", value);
        } catch (Exception e) {
            //LOGGER.error(StrUtil.format("Filed {} setValue, failed: {}", field.getName(), e.getMessage()));
        }
    }
}
