package cn.hl.ax.spring.base.demo;

import cn.hl.ax.spring.base.bean.RO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hyman
 * @date 2019-12-30 13:21:17
 */
@Data
public class DemoRO implements RO {
    @ApiModelProperty("编号")
    private Integer id;
    @ApiModelProperty("姓名")
    private String  name;
    @ApiModelProperty("收入")
    private Double  income;
    @ApiModelProperty("负债")
    private Double  debt;
    @ApiModelProperty("性别")
    private Boolean male;
    @ApiModelProperty("生日")
    private Date    birthday;
}
