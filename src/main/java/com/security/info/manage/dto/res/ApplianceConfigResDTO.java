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
public class ApplianceConfigResDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "工号")
    private String userId;

    @ApiModelProperty(value = "工号")
    private String userNo;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "用品名称")
    private String applianceName;

    @ApiModelProperty(value = "用品编号")
    private String applianceCode;

    @ApiModelProperty(value = "用品类别id")
    private String applianceTypeId;

    @ApiModelProperty(value = "用品类别名称")
    private String applianceTypeName;

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

    @ApiModelProperty(value = "更换ID")
    private String lastId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态 0 未到期 1 已到期 2 已更换")
    private Integer status;
}
