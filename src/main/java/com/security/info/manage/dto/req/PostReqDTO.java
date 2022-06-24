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

    @ApiModelProperty(value = "岗位id")
    private String postId;

    @ApiModelProperty(value = "岗位名称")
    private String postName;

    @ApiModelProperty(value = "文件s")
    private String files;

    @ApiModelProperty(value = "状态 0 正常 1 停用")
    private Integer status;

    @ApiModelProperty(value = "审批权限 0(默认，无审批权限) 1(一级审批权限，科长 副科长类的岗位) 2(副部长) 3(部长)")
    private Integer auditLevel;

    @ApiModelProperty(value = "用户id")
    private String userId;
}
