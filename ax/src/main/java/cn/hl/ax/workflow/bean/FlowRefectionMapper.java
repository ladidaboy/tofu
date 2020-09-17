package cn.hl.ax.workflow.bean;

import cn.hl.ax.data.DataUtils;

import java.util.List;

/**
 * <b>映射规则</b>
 * <ul>
 *     <li>标准: attr.child[2].map[key]</li>
 *     <li>扩展: attr.child[*]{}</li>
 *     <li>扩展: attr.child[*]{sub, field}</li>
 * </ul>
 * 扩展映射时1：前后两个对象的key值，以出现顺序方式进行转换。
 * <ul>
 *     <li>例如：anchorKey = tt[*]{aa, b, c} <-- sourceKey = ss[*]{x, y, zz}</li>
 *     <li>结果：从ss列表对象映射到tt列表对象，对象内映射方式: x->aa, y->b, zz->c</li>
 * </ul>
 * 扩展映射时2：anchorKey为空，使用sourceKey的第一个字段进行赋值。
 * <ul>
 *     <li>例如：anchorKey = tt[*]{} <-- sourceKey = ss[*]{fd}</li>
 *     <li>结果：将ss列表对象的fd属性值，转换为tt列表</li>
 * </ul>
 * <b>扩展映射在一个KEY中最多只能出现一次并且在末尾。例如错误示范: tt[*]{aa, b, c}.ff</b><br>
 * <u>以上两种映射方式都会受checkValidValue限制</u>
 * @author hyman
 * @date 2020-03-03 16:36:48
 * @version $ Id: FlowRefectionMapper.java, v 0.1  hyman Exp $
 */
public class FlowRefectionMapper {
    private String   targetKey;
    private String[] sourceKeys;
    private boolean  filterNull;

    public FlowRefectionMapper() {

    }

    /**
     * 反射机制映射关系对象
     * @param targetKey 目标属性
     * @param sourceKeys 数据来源属性列表(优先级: 越靠后优先级越高)
     * @param filterNull 只复制有效值(拷贝数值前判断是否有效，无效值不拷贝)
     */
    public FlowRefectionMapper(String targetKey, String[] sourceKeys, boolean filterNull) {
        this.targetKey = targetKey;
        this.sourceKeys = sourceKeys;
        this.filterNull = filterNull;
    }

    public String getTargetKey() {
        return targetKey;
    }

    public void setTargetKey(String targetKey) {
        this.targetKey = targetKey;
    }

    public String[] getSourceKeys() {
        return sourceKeys;
    }

    public void setSourceKeys(String[] sourceKeys) {
        this.sourceKeys = sourceKeys;
    }

    public boolean isFilterNull() {
        return filterNull;
    }

    public void setFilterNull(boolean filterNull) {
        this.filterNull = filterNull;
    }

    public static void addIn(List<FlowRefectionMapper> mappers, String targetKey, String... sourceKeys) {
        addIn(mappers, true, targetKey, sourceKeys);
    }

    public static void addIn(List<FlowRefectionMapper> mappers, boolean filterNull, String targetKey, String... sourceKeys) {
        if (mappers == null || DataUtils.isInvalid(targetKey) || DataUtils.isInvalid(sourceKeys)) {
            return;
        }

        mappers.add(new FlowRefectionMapper(targetKey, sourceKeys, filterNull));
    }
}
