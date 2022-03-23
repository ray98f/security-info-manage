package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
public class ApplianceTypeTreeResDTO {

    @ApiModelProperty(value = "劳保用品类别id")
    private String id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "分类编码")
    private String code;

    @ApiModelProperty(value = "上级id")
    private String parentId;

    @ApiModelProperty(value = "上级编码")
    private String parentCode;

    @ApiModelProperty(value = "状态 0 启用 1 停用")
    private String status;

    @ApiModelProperty(value = "子集")
    private List<ApplianceTypeTreeResDTO> children;
}
