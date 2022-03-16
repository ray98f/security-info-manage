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
public class PhysicalFeedback extends BaseEntity {

    @ApiModelProperty(value = "体检反馈id")
    private String id;

    @ApiModelProperty(value = "体检流程id")
    private String physicalId;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "反馈信息")
    private String feedbackInfo;

    @ApiModelProperty(value = "审批意见")
    private String applyOpinions;

    @ApiModelProperty(value = "审批时间")
    private String applyTime;

    @ApiModelProperty(value = "状态")
    private Integer status;

}
