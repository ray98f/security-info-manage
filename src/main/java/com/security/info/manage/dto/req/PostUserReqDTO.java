package com.security.info.manage.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
public class PostUserReqDTO {

    @ApiModelProperty(value = "岗位id")
    private String postId;

    @ApiModelProperty(value = "用户id")
    private List<String> userIds;
}
