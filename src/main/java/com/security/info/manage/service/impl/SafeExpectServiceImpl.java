package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.config.MinioConfig;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.SafeExpectModifyReqDTO;
import com.security.info.manage.dto.req.SafeExpectReqDTO;
import com.security.info.manage.dto.res.SafeExpectCollectionUnionResDTO;
import com.security.info.manage.dto.res.SafeExpectInfoResDTO;
import com.security.info.manage.dto.res.SafeExpectResDTO;
import com.security.info.manage.dto.res.SafeExpectUserResDTO;
import com.security.info.manage.entity.File;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.SafeExpectMapper;
import com.security.info.manage.service.FileService;
import com.security.info.manage.service.SafeExpectService;
import com.security.info.manage.utils.*;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class SafeExpectServiceImpl implements SafeExpectService {

    public static final String SAFE_EXPECT = "safe-expect";
    @Autowired
    private SafeExpectMapper safeExpectMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private MinioClient client;

    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private MinioConfig minioConfig;

    @Override
    public Page<SafeExpectResDTO> listSafeExpect(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return safeExpectMapper.listSafeExpect(pageReqDTO.of());
    }

    @Override
    public Page<SafeExpectResDTO> vxListSafeExpect(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return safeExpectMapper.vxListSafeExpect(pageReqDTO.of(), TokenUtil.getCurrentPersonNo());
    }

    @Override
    public SafeExpectResDTO getSafeExpectDetail(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        SafeExpectResDTO safeExpectResDTO = safeExpectMapper.getSafeExpectDetail(id);
        if (!Objects.isNull(safeExpectResDTO)) {
            safeExpectResDTO.setSafeExpectInfo(safeExpectMapper.getSafeExpectInfoDetail(id));
            safeExpectResDTO.setSafeExpectCollectionUnion(safeExpectMapper.getSafeExpectCollectionUnionDetail(id));
            safeExpectResDTO.setUserInfo(safeExpectMapper.getSafeExpectUserInfo(id));
        }
        return safeExpectResDTO;
    }

    @Override
    public void addSafeExpect(SafeExpectReqDTO safeExpectReqDTO) {
        if (Objects.isNull(safeExpectReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = safeExpectMapper.selectSafeExpectIsExist(safeExpectReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        safeExpectReqDTO.setId(TokenUtil.getUuId());
        safeExpectReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = safeExpectMapper.addSafeExpect(safeExpectReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
        if (result > 0) {
            if (safeExpectReqDTO.getUserIds() != null && safeExpectReqDTO.getUserIds().size() > 0) {
                result = safeExpectMapper.insertSafeExpectUser(safeExpectReqDTO.getId(), safeExpectReqDTO.getUserIds());
                if (result < 0) {
                    throw new CommonException(ErrorCode.INSERT_ERROR);
                }
            }
        }
    }

    @Override
    public void modifySafeExpect(SafeExpectModifyReqDTO safeExpectModifyReqDTO) {
        if (Objects.isNull(safeExpectModifyReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        if (!Objects.isNull(safeExpectModifyReqDTO.getSafeExpectInfo())) {
            Integer result = safeExpectMapper.modifySafeExpectInfo(safeExpectModifyReqDTO.getSafeExpectInfo());
            if (result < 0) {
                throw new CommonException(ErrorCode.UPDATE_ERROR);
            }
        }
        if (!Objects.isNull(safeExpectModifyReqDTO.getSafeExpectCollectionUnion())) {
            Integer result = safeExpectMapper.modifySafeExpectCollectionUnion(safeExpectModifyReqDTO.getSafeExpectCollectionUnion());
            if (result < 0) {
                throw new CommonException(ErrorCode.UPDATE_ERROR);
            }
        }
    }

    @Override
    public void deleteSafeExpect(SafeExpectReqDTO safeExpectReqDTO) {
        if (Objects.isNull(safeExpectReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        safeExpectReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = safeExpectMapper.deleteSafeExpect(safeExpectReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public Page<SafeExpectUserResDTO> listSafeExpectUser(String id, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return safeExpectMapper.listSafeExpectUser(pageReqDTO.of(), id);
    }

    @Override
    public void signSafeExpectUser(SafeExpectUserResDTO safeExpectUserResDTO) {
        if (Objects.isNull(safeExpectUserResDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = safeExpectMapper.signSafeExpectUser(safeExpectUserResDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void vxSignSafeExpectUser(String id) {
        Integer result = safeExpectMapper.vxSignSafeExpectUser(id, TokenUtil.getCurrentPersonNo());
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public Map<String, Object> exportSafeExpectData(String id) {
        SafeExpectResDTO safeExpectResDTO = safeExpectMapper.getSafeExpectDetail(id);
        SafeExpectInfoResDTO safeExpectInfoResDTO = safeExpectMapper.exportSafeExpectInfo(id);
        SafeExpectCollectionUnionResDTO safeExpectCollectionUnionResDTO = safeExpectMapper.getSafeExpectCollectionUnionDetail(id);
        if (Objects.isNull(safeExpectResDTO) || Objects.isNull(safeExpectInfoResDTO) || Objects.isNull(safeExpectCollectionUnionResDTO)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
        safeExpectCollectionUnionResDTO.setCollectionUnionTimeStr(sdf.format(safeExpectCollectionUnionResDTO.getCollectionUnionTime()));
        Map<String, Object> dataMap = ObjectUtils.objectToMap(safeExpectResDTO);
        dataMap.putAll(ObjectUtils.objectToMap(safeExpectInfoResDTO));
        dataMap.putAll(ObjectUtils.objectToMap(safeExpectCollectionUnionResDTO));
        return dataMap;
    }

    @Override
    public File exportSafeExpect(String id) throws Exception {
        SafeExpectResDTO safeExpectResDTO = safeExpectMapper.getSafeExpectDetail(id);
        SafeExpectInfoResDTO safeExpectInfoResDTO = safeExpectMapper.exportSafeExpectInfo(id);
        SafeExpectCollectionUnionResDTO safeExpectCollectionUnionResDTO = safeExpectMapper.getSafeExpectCollectionUnionDetail(id);
        if (Objects.isNull(safeExpectResDTO) || Objects.isNull(safeExpectInfoResDTO) || Objects.isNull(safeExpectCollectionUnionResDTO)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
        safeExpectCollectionUnionResDTO.setCollectionUnionTimeStr(sdf.format(safeExpectCollectionUnionResDTO.getCollectionUnionTime()));
        Map<String, Object> dataMap = ObjectUtils.objectToMap(safeExpectResDTO);
        dataMap.putAll(ObjectUtils.objectToMap(safeExpectInfoResDTO));
        dataMap.putAll(ObjectUtils.objectToMap(safeExpectCollectionUnionResDTO));
        MultipartFile file = DocUtils.saveWord(safeExpectResDTO.getWorkNo() + "安全预想会与收工会记录.docx", SAFE_EXPECT, dataMap);
        return uploadFile(file, SAFE_EXPECT);
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
