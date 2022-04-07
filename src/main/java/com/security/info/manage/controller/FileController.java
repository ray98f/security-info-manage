package com.security.info.manage.controller;

import com.security.info.manage.config.MinioConfig;
import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.entity.File;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.service.FileService;
import com.security.info.manage.utils.FileUploadUtils;
import com.security.info.manage.utils.MinioUtils;
import com.security.info.manage.utils.TokenUtil;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/file")
@Api(tags = "文件接口")
@Validated
public class FileController {

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private FileService fileService;

    @Autowired
    private MinioClient client;

    @Autowired
    private MinioUtils minioUtils;

    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    public DataResponse<File> fileUpload(@RequestParam MultipartFile file, String bizCode) throws Exception {
        return DataResponse.of(uploadFile(file, bizCode));
    }

    public File uploadFile(MultipartFile file, String bizCode) throws Exception {
        if (!minioUtils.bucketExists(bizCode)) {
            minioUtils.makeBucket(bizCode);
        }
        String oldName = file.getOriginalFilename();
        String fileName = FileUploadUtils.extractFilename(file);
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bizCode)
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        client.putObject(args);
        String url = minioConfig.getUrl() + "/" + bizCode + "/" + fileName;
        return fileService.insertFile(url, bizCode, oldName);
    }
}