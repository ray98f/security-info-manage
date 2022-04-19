package com.security.info.manage.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
public class UserRoleReqDTO {

    @ApiModelProperty(value = "角色id")
    private String roleId;

    @ApiModelProperty(value = "用户ids")
    private List<String> userIds;
}
