package com.security.info.manage.controller;

import com.security.info.manage.config.MinioConfig;
import com.security.info.manage.config.repository.LawFileEsRepository;
import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.entity.File;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.service.FileService;
import com.security.info.manage.utils.*;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    private LawFileEsRepository repository;

    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    public DataResponse<File> fileUpload(@RequestParam MultipartFile file, String bizCode) throws Exception {
        File fileRes;
        fileRes = uploadFile(file, bizCode);
        if (Objects.isNull(fileRes)) {
            throw new CommonException(ErrorCode.FILE_UPLOAD_FAIL);
        }
        if ("law".equals(bizCode)) {
            String regExHtml = "<[^>]+>";
            Pattern pHtml = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
            Matcher mHtml = pHtml.matcher(TikaUtils.extractHtml(file));
            fileRes.setContent(mHtml.replaceAll("").replaceAll("\n", "").replace("\r", ""));
            repository.save(fileRes);
        }
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
        String url = minioConfig.getImgPath() + "/" + bizCode + "/" + fileName;
        return fileService.insertFile(url, bizCode, oldName);
    }
}