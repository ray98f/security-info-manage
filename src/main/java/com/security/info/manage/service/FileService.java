package com.security.info.manage.service;

import com.security.info.manage.entity.File;

/**
 * @author frp
 */
public interface FileService {

    File insertFile(String url, String bizCode, String name);

}
