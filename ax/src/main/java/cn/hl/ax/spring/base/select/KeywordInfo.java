package cn.hl.ax.spring.base.select;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author hyman
 * @date 2019-12-05 15:26:49
 */
@Data
@ApiModel("关键词搜索字段信息")
public class KeywordInfo {
    private static final int basePosition = 100;

    @ApiModelProperty(value = "所有字段", position = basePosition + 1)
    List<String> allFields;
    @ApiModelProperty(value = "模糊匹配字段", position = basePosition + 2)
    List<String> fuzzyMatches;
    @ApiModelProperty(value = "精确匹配字段", position = basePosition + 3)
    List<String> exactMatches;
}
