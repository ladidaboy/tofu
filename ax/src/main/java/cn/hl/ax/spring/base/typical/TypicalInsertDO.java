/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ax.spring.base.typical;

import cn.hutool.core.date.DateUtil;

/**
 * 默认拥有插入标记对象
 * @author hyman
 * @date 2019-12-01 02:02:20
 */
public interface TypicalInsertDO extends TypicalBaseDO {
    /**
     * 插入时设置默认数据
     * @param dbo DO数据对象
     */
    default void prepare4insert(Object dbo) {
        prepare4column(dbo, CREATE_BY, getCurrentUserId());
        prepare4column(dbo, CREATE_TIME, DateUtil.date());
    }
}
