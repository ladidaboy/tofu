/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2020 All Rights Reserved.
 */
package cn.hl.ax.spring.base.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hyman
 * @date 2020-01-08 10:45:01
 * @version $ Id: SimpleInfo.java, v 0.1  hyman Exp $
 */
@Data
public class SimpleInfo implements Serializable {
    /**
     * 数值编号
     */
    @ApiModelProperty(value = "数值编号", example = "1")
    private Integer id;
    /**
     * 字符编码
     */
    @ApiModelProperty("字符编码")
    private String  code;
    /**
     * 信息内容
     */
    @ApiModelProperty("信息内容")
    private String  name;
    /**
     * 信息描述
     */
    @ApiModelProperty("信息描述")
    private String  desc;
}
