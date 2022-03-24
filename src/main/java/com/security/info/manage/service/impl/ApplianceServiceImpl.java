package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.ApplianceConfigReqDTO;
import com.security.info.manage.dto.req.ApplianceTypeReqDTO;
import com.security.info.manage.dto.req.TrainDetailReqDTO;
import com.security.info.manage.dto.res.ApplianceConfigResDTO;
import com.security.info.manage.dto.res.ApplianceTypeTreeResDTO;
import com.security.info.manage.dto.res.ApplianceWarnResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.ApplianceMapper;
import com.security.info.manage.service.ApplianceService;
import com.security.info.manage.utils.ApplianceTypeTreeToolUtils;
import com.security.info.manage.utils.DeptTreeToolUtils;
import com.security.info.manage.utils.FileUtils;
import com.security.info.manage.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class ApplianceServiceImpl implements ApplianceService {

    @Autowired
    private ApplianceMapper applianceMapper;

    @Override
    public List<ApplianceTypeTreeResDTO> listTypeTree(Integer status) {
        List<ApplianceTypeTreeResDTO> extraRootList = applianceMapper.getTypeRoot(status);
        if (Objects.isNull(extraRootList)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        List<ApplianceTypeTreeResDTO> extraBodyList = applianceMapper.getTypeBody(status);
        ApplianceTypeTreeToolUtils extraTree = new ApplianceTypeTreeToolUtils(extraRootList, extraBodyList);
        return extraTree.getTree();
    }

    @Override
    public ApplianceTypeTreeResDTO getTypeDetail(String id) {
        return applianceMapper.getTypeDetail(id);
    }

    @Override
    public void addType(ApplianceTypeReqDTO applianceTypeReqDTO) {
        if (Objects.isNull(applianceTypeReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        applianceTypeReqDTO.setId(TokenUtil.getUuId());
        applianceTypeReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        Integer result = applianceMapper.addType(applianceTypeReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void modifyType(ApplianceTypeReqDTO applianceTypeReqDTO) {
        if (Objects.isNull(applianceTypeReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        if (applianceTypeReqDTO.getStatus() == 1) {
            Integer i = applianceMapper.selectHadChild(applianceTypeReqDTO.getId());
            if (i > 0) {
                throw new CommonException(ErrorCode.CANT_UPDATE_HAD_CHILD);
            }
        }
        applianceTypeReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        Integer result = applianceMapper.modifyType(applianceTypeReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void deleteType(ApplianceTypeReqDTO applianceTypeReqDTO) {
        if (Objects.isNull(applianceTypeReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer i = applianceMapper.selectHadChild(applianceTypeReqDTO.getId());
        if (i > 0) {
            throw new CommonException(ErrorCode.CANT_DELETE_HAD_CHILD);
        }
        applianceTypeReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        Integer result = applianceMapper.deleteType(applianceTypeReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importAppliance(MultipartFile file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(FileUtils.transferToFile(file));
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            List<ApplianceConfigReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                ApplianceConfigReqDTO reqDTO = new ApplianceConfigReqDTO();
                cells.getCell(0).setCellType(1);
                reqDTO.setApplianceName(cells.getCell(0) == null ? null : cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(1);
                reqDTO.setApplianceCode(cells.getCell(1) == null ? null : cells.getCell(1).getStringCellValue());
                cells.getCell(2).setCellType(1);
                reqDTO.setApplianceType(cells.getCell(2) == null ? null : cells.getCell(2).getStringCellValue());
                cells.getCell(3).setCellType(1);
                reqDTO.setUserNo(cells.getCell(3) == null ? null : cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(1);
                reqDTO.setUserName(cells.getCell(4) == null ? null : cells.getCell(4).getStringCellValue());
                cells.getCell(5).setCellType(1);
                reqDTO.setNum(cells.getCell(5) == null ? null : Integer.valueOf(cells.getCell(5).getStringCellValue()));
                reqDTO.setReceivingTime(cells.getCell(6) == null ? null : cells.getCell(6).getDateCellValue());
                reqDTO.setEffectiveTime(cells.getCell(7) == null ? null : cells.getCell(7).getDateCellValue());
                cells.getCell(8).setCellType(1);
                reqDTO.setRemark(cells.getCell(8) == null ? null : cells.getCell(8).getStringCellValue());
                reqDTO.setId(TokenUtil.getUuId());
                reqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
                temp.add(reqDTO);
            }
            fileInputStream.close();
            applianceMapper.importAppliance(temp);
        } catch (IOException e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public Page<ApplianceConfigResDTO> listApplianceConfig(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return applianceMapper.listApplianceConfig(pageReqDTO.of());
    }

    @Override
    public ApplianceConfigResDTO getApplianceConfigDetail(String id) {
        return applianceMapper.getApplianceConfigDetail(id);
    }

    @Override
    public void changeAppliance(ApplianceConfigReqDTO applianceConfigReqDTO) {
        if (Objects.isNull(applianceConfigReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        applianceConfigReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = applianceMapper.changeAppliance(applianceConfigReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public Page<ApplianceWarnResDTO> listApplianceWarn(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return applianceMapper.listApplianceWarn(pageReqDTO.of());
    }

    @Override
    public void handleApplianceWarn(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = applianceMapper.modifyApplianceWarn(id);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

}
