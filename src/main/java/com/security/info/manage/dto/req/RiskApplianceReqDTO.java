package com.security.info.manage.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
public class RiskApplianceReqDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "专业名称")
    private String name;

    @ApiModelProperty(value = "状态 0 启用 1 停用")
    private Integer status;

    @ApiModelProperty(value = "用户id")
    private String userId;
}
