/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.kit.spring.base;

/**
 * <ul>
 * <li>D - Database Object</li>
 * <li>M - Database Mapper</li>
 * </ul>
 * 通用Dao层批量操作执行接口
 * @author hyman
 * @date 2019-12-23 17:30:24
 */
public interface DaoBatchExecutor<D, M> {
    /**
     * 执行业务
     * @param dbo Database Object
     * @param mpr MyBatis Mapper
     */
    void execute(D dbo, M mpr);
}
