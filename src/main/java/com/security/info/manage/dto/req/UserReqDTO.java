package com.security.info.manage.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class UserReqDTO {

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "姓名")
    private String userRealName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "用户角色id")
    private List<Long> roleIds;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "成员开放id 全局唯一")
    private String openUserid;

    @ApiModelProperty(value = "离职时间")
    private Date leaveTime;

    @ApiModelProperty(value = "新增时间")
    private Date createDate;

    @ApiModelProperty(value = "用户工号")
    private String userNo;

    @ApiModelProperty(value = "年龄")
    private Integer age;
}
