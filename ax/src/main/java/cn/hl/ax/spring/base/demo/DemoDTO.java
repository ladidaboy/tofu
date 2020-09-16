/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ax.spring.base.demo;

import cn.hl.ax.spring.base.bean.DTO;
import lombok.Data;

/**
 * @author hyman
 * @date 2019-12-30 13:12:33
 */
@Data
public class DemoDTO implements DTO {
    /**
     * 编号
     */
    private Integer id;
    /**
     * 姓名
     */
    private String  name;
    /**
     * 收入
     */
    private Double  income;
    /**
     * 结余
     */
    private Double  balance;
}
