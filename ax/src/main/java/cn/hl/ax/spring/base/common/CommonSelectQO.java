package cn.hl.ax.spring.base.common;

import cn.hl.ax.spring.base.select.SelectKeywordQO;
import cn.hl.ax.spring.base.select.SelectOrderByQO;
import cn.hl.ax.spring.base.select.SelectPageQO;
import cn.hl.ax.spring.base.select.SortBy;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 通用查询对象(默认开启所有开关)
 *
 * @author hyman
 * @date 2019-12-05 00:01:09
 */
@Data
@ApiModel("通用搜索条件")
public class CommonSelectQO implements SelectKeywordQO, SelectOrderByQO, SelectPageQO, Serializable {
    private static final int basePosition = 100;

    //> SelectKeywordQO
    @ApiModelProperty(value = "关键词查询开关(默认:开启)", position = basePosition + 1)
    private boolean      openSearch  = true;
    @ApiModelProperty(value = "是否模糊查询(默认:模糊查询)", position = basePosition + 2)
    private boolean      fuzzyQuery  = true;
    @ApiModelProperty(value = "查询使用字段", position = basePosition + 3)
    private List<String> fields;
    @ApiModelProperty(value = "查询关键词(= queryValue)", position = basePosition + 4)
    private String       keyword;
    @ApiModelProperty(value = "自定义查询(= exactValue)", position = basePosition + 5)
    private String       condition;
    @ApiModelProperty(value = "使用`fields`指定的字段 / 数据模型指定的字段 查询数据", position = basePosition + 6)
    private String       queryValue;
    @ApiModelProperty(value = "自定义字段的多条件精确查询( field1 : value1, value2; field2 : value3 )", position = basePosition + 7)
    private String       exactValue;
    //> SelectOrderByQO
    @ApiModelProperty(value = "排序查询开关(默认:开启)", position = basePosition + 8)
    private boolean      openOrderBy = true;
    @ApiModelProperty(value = "排序列表", position = basePosition + 9)
    private List<SortBy> sortBies;
    //> SelectPageQO
    @ApiModelProperty(value = "分页查询开关(默认:开启)", position = basePosition + 10)
    private boolean      openPage    = true;
    @ApiModelProperty(value = "分页页码", example = "1", position = basePosition + 11)
    private int          pageNum;
    @ApiModelProperty(value = "分页大小", example = "10", position = basePosition + 12)
    private int          pageSize;

    /**
     * 兼容queryValue进行keyword查询
     *
     * @param queryValue ~~ keyword
     */
    public void setQueryValue(String queryValue) {
        this.queryValue = queryValue;
        setKeyword(queryValue);
    }

    /**
     * 兼容exactValue进行condition查询
     *
     * @param exactValue ~~ condition
     */
    public void setExactValue(String exactValue) {
        this.exactValue = exactValue;
        setCondition(exactValue);
    }
}
