package com.security.info.manage.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
public class LawCatalogUserRoleReqDTO {

    @ApiModelProperty(value = "法律法规目录id")
    private List<String> lawCatalogIds;

    @ApiModelProperty(value = "用户id")
    private List<String> userIds;

    @ApiModelProperty(value = "权限字段（多选） 1 访问 2 上传 3 下载 4 查看")
    private String role;

}
