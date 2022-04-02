package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.ApplianceConfigReqDTO;
import com.security.info.manage.dto.req.ApplianceReqDTO;
import com.security.info.manage.dto.req.ApplianceTypeReqDTO;
import com.security.info.manage.dto.req.TrainDetailReqDTO;
import com.security.info.manage.dto.res.ApplianceConfigResDTO;
import com.security.info.manage.dto.res.ApplianceResDTO;
import com.security.info.manage.dto.res.ApplianceTypeTreeResDTO;
import com.security.info.manage.dto.res.ApplianceWarnResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.ApplianceMapper;
import com.security.info.manage.service.ApplianceService;
import com.security.info.manage.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.security.info.manage.utils.Constants.XLS;
import static com.security.info.manage.utils.Constants.XLSX;

/**
 * @author frp
 */
@Service
@Slf4j
public class ApplianceServiceImpl implements ApplianceService {

    @Autowired
    private ApplianceMapper applianceMapper;

    @Override
    public Page<ApplianceResDTO> listAppliance(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return applianceMapper.listAppliance(pageReqDTO.of());
    }

    @Override
    public ApplianceResDTO getApplianceDetail(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        return applianceMapper.getApplianceDetail(id);
    }

    @Override
    public void insertAppliance(ApplianceReqDTO applianceReqDTO) {
        if (Objects.isNull(applianceReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = applianceMapper.selectApplianceIsExist(applianceReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        applianceReqDTO.setId(TokenUtil.getUuId());
        applianceReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = applianceMapper.insertAppliance(applianceReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void modifyAppliance(ApplianceReqDTO applianceReqDTO) {
        if (Objects.isNull(applianceReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = applianceMapper.selectApplianceIsExist(applianceReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        applianceReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = applianceMapper.modifyAppliance(applianceReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void deleteAppliance(ApplianceReqDTO applianceReqDTO) {
        if (Objects.isNull(applianceReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        applianceReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        Integer result = applianceMapper.deleteAppliance(applianceReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importApplianceConfig(MultipartFile file) {
        try {
            Workbook workbook;
            String fileName = file.getOriginalFilename();
            FileInputStream fileInputStream = new FileInputStream(FileUtils.transferToFile(file));
            if (Objects.requireNonNull(fileName).endsWith(XLS)) {
                workbook = new HSSFWorkbook(fileInputStream);
            } else if (fileName.endsWith(XLSX)) {
                workbook = new XSSFWorkbook(fileInputStream);
            } else {
                throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
            }
            Sheet sheet = workbook.getSheetAt(0);
            List<ApplianceConfigReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                ApplianceConfigReqDTO reqDTO = new ApplianceConfigReqDTO();
                cells.getCell(1).setCellType(1);
                reqDTO.setYear(cells.getCell(1) == null ? null : Integer.valueOf(cells.getCell(1).getStringCellValue()));
                cells.getCell(4).setCellType(1);
                reqDTO.setDeptName(cells.getCell(4) == null ? null : cells.getCell(4).getStringCellValue());
                cells.getCell(6).setCellType(1);
                reqDTO.setUserName(cells.getCell(6) == null ? null : cells.getCell(6).getStringCellValue());
                cells.getCell(7).setCellType(1);
                reqDTO.setWorkType(cells.getCell(7) == null ? null : cells.getCell(7).getStringCellValue());
                cells.getCell(8).setCellType(1);
                reqDTO.setHazardFactors(cells.getCell(8) == null ? null : cells.getCell(8).getStringCellValue());
                cells.getCell(9).setCellType(1);
                reqDTO.setApplianceName(cells.getCell(9) == null ? null : cells.getCell(9).getStringCellValue());
                cells.getCell(10).setCellType(1);
                reqDTO.setApplianceCode(cells.getCell(10) == null ? null : cells.getCell(10).getStringCellValue());
                cells.getCell(11).setCellType(1);
                reqDTO.setNum(cells.getCell(11) == null ? null : Integer.valueOf(cells.getCell(11).getStringCellValue()));
                reqDTO.setReceivingTime(cells.getCell(13) == null ? null : cells.getCell(13).getDateCellValue());
                cells.getCell(14).setCellType(1);
                String str = cells.getCell(14) == null ? null : cells.getCell(14).getStringCellValue();
                Matcher m = Pattern.compile("[^0-9]").matcher(Objects.requireNonNull(str));
                reqDTO.setEffectiveTime(DateUtils.getMonthNewDate(reqDTO.getReceivingTime(), Integer.valueOf(m.replaceAll("").trim())));
                cells.getCell(16).setCellType(1);
                reqDTO.setChangeReason(cells.getCell(16) == null ? null : cells.getCell(16).getStringCellValue());
                cells.getCell(17).setCellType(1);
                reqDTO.setRemark(cells.getCell(17) == null ? null : cells.getCell(17).getStringCellValue());
                reqDTO.setId(TokenUtil.getUuId());
                reqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
                temp.add(reqDTO);
            }
            fileInputStream.close();
            applianceMapper.importApplianceConfig(temp);

        } catch (IOException | ParseException e) {
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
        ApplianceConfigResDTO applianceConfigResDTO = applianceMapper.getApplianceConfigDetail(applianceConfigReqDTO.getId());
        if (!Objects.isNull(applianceConfigResDTO)) {
            applianceConfigResDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
            applianceConfigResDTO.setReceivingTime(applianceConfigReqDTO.getReceivingTime());
            applianceConfigResDTO.setEffectiveTime(applianceConfigReqDTO.getEffectiveTime());
            applianceConfigResDTO.setChangeReason(applianceConfigReqDTO.getChangeReason());
            applianceConfigResDTO.setNum(applianceConfigReqDTO.getNum());
            Integer result = applianceMapper.changeAppliance(applianceConfigResDTO);
            if (result < 0) {
                throw new CommonException(ErrorCode.UPDATE_ERROR);
            }
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

    @Override
    public List<ApplianceConfigResDTO> userArchives(String id) {
        return applianceMapper.userArchives(id);
    }

}
