package com.security.info.manage.dto.req;

import com.security.info.manage.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

    @ApiModelProperty(value = "体检类型")
    private Integer type;

    @ApiModelProperty(value = "体检起始时间")
    private Date startTime;

    @ApiModelProperty(value = "体检结束时间")
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
