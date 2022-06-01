package com.security.info.manage.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
public class LawCatalogReqDTO {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "目录编号")
    private String no;

    @ApiModelProperty(value = "目录名称")
    private String name;

    @ApiModelProperty(value = "目录英文名称")
    private String engName;

    @ApiModelProperty(value = "目录编码")
    private String code;

    @ApiModelProperty(value = "上级目录ID")
    private String parentId;

    @ApiModelProperty(value = "目录路径 例如 1,101")
    private String route;

    @ApiModelProperty(value = "序号")
    private Integer sort;

    @ApiModelProperty(value = "状态 0 启用 1 停用")
    private Integer status;

    @ApiModelProperty(value = "是否为公共目录 0 否 1 是")
    private Integer isPublic;

    @ApiModelProperty(value = "管理部门id")
    private String deptId;

    @ApiModelProperty(value = "创建人id")
    private String userId;


}
