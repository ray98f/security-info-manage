package com.security.info.manage.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class PostHazardFactorReqDTO {

    @ApiModelProperty(value = "岗位id")
    private String postId;

    @ApiModelProperty(value = "危害因素id")
    private List<String> hazardFactorIds;

}
