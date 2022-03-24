package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class PostHazardFactorResDTO {

    @ApiModelProperty(value = "岗位id")
    private String postId;

    @ApiModelProperty(value = "岗位名称")
    private String postName;

    @ApiModelProperty(value = "危害因素id集合")
    private String hazardFactorIds;

    @ApiModelProperty(value = "危害因素名称集合")
    private String hazardFactorNames;

}
