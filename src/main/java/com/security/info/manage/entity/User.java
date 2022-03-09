package com.security.info.manage.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author frp
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class User extends BaseEntity {

    @ApiModelProperty(value = "用户ID")
    private String id;

    @ApiModelProperty(value = "用户真实姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "座机")
    private String telephone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "企业邮箱")
    private String bizMail;

    @ApiModelProperty(value = "激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业。")
    private Integer status;

    @ApiModelProperty(value = "成员开放id 全局唯一")
    private String openUserid;

    @ApiModelProperty(value = "主部门")
    private String mainDept;

    @ApiModelProperty(value = "头像url")
    private String avatar;

    @ApiModelProperty(value = "头像缩略图url")
    private String thumbAvatar;

    @ApiModelProperty(value = "员工个人二维码")
    private String qrCode;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "性别。0表示未定义，1表示男性，2表示女性")
    private String gender;
}
