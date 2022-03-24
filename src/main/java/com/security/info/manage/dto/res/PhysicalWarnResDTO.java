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
public class PhysicalWarnResDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "体检流程id")
    private String physicalId;

    @ApiModelProperty(value = "体检人员id")
    private String physicalUserId;

    @ApiModelProperty(value = "员工id")
    private String userId;

    @ApiModelProperty(value = "工号")
    private String userNo;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "岗位")
    private String postName;

    @ApiModelProperty(value = "预警信息")
    private String info;

    @ApiModelProperty(value = "体检类型")
    private Integer physicalType;

    @ApiModelProperty(value = "超期天数")
    private Integer day;

    @ApiModelProperty(value = "状态 0 未处理 1 已处理")
    private Integer status;

    @ApiModelProperty(value = "预警时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date warningTime;
}
