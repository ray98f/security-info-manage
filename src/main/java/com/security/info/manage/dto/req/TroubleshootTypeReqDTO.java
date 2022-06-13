package com.security.info.manage.dto.req;

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
public class TroubleshootTypeReqDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "编号")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "状态 0 正常 1 停用")
    private Integer status;

    @ApiModelProperty(value = "创建人id")
    private String createBy;
}
