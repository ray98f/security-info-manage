package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class Ztt4GVideoResDTO {

    @ApiModelProperty(value = "资源类型")
    private String Type;

    @ApiModelProperty(value = "资源索引")
    private String Idx;

    @ApiModelProperty(value = "资源名称")
    private String Name;

    private String Description;

    private String Enable;

    private String Usable;

    private String Remark;
}
