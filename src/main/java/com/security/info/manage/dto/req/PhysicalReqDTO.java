package com.security.info.manage.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.security.info.manage.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author frp
 */
@Data
public class PhysicalReqDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "流水号")
    private String no;

    @ApiModelProperty(value = "医院流水号")
    private String hospitalNo;

    @ApiModelProperty(value = "体检类型 1 岗中体检 2 新人体检 3 普通体检 4 离岗体检")
    private Integer type;

    @ApiModelProperty(value = "体检起始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date startTime;

    @ApiModelProperty(value = "体检结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date endTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "文件word")
    private String fileWord;

    @ApiModelProperty(value = "文件pdf")
    private String filePdf;

    @ApiModelProperty(value = "应检人数")
    private Integer estimateNum;

    @ApiModelProperty(value = "受检人数")
    private Integer actualNum;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "选择人员")
    private List<User> users;

}
