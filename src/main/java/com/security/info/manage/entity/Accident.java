package com.security.info.manage.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class Accident {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "事故名称")
    private String name;

    @ApiModelProperty(value = "编号")
    private String code;

}
