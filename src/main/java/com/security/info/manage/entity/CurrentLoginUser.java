package com.security.info.manage.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/8/3 11:34
 */
@Data
public class CurrentLoginUser implements Serializable {
    // 工号
    private String personId;

    // 工号
    private String personNo;

    // 姓名
    private String personName;

    public CurrentLoginUser(){}

    public CurrentLoginUser(String personNo, String personName) {

        this.personNo = personNo;
        this.personName = personName;
    }
}
