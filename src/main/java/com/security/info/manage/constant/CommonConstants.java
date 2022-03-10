package com.security.info.manage.constant;

/**
 * 常量
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/8/2 10:31
 */
public interface CommonConstants {

    /**
     * 删除
     */
    String IS_DEL = "1";


    /**
     * 正常
     */
    String STATUS_NORMAL = "0";

    /**
     * 禁用
     */
    String STATUS_ban = "9";

    /**
     * 编码
     */
    String UTF8 = "UTF-8";

    /**
     * JSON 资源
     */
    String CONTENT_TYPE = "application/json; charset=utf-8";


    /**
     * 成功标记
     */
    Integer SUCCESS = 0;

    /**
     * 失败标记
     */
    Integer FAIL = 401;

    /**
     * 当前页
     */
    String CURRENT = "current";

    /**
     * size
     */
    String SIZE = "size";


    /**
     * 人脸识别成功标记
     */
    Integer COMPARE_FACE_SUCCESS = 1;

    /**
     * 人脸识别失败标记
     */
    Integer COMPARE_FACE_FAIL = 2;

    String BUCKET_POLICY_DEFAULT ="";
    String BUCKET_POLICY_READ ="read";

    String BUCKET_POLICY_WRITE ="write";

    String BUCKET_POLICY_READ_WRITE ="read-write";
}
