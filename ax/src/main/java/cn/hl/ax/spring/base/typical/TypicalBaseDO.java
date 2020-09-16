/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ax.spring.base.typical;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.UUID;

//import com.zenlayer.sso.util.SubjectUtil;

/**
 * 典型业务对象
 * <ol>
 * <li>拥有默认操作行为的业务模型</li>
 * <li>可覆写相关方法以达到定制化操作数据</li>
 * </ol>
 * @author hyman
 * @date 2019-12-01 01:49:25
 */
public interface TypicalBaseDO {
    String ID  = "id";
    String UID = "uid";

    String CREATE_BY   = "createBy";
    String CREATE_TIME = "createTime";

    String UPDATE_BY   = "updateBy";
    String UPDATE_TIME = "updateTime";

    String DELETE_BY   = "deleteBy";
    String DELETE_TIME = "deleteTime";
    String DELETE_FLAG = "del";

    /**
     * 设置DO对象的属性值<br>
     * 此方法提供公共操作，覆写时考虑好影响范围
     * @param dbo DO对象实例
     * @param field 属性名称
     * @param value 属性数值
     */
    default void prepare4column(Object dbo, String field, Object value) {
        if (dbo == null) {
            return;
        }

        Class clz = dbo.getClass();
        if (!ReflectUtil.hasField(clz, field)) {
            throw new RuntimeException(StrUtil.format("{} has no field : {}", clz.getSimpleName(), field));
        }

        ReflectUtil.setFieldValue(dbo, field, value);
    }

    /**
     * 获取当前用户编号
     * @return
     */
    default String getCurrentUserId() {
        try {
            //return SubjectUtil.getContextSubjectUserId();
            return UUID.randomUUID().toString();
        } catch (Exception ex) {
            return null;
        }
    }
}
