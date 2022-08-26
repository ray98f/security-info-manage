package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class TroubleshootResDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "类型id")
    private String typeId;

    @ApiModelProperty(value = "类型名称")
    private String typeName;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "pdf文件路径")
    private String pdf;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
