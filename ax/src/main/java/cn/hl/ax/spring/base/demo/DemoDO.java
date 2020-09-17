package cn.hl.ax.spring.base.demo;

import cn.hl.ax.spring.base.bean.DO;
import cn.hl.ax.spring.base.common.CommonTypicalDO;
import cn.hl.ax.spring.base.select.Keyword;
import cn.hl.ax.spring.base.typical.TypicalBaseDO;
import lombok.Data;

import java.util.Date;

/**
 * @author hyman
 * @date 2019-12-05 13:31:03
 */
@Data
public class DemoDO extends CommonTypicalDO implements DO, TypicalBaseDO {
    /**
     * 编号
     */
    @Keyword
    private Integer id;
    /**
     * 姓名
     */
    @Keyword
    private String  name;
    /**
     * 收入
     */
    private Double  income;
    /**
     * 性别
     */
    private Boolean male;
    /**
     * 生日
     */
    @Keyword
    private Date    birthday;
}
