package cn.hl.ax.spring.base.common;

import cn.hl.ax.spring.base.select.SortBy;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author hyman
 * @date 2019-12-10 17:20:23
 */
@Data
@ApiModel("通用搜索条件")
public class CommonQueryRO implements Serializable {
    private static final int basePosition = 100;

    /**
     * SelectKeywordQO
     */
    @ApiModelProperty(value = "是否模糊查询(默认:模糊查询)", position = basePosition + 1)
    private boolean      fuzzyQuery = true;
    @ApiModelProperty(value = "查询关键词(按照系统设定的字段进行匹配查询)", position = basePosition + 2)
    private String       queryValue;
    @ApiModelProperty(value = "自定义字段的多条件精确查询( field1 : value1, value2; field2 : value3 )", position = basePosition + 3)
    private String       exactValue;
    /**
     * SelectOrderByQO
     */
    @ApiModelProperty(value = "排序列表", position = basePosition + 4)
    private List<SortBy> sortBies;
    /**
     * SelectPageQO
     */
    @ApiModelProperty(value = "分页页码", example = "1", position = basePosition + 5)
    private int          pageNum;
    @ApiModelProperty(value = "分页大小", example = "10", position = basePosition + 6)
    private int          pageSize;
}
