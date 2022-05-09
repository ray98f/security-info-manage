package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class Ztt4GTalkResDTO {

    private String IP;

    private String Port;

    private String Token;
}
