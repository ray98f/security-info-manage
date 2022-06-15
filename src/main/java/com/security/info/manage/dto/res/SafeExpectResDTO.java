package com.security.info.manage.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.security.info.manage.entity.SafeExpectUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author frp
 */
@Data
public class SafeExpectResDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "主持人")
    private String userId;

    @ApiModelProperty(value = "主持人")
    private String userName;

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

    @ApiModelProperty(value = "状态 0 未开始 1 已开始 2 已收工")
    private Integer status;

    @ApiModelProperty(value = "参与人员姓名")
    private String userNames;

    @ApiModelProperty(value = "当前用户是否签到")
    private Integer isSign;

    @ApiModelProperty(value = "参与人员详情")
    private List<SafeExpectUser> userInfo;

    @ApiModelProperty(value = "安全预想会详情")
    private SafeExpectInfoResDTO safeExpectInfo;

    @ApiModelProperty(value = "收工会详情")
    private SafeExpectCollectionUnionResDTO safeExpectCollectionUnion;
}
