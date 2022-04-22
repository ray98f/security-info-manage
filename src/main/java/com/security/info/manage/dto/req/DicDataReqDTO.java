package com.security.info.manage.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class DicDataReqDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "字典键值")
    @NotNull(message = "32000006")
    private String itemKey;

    @ApiModelProperty(value = "字典标签")
    @NotNull(message = "32000006")
    private String itemValue;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "字典类型")
    private String type;

    @ApiModelProperty(value = "字典排序")
    @NotNull(message = "32000006")
    private Integer sort;

    @ApiModelProperty(value = "状态：0-禁用，1-启用")
    private Integer isEnable;

    @ApiModelProperty(value = "创建人id")
    private String createBy;
}
