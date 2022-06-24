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
public class OperationLogResDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "类型 1 pc 2 小程序")
    private Integer type;

    @ApiModelProperty(value = "操作员名称")
    private String userName;

    @ApiModelProperty(value = "操作时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date operationTime;

    @ApiModelProperty(value = "操作类型 1 新增 2 删除 3 修改 4 查询")
    private String operationType;

    @ApiModelProperty(value = "用时")
    private Double useTime;

    @ApiModelProperty(value = "参数")
    private String params;

    @ApiModelProperty(value = "主机IP")
    private String hostIp;

    @ApiModelProperty(value = "新增时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;
}
