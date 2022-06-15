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
public class TrainResDTO {

    @ApiModelProperty(value = "培训id")
    private String id;

    @ApiModelProperty(value = "包含人员id")
    private String userIds;

    @ApiModelProperty(value = "包含人员姓名")
    private String userNames;

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

    @ApiModelProperty(value = "图片文件")
    private List<File> picFile;

    @ApiModelProperty(value = "地点")
    private String address;

    @ApiModelProperty(value = "培训课件")
    private String courseware;

    @ApiModelProperty(value = "课件文件")
    private List<File> coursewareFile;

    @ApiModelProperty(value = "培训人")
    private String lecturer;

    @ApiModelProperty(value = "培训人")
    private String lecturerName;
}
