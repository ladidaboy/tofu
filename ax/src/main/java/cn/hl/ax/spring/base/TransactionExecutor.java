/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2020 All Rights Reserved.
 */
package cn.hl.ax.spring.base;

/**
 * 事务执行接口
 * @author hyman
 * @date 2020-06-04 22:47:58
 * @version $ Id: TransactionExecutor.java, v 0.1  hyman Exp $
 */
public interface TransactionExecutor {
    /**
     * 执行业务
     */
    void execute();
}
