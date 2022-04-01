package com.security.info.manage.service.impl;

import com.security.info.manage.mapper.FileMapper;
import com.security.info.manage.service.FileService;
import com.security.info.manage.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author frp
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMapper fileMapper;

    @Override
    public void insertFile(String url, String bizCode, String name) {
        fileMapper.insertFile(TokenUtil.getUuId(), url, bizCode, name, TokenUtil.getCurrentPersonNo());
    }

}
