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
public class PostChangeListResDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date ringTime;

    @ApiModelProperty(value = "旧岗位id")
    private String oldPostId;

    @ApiModelProperty(value = "新岗位id")
    private String newPostId;

    @ApiModelProperty(value = "旧岗位名称")
    private String oldPostName;

    @ApiModelProperty(value = "新岗位名称")
    private String newPostName;

    @ApiModelProperty(value = "告警状态(0待处理;1已处理)")
    private Integer status;
}
