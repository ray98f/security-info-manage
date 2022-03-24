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
public class ApplianceWarnResDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "劳保配置id")
    private String applianceUserId;

    @ApiModelProperty(value = "工号")
    private String userNo;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "用品名称")
    private String applianceName;

    @ApiModelProperty(value = "部门")
    private String deptName;

    @ApiModelProperty(value = "岗位")
    private String postName;

    @ApiModelProperty(value = "预警信息")
    private String info;

    @ApiModelProperty(value = "状态 0 未处理 1 已处理")
    private Integer status;

    @ApiModelProperty(value = "预警时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date ringTime;
}
