package cn.hl.ax.spring.base.demo;

import cn.hl.ax.spring.base.bean.BO;
import cn.hl.ax.spring.base.bean.BizConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hyman
 * @date 2019-12-05 13:31:03
 */
@Data
public class DemoBO implements BO, BizConverter<DemoDO> {
    @ApiModelProperty("编号")
    private Integer id;
    @ApiModelProperty("姓名")
    private String  name;
    @ApiModelProperty("收入")
    private Double  income;
    @ApiModelProperty("性别")
    private Boolean male;
    @ApiModelProperty("生日")
    private Date    birthday;

    @ApiModelProperty("结余")
    private Double balance;

    @Override
    public void assemble(DemoDO dbo) {
        if (dbo == null) {
            return;
        }

        this.setId(dbo.getId());
        this.setName(dbo.getName());
        this.setIncome(dbo.getIncome());
        this.setMale(dbo.getMale());
        this.setBirthday(dbo.getBirthday());
        this.setBalance(Math.PI);
    }

    @Override
    public DemoDO compose() {
        Double income = this.income;
        if (income == null) {
            income = this.balance;
        } else {
            income += (this.balance == null ? 0 : this.balance);
        }

        DemoDO dbo = new DemoDO();
        dbo.setId(this.id);
        dbo.setName(this.name);
        dbo.setIncome(income);
        dbo.setMale(this.male);
        dbo.setBirthday(this.birthday);

        return dbo;
    }

    /*public static Function<DemoDO, DemoBO> assembler = dbo -> {
        if (dbo == null) {
            return null;
        }

        DemoBO biz = new DemoBO();
        biz.assemble(dbo);
        return biz;
    };*/
}
