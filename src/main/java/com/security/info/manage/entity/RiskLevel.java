package com.security.info.manage.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author frp
 */
@Data
@ApiModel
public class RiskLevel {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "等级名称")
    private String name;

    @ApiModelProperty(value = "颜色")
    private String color;

    @ApiModelProperty(value = "色谱")
    private String chromatography;

}
