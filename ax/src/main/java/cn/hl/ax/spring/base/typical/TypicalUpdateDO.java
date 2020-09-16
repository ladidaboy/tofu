/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ax.spring.base.typical;

import cn.hutool.core.date.DateUtil;

/**
 * 默认拥有更新标记对象
 * @author hyman
 * @date 2019-12-01 02:04:42
 */
public interface TypicalUpdateDO extends TypicalBaseDO {
    /**
     * 更新时设置默认数据
     * @param dbo DO数据对象
     */
    default void prepare4update(Object dbo) {
        prepare4column(dbo, UPDATE_BY, getCurrentUserId());
        prepare4column(dbo, UPDATE_TIME, DateUtil.date());
    }
}
