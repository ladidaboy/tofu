package cn.hl.ax.spring.base.select;

import cn.hl.ax.data.DataUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 排序对象
 *
 * @author hyman
 * @date 2019-11-29 14:11:29
 */
@Data
public class SortBy {
    private static final int    basePosition = 100;
    public static final  String ASC          = "asc";
    public static final  String DESC         = "desc";

    /**
     * 字段名称
     */
    @ApiModelProperty(value = "字段名称", position = basePosition + 1)
    private String  field;
    /**
     * 排序方式: 1.升序; 其他.降序;
     */
    @ApiModelProperty(value = "排序方式: 1.升序; 其他.降序;", example = "1", position = basePosition + 2)
    private Integer asc;

    public String toOrderChar() {
        return String.join(" ", DataUtils.humpToLine(field), asc == 1 ? ASC : DESC);
    }
}
