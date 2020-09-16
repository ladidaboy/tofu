/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ax.spring.base.select;

import java.util.List;

/**
 * 排序查询
 * @author hyman
 * @date 2019-11-29 13:00:57
 */
public interface SelectOrderByQO extends SelectBaseQO {
    /**
     * 是否开启排序
     * @return openOrderBy : true.开启排序; false.关闭排序
     */
    default boolean isOpenOrderBy() {
        return true;
    }

    /**
     * 获取 排序对象列表
     * @return sortBies
     */
    default List<SortBy> getOrderBy() {
        return null;
    }
}
