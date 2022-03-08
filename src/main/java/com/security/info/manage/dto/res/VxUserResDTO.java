package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class VxUserResDTO {

    private String userid;

    private String name;

    private List<String> department;

    private String position;

    private String mobile;

    private String gender;

    private String email;

    private String biz_mail;

    private String avatar;

    private String thumb_avatar;

    private String telephone;

    private Integer status;

    private String address;

    private String english_name;

    private String open_userid;

    private String main_department;

    private String qr_code;

}
