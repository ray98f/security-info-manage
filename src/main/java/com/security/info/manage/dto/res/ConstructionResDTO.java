package com.security.info.manage.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author frp
 */
@Data
public class ConstructionResDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "周计划id")
    private String planId;

    @ApiModelProperty(value = "周计划名称")
    private String planName;

    @ApiModelProperty(value = "施工作业类型id")
    private String typeId;

    @ApiModelProperty(value = "施工作业类型名称")
    private String typeName;

    @ApiModelProperty(value = "作业日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    private Date date;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "作业代码")
    private String no;

    @ApiModelProperty(value = "作业部门id")
    private String orgId;

    @ApiModelProperty(value = "作业部门名称")
    private String orgName;

    @ApiModelProperty(value = "作业开始时间")
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(
            pattern = "HH:mm",
            timezone = "GMT+8"
    )
    private Date startTime;

    @ApiModelProperty(value = "作业结束时间")
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(
            pattern = "HH:mm",
            timezone = "GMT+8"
    )
    private Date endTime;

    @ApiModelProperty(value = "是否隔天 0 否 1 是")
    private Integer isDay;

    @ApiModelProperty(value = "作业名称")
    private String name;

    @ApiModelProperty(value = "作业区域")
    private String region;

    @ApiModelProperty(value = "接触网供电安排")
    private String electricArrange;

    @ApiModelProperty(value = "防护措施")
    private String protectMeasures;

    @ApiModelProperty(value = "配合部门及要求")
    private String coordinationRequirement;

    @ApiModelProperty(value = "施工负责人id")
    private String userId;

    @ApiModelProperty(value = "施工负责人姓名")
    private String userName;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态 0 启用 1 停用")
    private Integer status;
}
