package com.security.info.manage.controller;

import com.alibaba.fastjson.JSON;
import com.security.info.manage.config.MinioConfig;
import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.entity.File;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.service.FileService;
import com.security.info.manage.utils.*;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

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
        File fileRes = uploadFile(file, bizCode);
        if (Objects.isNull(fileRes)) {
            throw new CommonException(ErrorCode.FILE_UPLOAD_FAIL);
        }

        // todo 验证文件上传es是否可用
//        if ("law".equals(bizCode)) {
//            byte[] bytes = getContent(file);
//            String base64 = Base64.getEncoder().encodeToString(bytes);
//            fileRes.setContent(base64);
//            IndexRequest indexRequest = new IndexRequest("fileindex");
//            //上传同时，使用attachment pipline进行提取文件
//            indexRequest.source(JSON.toJSONString(fileRes), XContentType.JSON);
//            indexRequest.setPipeline("attachment");
//            IndexResponse indexResponse = EsUtil.client.index(indexRequest, RequestOptions.DEFAULT);
//            log.info("send to eSearch:" + fileRes.getFileName());
//            log.info("send to eSeach results:" + indexResponse);
//        }
        return DataResponse.of(fileRes);
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

    public byte[] getContent(MultipartFile multipartFile) throws IOException {
        java.io.File file = FileUtils.transferToFile(multipartFile);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        fi.close();
        return buffer;
    }
}