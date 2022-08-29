package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class UserResDTO {

    @ApiModelProperty(value = "用户ID")
    private String id;

    @ApiModelProperty(value = "用户昵称")
    private String name;

    @ApiModelProperty(value = "用户真实姓名")
    private String realName;

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

    @ApiModelProperty(value = "账号状态 0 正常 1 停用")
    private Integer userStatus;

    @ApiModelProperty(value = "成员开放id 全局唯一")
    private String openUserid;

    @ApiModelProperty(value = "主部门")
    private String mainDept;

    @ApiModelProperty(value = "主部门名称")
    private String deptName;

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

    @ApiModelProperty(value = "账号")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户工号")
    private String userNo;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "岗位名称")
    private String postName;

    @ApiModelProperty(value = "角色权限codes")
    private String userRoles;

    public UserResDTO() {}

    public UserResDTO(String id, String name, String realName) {
        this.id = id;
        this.name = name;
        this.realName = realName;
    }

    public UserResDTO(String id, String name, String realName, String mobile, String telephone, String email, String bizMail, Integer status, Integer userStatus, String openUserid, String mainDept, String deptName, String avatar, String thumbAvatar, String qrCode, String address, String gender, String userName, String password, String userNo, Integer age) {
        this.id = id;
        this.name = name;
        this.realName = realName;
        this.mobile = mobile;
        this.telephone = telephone;
        this.email = email;
        this.bizMail = bizMail;
        this.status = status;
        this.userStatus = userStatus;
        this.openUserid = openUserid;
        this.mainDept = mainDept;
        this.deptName = deptName;
        this.avatar = avatar;
        this.thumbAvatar = thumbAvatar;
        this.qrCode = qrCode;
        this.address = address;
        this.gender = gender;
        this.userName = userName;
        this.password = password;
        this.userNo = userNo;
        this.age = age;
    }
}
