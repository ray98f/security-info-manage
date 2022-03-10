package com.security.info.manage.dto;

import lombok.Data;

/**
 * 文件信息
 *
 * @author zhangxin
 * @version 1.0
 * @date 2021/7/5 10:18
 */
@Data
public class AttachmentFile {

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件地址
     */
    private String url;

    /**
     * 文件名称
     */
    private String thumbnailsName;

    /**
     * 文件地址
     */
    private String thumbnailsUrl;
}
