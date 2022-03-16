package com.security.info.manage.dto.req;

import com.security.info.manage.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author frp
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class UserReqDTO extends User {

    @ApiModelProperty(value = "账号")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户工号")
    private String userNo;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "年龄")
    private Integer status;

    @ApiModelProperty(value = "年龄")
    private List<String> roleIds;
}
