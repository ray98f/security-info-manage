package com.security.info.manage.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.security.info.manage.entity.File;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author frp
 */
@Data
public class DangerRectifyResDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "隐患id")
    private String dangerId;

    @ApiModelProperty(value = "整改责任人")
    private String rectifyUserId;

    @ApiModelProperty(value = "整改责任人名")
    private String rectifyUserName;

    @ApiModelProperty(value = "整改措施")
    private String rectifyMeasure;

    @ApiModelProperty(value = "整改后图片")
    private String afterPic;

    @ApiModelProperty(value = "整改后图片文件")
    private List<File> afterPicFile;

    @ApiModelProperty(value = "审核人")
    private String examineUserId;

    @ApiModelProperty(value = "审核人名")
    private String examineUserName;

    @ApiModelProperty(value = "审核时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date examineTime;

    @ApiModelProperty(value = "是否消项 0 否 1 是")
    private Integer isEliminate;

    @ApiModelProperty(value = "审核状态 0 未审核 1 审核通过 2 审核不通过")
    private Integer status;

}
