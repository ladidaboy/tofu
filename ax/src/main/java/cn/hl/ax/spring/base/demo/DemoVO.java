package cn.hl.ax.spring.base.demo;

import cn.hl.ax.spring.base.bean.CopyConverter;
import cn.hl.ax.spring.base.bean.VO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hyman
 * @date 2019-12-30 13:22:41
 * @version $ Id: DemoVO.java, v 0.1  hyman Exp $
 */
@Data
public class DemoVO implements VO, CopyConverter<DemoBO> {
    @ApiModelProperty("编号")
    private Integer id;
    @ApiModelProperty("姓名")
    private String  name;
    @ApiModelProperty("收入")
    private Double  income;
    @ApiModelProperty("结余")
    private Double  balance;
    @ApiModelProperty("是否盈利")
    private Boolean isProfit;

    /*public static Function<DemoBO, DemoVO> copier = bo -> {
        if (bo == null) {
            return null;
        }

        DemoVO vo = new DemoVO();
        vo.from(bo);
        return vo;
    };*/

}
