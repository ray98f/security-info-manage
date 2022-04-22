package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@ApiModel
public class DicResDTO {

    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "状态：0-停用，1-启用")
    private Integer isEnable;

    @ApiModelProperty(value = "备注")
    private String description;
}
