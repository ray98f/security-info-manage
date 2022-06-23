package com.security.info.manage.service.impl;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import com.security.info.manage.config.MinioConfig;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.SafeExpectModifyReqDTO;
import com.security.info.manage.dto.req.SafeExpectReqDTO;
import com.security.info.manage.dto.req.VxSendTextMsgReqDTO;
import com.security.info.manage.dto.res.*;
import com.security.info.manage.entity.File;
import com.security.info.manage.entity.SafeExpectUser;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.SafeExpectMapper;
import com.security.info.manage.service.FileService;
import com.security.info.manage.service.MsgService;
import com.security.info.manage.service.SafeExpectService;
import com.security.info.manage.utils.*;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
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
    private MsgService msgService;

    @Autowired
    private MinioClient client;

    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private MinioConfig minioConfig;

    @Value("${safe-expect.template.num}")
    private Integer safeExpectTemplateNum;

    @Value("${vx-business.jumppage}")
    private String jumppage;

    @Override
    public Page<SafeExpectResDTO> listSafeExpect(PageReqDTO pageReqDTO, String startTime, String endTime, String name) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return safeExpectMapper.listSafeExpect(pageReqDTO.of(), startTime, endTime, name);
    }

    @Override
    public Page<SafeExpectResDTO> vxListSafeExpect(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return safeExpectMapper.vxListSafeExpect(pageReqDTO.of(), TokenUtil.getCurrentPersonNo());
    }

    @Override
    public SafeExpectResDTO getSafeExpectVxDetail(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        SafeExpectResDTO safeExpectResDTO = safeExpectMapper.getSafeExpectDetail(id);
        if (!Objects.isNull(safeExpectResDTO)) {
            safeExpectResDTO.setSafeExpectInfo(safeExpectMapper.exportSafeExpectInfo(id));
            safeExpectResDTO.setSafeExpectCollectionUnion(safeExpectMapper.getSafeExpectCollectionUnionDetail(id));
            safeExpectResDTO.setUserInfo(safeExpectMapper.getSafeExpectUserInfo(id));
            safeExpectResDTO.setIsSign(safeExpectMapper.selectUserIsSign(id, TokenUtil.getCurrentPersonNo()));
        }
        return safeExpectResDTO;
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
            safeExpectResDTO.setIsSign(safeExpectMapper.selectUserIsSign(id, TokenUtil.getCurrentPersonNo()));
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
        } else {
            if (safeExpectReqDTO.getUserIds() != null && safeExpectReqDTO.getUserIds().size() > 0) {
                result = safeExpectMapper.insertSafeExpectUser(safeExpectReqDTO.getId(), safeExpectReqDTO.getUserIds());
                if (result < 0) {
                    throw new CommonException(ErrorCode.INSERT_ERROR);
                } else if (safeExpectReqDTO.getUserIds() != null && !safeExpectReqDTO.getUserIds().isEmpty()) {
                    VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
                    vxSendTextMsgReqDTO.setTouser(Joiner.on("|").join(safeExpectReqDTO.getUserIds()) + "|" + safeExpectReqDTO.getUserId());
                    vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的安全预想会参会通知，请前往小程序查看。"));
                    msgService.sendTextMsg(vxSendTextMsgReqDTO);
                }
            }
        }
    }

    @Override
    public List<SafeExpectTemplateResDTO> listSafeExpectTemplate(String riskId) {
        return safeExpectMapper.listSafeExpectTemplate(riskId, TokenUtil.getCurrentPersonNo());
    }

    @Override
    public void modifySafeExpect(SafeExpectModifyReqDTO safeExpectModifyReqDTO) {
        if (Objects.isNull(safeExpectModifyReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        if (!Objects.isNull(safeExpectModifyReqDTO.getSafeExpect())) {
            Integer result = safeExpectMapper.selectSafeExpectIsExist(safeExpectModifyReqDTO.getSafeExpect());
            if (result > 0) {
                throw new CommonException(ErrorCode.DATA_EXIST);
            }
            safeExpectModifyReqDTO.getSafeExpect().setCreateBy(TokenUtil.getCurrentPersonNo());
            result = safeExpectMapper.modifySafeExpect(safeExpectModifyReqDTO.getSafeExpect());
            if (result < 0) {
                throw new CommonException(ErrorCode.UPDATE_ERROR);
            } else {
                if (safeExpectModifyReqDTO.getSafeExpect().getUserIds() != null && safeExpectModifyReqDTO.getSafeExpect().getUserIds().size() > 0) {
                    result = safeExpectMapper.insertSafeExpectUser(safeExpectModifyReqDTO.getSafeExpect().getId(), safeExpectModifyReqDTO.getSafeExpect().getUserIds());
                    if (result < 0) {
                        throw new CommonException(ErrorCode.INSERT_ERROR);
                    } else if (safeExpectModifyReqDTO.getSafeExpect().getUserIds() != null && !safeExpectModifyReqDTO.getSafeExpect().getUserIds().isEmpty()) {
                        VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
                        vxSendTextMsgReqDTO.setTouser(Joiner.on("|").join(safeExpectModifyReqDTO.getSafeExpect().getUserIds()));
                        vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的安全预想会参会通知，请前往小程序查看。"));
                        msgService.sendTextMsg(vxSendTextMsgReqDTO);
                    }
                }
            }
        }
        if (!Objects.isNull(safeExpectModifyReqDTO.getSafeExpectInfo())) {
            Integer result = safeExpectMapper.modifySafeExpectInfo(safeExpectModifyReqDTO.getSafeExpectInfo());
            if (result < 0) {
                throw new CommonException(ErrorCode.UPDATE_ERROR);
            }
        }
        if (!Objects.isNull(safeExpectModifyReqDTO.getSafeExpectCollectionUnion()) && safeExpectModifyReqDTO.getSafeExpectCollectionUnion().getCollectionUnionTime() != null) {
            Integer result = safeExpectMapper.modifySafeExpectCollectionUnion(safeExpectModifyReqDTO.getSafeExpectCollectionUnion());
            if (result < 0) {
                throw new CommonException(ErrorCode.UPDATE_ERROR);
            }
        }
        if (safeExpectModifyReqDTO.getSafeExpectInfo().getIsTemplate() == 1) {
            Integer result = safeExpectMapper.selectSafeExpectTemplateNum(TokenUtil.getCurrentPersonNo());
            if (result >= safeExpectTemplateNum) {
                throw new CommonException(ErrorCode.SAFE_EXPECT_TEMPLATE_NUM_MAX, safeExpectTemplateNum.toString());
            }
            result = safeExpectMapper.insertSafeExpectTemplate(safeExpectModifyReqDTO.getSafeExpectInfo(), TokenUtil.getCurrentPersonNo());
            if (result < 0) {
                throw new CommonException(ErrorCode.INSERT_ERROR);
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
    public void cancelSafeExpect(SafeExpectReqDTO safeExpectReqDTO) {
        if (Objects.isNull(safeExpectReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        List<String> userIds = safeExpectMapper.selectUserIdById(safeExpectReqDTO.getId());
        if (!Objects.isNull(userIds) && !userIds.isEmpty()) {
            VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
            vxSendTextMsgReqDTO.setTouser(Joiner.on("|").join(userIds));
            vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条安全预想会已取消，请前往小程序查看。"));
            msgService.sendTextMsg(vxSendTextMsgReqDTO);
        }
        safeExpectReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = safeExpectMapper.cancelSafeExpect(safeExpectReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
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
    public void vxSignSafeExpectUser(String id, Integer isSign) {
        Integer result = safeExpectMapper.vxSignSafeExpectUser(id, isSign, TokenUtil.getCurrentPersonNo());
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
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
        safeExpectCollectionUnionResDTO.setCollectionUnionTimeStr(safeExpectCollectionUnionResDTO.getCollectionUnionTime() != null ? sdf.format(safeExpectCollectionUnionResDTO.getCollectionUnionTime()) : null);
        Map<String, Object> dataMap = ObjectUtils.objectToMap(safeExpectResDTO);
        dataMap.putAll(ObjectUtils.objectToMap(safeExpectInfoResDTO));
        dataMap.putAll(ObjectUtils.objectToMap(safeExpectCollectionUnionResDTO));
        dataMap.put("safeExpectQr", QrCodeUtil.generateAsBase64(jumppage +
                "?page=pages/anticipatedSafety/detail" +
                "&id="+ safeExpectResDTO.getId(), initQrConfig(), "png"));
        dataMap.put("constructionQr", QrCodeUtil.generateAsBase64(jumppage +
                "?page=pages/constructionTasks/detail" +
                "&id="+ safeExpectResDTO.getWorkId(), initQrConfig(), "png"));
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
        String url = minioConfig.getImgPath() + "/" + bizCode + "/" + fileName;
        return fileService.insertFile(url, bizCode, oldName);
    }

    private static QrConfig initQrConfig() {
        QrConfig config = new QrConfig(300, 300);
        // 设置边距，既二维码和背景之间的边距
        config.setMargin(2);
        // 设置前景色，既二维码颜色（青色）
        config.setForeColor(Color.BLACK.getRGB());
        // 设置背景色（灰色）
        config.setBackColor(Color.WHITE.getRGB());
        return config;
    }

}
