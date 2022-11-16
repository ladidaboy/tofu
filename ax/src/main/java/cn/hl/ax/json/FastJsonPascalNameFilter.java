package cn.hl.ax.json;

import cn.hl.ax.data.DataUtils;
import cn.hl.ax.log.LogUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.NameFilter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @author Halfman
 * @date 2019-10-15 13:45:20
 */
@Slf4j
public class FastJsonPascalNameFilter implements NameFilter {
    public static final FastJsonPascalNameFilter INSTANCE = new FastJsonPascalNameFilter();

    //多个别名时(alternateNames): 使用的别名序号,从0开始
    private int     nameIndex = 0;
    //多个别名时(alternateNames): true-优先使用第一个; false-优先使用最后一个
    private Boolean useFirst  = null;

    /**
     * FastJSON属性名称过滤器
     */
    public FastJsonPascalNameFilter() {

    }

    /**
     * FastJSON属性名称过滤器
     *
     * @param nameIndex 多个别名时(alternateNames): 使用的别名序号,从0开始
     */
    public FastJsonPascalNameFilter(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    /**
     * FastJSON属性名称过滤器
     *
     * @param useFirst 多个别名时(alternateNames): true-优先使用第一个; false-优先使用最后一个
     */
    public FastJsonPascalNameFilter(boolean useFirst) {
        this.useFirst = useFirst;
    }

    @Override
    public String process(Object object, String name, Object value) {
        try {
            Class clz = object.getClass();
            // 获取 类注解 @JSONType
            //JSONType type = (JSONType) clz.getAnnotation(JSONType.class);
            //System.out.println(type);
            // 获取 字段注解 @JSONField
            Field fld = clz.getDeclaredField(name);
            JSONField field = fld.getAnnotation(JSONField.class);
            if (field != null) {
                String jsonName = field.name();
                String[] jsonNames = field.alternateNames();
                if (DataUtils.isInvalid(jsonName)) {
                    return jsonName;
                } else if (DataUtils.isInvalid(jsonNames)) {
                    if (useFirst != null) {
                        if (useFirst) {
                            nameIndex = 0;
                        } else {
                            nameIndex = jsonNames.length - 1;
                        }
                    }
                    nameIndex = Math.min(nameIndex, jsonNames.length - 1);
                    nameIndex = Math.max(nameIndex, 0);
                    return jsonNames[nameIndex];
                }
            }
            // 按驼峰进行name转换
            return DataUtils.humpToLine(name);
        } catch (NoSuchFieldException e) {
            //log.error("process error : NoSuchField({})!", name);
        } catch (Exception e) {
            log.error("process({}) error!\n{}", name, LogUtils.getSimpleMessages(e));
        }
        return name;
    }
}
