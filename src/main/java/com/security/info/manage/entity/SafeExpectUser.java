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
@Data
@ApiModel
public class SafeExpectUser {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "参会人员id")
    private String userId;

    @ApiModelProperty(value = "参会人员姓名")
    private String userName;

    @ApiModelProperty(value = "安全预想会id")
    private String safeExpectId;

    @ApiModelProperty(value = "签到状态 0 已确认 1 未签到 2 未确认")
    private Integer isSign;

    @ApiModelProperty(value = "是否缺席 0 否 1 是")
    private Integer isAbsent;

    @ApiModelProperty(value = "签到时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date signTime;

}
