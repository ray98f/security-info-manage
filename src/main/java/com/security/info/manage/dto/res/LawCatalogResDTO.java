package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
public class LawCatalogResDTO {

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

    @ApiModelProperty(value = "目录路径")
    private String routeName;

    @ApiModelProperty(value = "序号")
    private Integer sort;

    @ApiModelProperty(value = "状态 0 启用 1 停用")
    private Integer status;

    @ApiModelProperty(value = "管理部门id")
    private String deptId;

    @ApiModelProperty(value = "管理部门名称")
    private String deptName;

    @ApiModelProperty(value = "目录子集")
    private List<LawCatalogResDTO> children;
}
