package com.security.info.manage.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author frp
 */
@Data
public class TrainReqDTO {

    @ApiModelProperty(value = "培训id")
    private String id;

    @ApiModelProperty(value = "包含人员id")
    private String userIds;

    @ApiModelProperty(value = "培训内容")
    private String content;

    @ApiModelProperty(value = "培训时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date trainTime;

    @ApiModelProperty(value = "培训人")
    private String lecturer;

    @ApiModelProperty(value = "图片")
    private String pic;

    @ApiModelProperty(value = "地点")
    private String address;

    @ApiModelProperty(value = "培训课件")
    private String courseware;

    @ApiModelProperty(value = "用户id")
    private String userId;
}
