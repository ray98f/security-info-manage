package com.security.info.manage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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

    @ApiModelProperty(value = "体检用户id")
    private String physicalUserId;

    @ApiModelProperty(value = "反馈信息")
    private String feedbackInfo;

    @ApiModelProperty(value = "审批意见")
    private String applyOpinions;

    @ApiModelProperty(value = "审批时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date applyTime;

    @ApiModelProperty(value = "状态 0 未审批 1 已审批 2审批不通过")
    private Integer status;

}
