package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
public class PhysicalUserCountResDTO {

    @ApiModelProperty(value = "禁忌证个数")
    private Integer tabooNum;

    @ApiModelProperty(value = "复查个数")
    private Integer reviewNum;

    @ApiModelProperty(value = "正常个数")
    private Integer normalNum;

}
