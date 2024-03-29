package cn.hl.ax.spring.base.typical;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReflectUtil;

/**
 * @author sky
 * @date 2020-02-24 14:26:00
 */
public interface TypicalUidDO extends TypicalBaseDO {
    /**
     * 更新设置默认数据
     *
     * @param dbo DO数据对象
     */
    default void prepare4InitUid(Object dbo) {
        if (dbo != null) {
            Object uidValue = ReflectUtil.getFieldValue(dbo, UID);
            if (uidValue == null) {
                prepare4column(dbo, UID, IdUtil.simpleUUID());
            }
        }
    }
}
