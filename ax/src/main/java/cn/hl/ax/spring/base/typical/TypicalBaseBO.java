package cn.hl.ax.spring.base.typical;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hyman
 * @date 2019-12-20 14:03:34
 */
@Data
public abstract class TypicalBaseBO implements Serializable {
    @ApiModelProperty(value = "[只读]创建者信息", position = 101, readOnly = true, accessMode = AccessMode.READ_ONLY)
    @JsonProperty(access = Access.READ_ONLY)
    @JSONField(deserialize = false)
    private String createBy;
    @ApiModelProperty(value = "[只读]创建时间", position = 102, readOnly = true, accessMode = AccessMode.READ_ONLY)
    @JsonProperty(access = Access.READ_ONLY)
    @JSONField(deserialize = false)
    private Date   createTime;

    @ApiModelProperty(value = "[只读]更新者信息", position = 103, readOnly = true, accessMode = AccessMode.READ_ONLY)
    @JsonProperty(access = Access.READ_ONLY)
    @JSONField(deserialize = false)
    private String updateBy;
    @ApiModelProperty(value = "[只读]更新时间", position = 104, readOnly = true, accessMode = AccessMode.READ_ONLY)
    @JsonProperty(access = Access.READ_ONLY)
    @JSONField(deserialize = false)
    private Date   updateTime;

    @ApiModelProperty(value = "[只读]删除者信息", position = 105, hidden = true)
    @JsonProperty(access = Access.READ_ONLY)
    @JSONField(deserialize = false)
    private String  deleteBy;
    @ApiModelProperty(value = "[只读]删除时间", position = 106, hidden = true)
    @JsonProperty(access = Access.READ_ONLY)
    @JSONField(deserialize = false)
    private Date    deleteTime;
    @ApiModelProperty(value = "[只读]逻辑删除", position = 107, hidden = true)
    @JsonProperty(access = Access.READ_ONLY)
    @JSONField(deserialize = false)
    private Boolean del;
}
