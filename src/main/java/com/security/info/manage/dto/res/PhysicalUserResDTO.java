package com.security.info.manage.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author frp
 */
@Data
@ApiModel
public class PhysicalUserResDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "体检流程id")
    private String physicalId;

    @ApiModelProperty(value = "所属体检批次号")
    private String physicalNo;

    @ApiModelProperty(value = "成员UserID")
    private String userId;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "成员手机号")
    private String mobile;

    @ApiModelProperty(value = "新入职员工id")
    private String newUserId;

    @ApiModelProperty(value = "是否属于复查 0 否 1 是")
    private Integer isReview;

    @ApiModelProperty(value = "上一次的体检流水号")
    private String lastPhysicalUserId;

    @ApiModelProperty(value = "流程状态(0发起中、1已体检反馈、2已体检反馈已审核、3体检结果已录入、4体检结果已确认、5已结束)")
    private Integer status;

    @ApiModelProperty(value = "体检结果状态( 1职业禁忌证、2复查、3正常)")
    private Integer result;

    @ApiModelProperty(value = "体检时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date physicalTime;

    @ApiModelProperty(value = "文件word")
    private String fileWord;

    @ApiModelProperty(value = "文件pdf")
    private String filePdf;

    @ApiModelProperty(value = "新增时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;

    @ApiModelProperty(value = "新增人")
    private String createBy;
}