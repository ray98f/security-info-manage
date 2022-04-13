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
public class DangerExamineResDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "隐患id")
    private String dangerId;

    @ApiModelProperty(value = "审核人类型 1 科长 2 副部长 3 部长")
    private Integer userType;

    @ApiModelProperty(value = "指定责任人")
    private String examineUserId;

    @ApiModelProperty(value = "指定责任人名")
    private String examineUserName;

    @ApiModelProperty(value = "审核时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date examineTime;

    @ApiModelProperty(value = "整改意见")
    private String opinion;

    @ApiModelProperty(value = "审核状态 0 未审核 1 审核通过 2 审核不通过")
    private Integer status;

}
