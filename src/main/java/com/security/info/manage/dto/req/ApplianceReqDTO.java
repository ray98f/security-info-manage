package com.security.info.manage.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
public class ApplianceReqDTO {

    @ApiModelProperty(value = "劳保用品类别id")
    private String id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "分类编码")
    private String code;

    @ApiModelProperty(value = "类型 1 公共用品 2 个人用品")
    private Integer type;

    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    @ApiModelProperty(value = "合格证")
    private String certificate;

    @ApiModelProperty(value = "计数单位")
    private String unit;

    @ApiModelProperty(value = "人员id")
    private String userId;
}
