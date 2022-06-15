package com.security.info.manage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VxAppAccessToken {

    @ApiModelProperty(value = "获取到的凭证")
    private String token;

    @ApiModelProperty(value = "凭证有效时间，单位：秒")
    private int expiresIn;
}
