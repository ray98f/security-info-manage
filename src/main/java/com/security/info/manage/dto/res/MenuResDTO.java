package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
public class MenuResDTO {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "上级目录ID")
    private String parentId;

    @ApiModelProperty(value = "目录路径 例如 1,101")
    private String parentIds;

    @ApiModelProperty(value = "目录路径")
    private String parentNames;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "菜单类型 1模块、2目录、3菜单、4操作")
    private Integer menuType;

    @ApiModelProperty(value = "菜单编码")
    private String menuCode;

    @ApiModelProperty(value = "排序")
    private Integer menuSort;

    @ApiModelProperty(value = "状态 0 启用 1 停用")
    private Integer status;

    @ApiModelProperty(value = "菜单组件路径")
    private String menuPath;

    @ApiModelProperty(value = "菜单图标icon")
    private String menuIcon;

    @ApiModelProperty(value = "菜单链接")
    private String menuUrl;

    @ApiModelProperty(value = "是否显示 0 否 1是")
    private Integer isShow;

    @ApiModelProperty(value = "底色")
    private String backgroundColor;

    @ApiModelProperty(value = "底部图片")
    private String backgroundPic;

    @ApiModelProperty(value = "字体颜色")
    private String titleColor;

    @ApiModelProperty(value = "子集")
    private List<MenuResDTO> children;
}
