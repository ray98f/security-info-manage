package com.security.info.manage.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class NewUserReqDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "性别 0 未知 1 男 2 女")
    private Integer sex;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "创建人")
    private String createBy;
}
