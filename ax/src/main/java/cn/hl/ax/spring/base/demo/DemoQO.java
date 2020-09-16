/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2019 All Rights Reserved.
 */
package cn.hl.ax.spring.base.demo;

import cn.hl.ax.spring.base.select.SelectKeywordQO;
import cn.hl.ax.spring.base.select.SelectOrderByQO;
import cn.hl.ax.spring.base.select.SelectPageQO;
import cn.hl.ax.spring.base.bean.QO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * 示例搜索模型
 * @author hyman
 * @date 2019-11-30 02:05:36
 */
@Data
public class DemoQO implements QO, SelectKeywordQO, SelectPageQO, SelectOrderByQO {
    //-- Customized attribute -------------------------------------------------

    @ApiModelProperty("查询关键词")
    private String myKeyword;

    //-- OverrideBy attribute -------------------------------------------------
    // 通过定义同名属性并由lombok自动生成setter方法的方式进行覆写

    @ApiModelProperty("分页页码")
    private int pageNum = 1;

    @ApiModelProperty("分页大小")
    private int pageSize = 20;

    @ApiModelProperty("查询字段")
    private List<String> fields = Arrays.asList("name", "description");

    //-- OverrideBy function --------------------------------------------------
    // 直接覆写接口中定义的默认方法

    @Override
    public String getKeyword() {
        return myKeyword;
    }

}
