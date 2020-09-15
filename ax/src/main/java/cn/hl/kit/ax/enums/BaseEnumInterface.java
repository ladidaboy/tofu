/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2020 All Rights Reserved.
 */
package cn.hl.kit.ax.enums;

/**
 * @author hyman
 * @date 2020-04-10 17:46:28
 */
public interface BaseEnumInterface {
    /**
     * 获取 标记值
     * @return 标记值
     */
    int getValue();

    /**
     * 是否是 标记值
     * @param val 标记值
     * @return true.是当前枚举值; false.不是当前枚举值
     */
    default boolean eq(Integer val) {
        return val != null && getValue() == val;
    }

    /**
     * 是否是 标记值
     * @param val 标记值
     * @return true.是当前枚举值; false.不是当前枚举值
     */
    default boolean eq(String val) {
        try {
            return getValue() == Integer.parseInt(val);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 是否有一个一致
     * @param es enum数组
     * @return true: 发现; false: 未发现
     */
    default boolean findIn(BaseEnumInterface... es) {
        if (es == null || es.length == 0) {
            return false;
        }
        for (BaseEnumInterface e : es) {
            if (this == e) {
                return true;
            }
        }
        return false;
    }
}
