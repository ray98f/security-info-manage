package com.security.info.manage.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author frp
 */
@Data
public class TrainResDTO {

    @ApiModelProperty(value = "培训id")
    private String id;

    @ApiModelProperty(value = "包含部门id")
    private String orgIds;

    @ApiModelProperty(value = "包含部门id")
    private String orgNames;

    @ApiModelProperty(value = "培训内容")
    private String content;

    @ApiModelProperty(value = "培训时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date trainTime;

    @ApiModelProperty(value = "图片")
    private String pic;

    @ApiModelProperty(value = "地点")
    private String address;

    @ApiModelProperty(value = "培训人")
    private String lecturer;
}
