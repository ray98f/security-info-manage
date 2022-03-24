package com.security.info.manage.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author frp
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class HazardFactor extends BaseEntity {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "危害因素名称")
    private String name;

    @ApiModelProperty(value = "上岗前检查项目")
    private String beforeCheckProj;

    @ApiModelProperty(value = "在岗期间检查项目")
    private String onCheckProj;

    @ApiModelProperty(value = "离岗时检查项目")
    private String afterCheckProj;

    @ApiModelProperty(value = "检查周期")
    private String checkCycle;

    @ApiModelProperty(value = "应急职业健康检查")
    private String healthCheck;

    @ApiModelProperty(value = "离岗时职业健康检查")
    private String afterCheck;

    @ApiModelProperty(value = "职业禁忌证")
    private String contraindication;

}
