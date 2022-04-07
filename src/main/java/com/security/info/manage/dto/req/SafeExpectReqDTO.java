package com.security.info.manage.dto.req;

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
public class SafeExpectReqDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "主持人")
    private String userId;

    @ApiModelProperty(value = "会议地点")
    private String address;

    @ApiModelProperty(value = "预想会时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date time;

    @ApiModelProperty(value = "作业id")
    private String workId;

    @ApiModelProperty(value = "作业编号")
    private String workNo;

    @ApiModelProperty(value = "作业名称")
    private String workName;

    @ApiModelProperty(value = "作业区域")
    private String workRegion;

    @ApiModelProperty(value = "参会人员id")
    private List<String> userIds;

    @ApiModelProperty(value = "创建人")
    private String createBy;
}
