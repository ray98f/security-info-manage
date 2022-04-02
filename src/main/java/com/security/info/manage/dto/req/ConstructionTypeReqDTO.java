package com.security.info.manage.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
public class ConstructionTypeReqDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "上级id")
    private String parentId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "状态 0 启用 1 停用")
    private Integer status;

    @ApiModelProperty(value = "用户id")
    private String userId;
}
