package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
@ApiModel
public class DicDataResDTO {

    private String id;

    @ApiModelProperty(value = "字典键值")
    private String itemKey;

    @ApiModelProperty(value = "字典标签")
    private String itemValue;

    @ApiModelProperty(value = "字典类型")
    private String type;

    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "状态:0-禁用，1-启用")
    private Integer isEnable;
}
