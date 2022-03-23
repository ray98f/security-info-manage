package com.security.info.manage.dto.req;

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
public class ApplianceConfigReqDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "领取人id")
    private String userId;

    @ApiModelProperty(value = "领取人工号")
    private String userNo;

    @ApiModelProperty(value = "领取人姓名")
    private String userName;

    @ApiModelProperty(value = "用品名称")
    private String applianceName;

    @ApiModelProperty(value = "用品编号")
    private String applianceCode;

    @ApiModelProperty(value = "用品类别")
    private String applianceType;

    @ApiModelProperty(value = "领取数量")
    private Integer num;

    @ApiModelProperty(value = "领取时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date receivingTime;

    @ApiModelProperty(value = "有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date effectiveTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private String createBy;
}
