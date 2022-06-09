package com.security.info.manage.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
public class SafeExpectModifyReqDTO {

    @ApiModelProperty(value = "预想会")
    private SafeExpectReqDTO safeExpectReqDTO;
    
    @ApiModelProperty(value = "预想会详情")
    private SafeExpectInfoReqDTO safeExpectInfo;

    @ApiModelProperty(value = "收工会详情")
    private SafeExpectCollectionUnionReqDTO safeExpectCollectionUnion;

}
