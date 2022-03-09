package com.security.info.manage.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
public class PostReqDTO {

    @ApiModelProperty(value = "岗位id")
    private String id;

    @ApiModelProperty(value = "所属机构id")
    private String orgId;

    @ApiModelProperty(value = "岗位名称")
    private String postName;

    @ApiModelProperty(value = "状态 0 正常 1 停用")
    private Integer status;

    @ApiModelProperty(value = "用户id")
    private String userId;
}
