package com.security.info.manage.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.ApplianceConfigReqDTO;
import com.security.info.manage.dto.req.ConstructionReqDTO;
import com.security.info.manage.dto.req.ConstructionTypeReqDTO;
import com.security.info.manage.dto.req.WeekPlanReqDTO;
import com.security.info.manage.dto.res.ConstructionResDTO;
import com.security.info.manage.dto.res.ConstructionTypeResDTO;
import com.security.info.manage.dto.res.WeekPlanResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.ConstructionMapper;
import com.security.info.manage.service.ConstructionService;
import com.security.info.manage.utils.DateUtils;
import com.security.info.manage.utils.FileUtils;
import com.security.info.manage.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.security.info.manage.utils.Constants.XLS;
import static com.security.info.manage.utils.Constants.XLSX;

/**
 * @author frp
 */
@Service
@Slf4j
public class ConstructionServiceImpl implements ConstructionService {


    @Autowired
    private ConstructionMapper constructionMapper;

    @Override
    public Page<ConstructionTypeResDTO> listConstructionType(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return constructionMapper.listConstructionType(pageReqDTO.of());
    }

    @Override
    public List<ConstructionTypeResDTO> listAllConstructionType() {
        return constructionMapper.listAllConstructionType();
    }

    @Override
    public void modifyConstructionType(ConstructionTypeReqDTO constructionTypeReqDTO) {
        if (Objects.isNull(constructionTypeReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = constructionMapper.selectConstructionTypeIsExist(constructionTypeReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        constructionTypeReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = constructionMapper.modifyConstructionType(constructionTypeReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void addConstructionType(ConstructionTypeReqDTO constructionTypeReqDTO) {
        if (Objects.isNull(constructionTypeReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = constructionMapper.selectConstructionTypeIsExist(constructionTypeReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        constructionTypeReqDTO.setId(TokenUtil.getUuId());
        constructionTypeReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = constructionMapper.addConstructionType(constructionTypeReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void deleteConstructionType(ConstructionTypeReqDTO constructionTypeReqDTO) {
        if (Objects.isNull(constructionTypeReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        constructionTypeReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        Integer result = constructionMapper.deleteConstructionType(constructionTypeReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public Page<WeekPlanResDTO> listWeekPlan(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return constructionMapper.listWeekPlan(pageReqDTO.of());
    }

    @Override
    public void modifyWeekPlan(WeekPlanReqDTO weekPlanReqDTO) {
        if (Objects.isNull(weekPlanReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = constructionMapper.selectWeekPlanIsExist(weekPlanReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        weekPlanReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = constructionMapper.modifyWeekPlan(weekPlanReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void addWeekPlan(WeekPlanReqDTO weekPlanReqDTO) {
        if (Objects.isNull(weekPlanReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = constructionMapper.selectWeekPlanIsExist(weekPlanReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        weekPlanReqDTO.setId(TokenUtil.getUuId());
        weekPlanReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = constructionMapper.addWeekPlan(weekPlanReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void deleteWeekPlan(WeekPlanReqDTO weekPlanReqDTO) {
        if (Objects.isNull(weekPlanReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        weekPlanReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        Integer result = constructionMapper.deleteWeekPlan(weekPlanReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public Page<ConstructionResDTO> listConstruction(String planId, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return constructionMapper.listConstruction(pageReqDTO.of(), planId);
    }

    @Override
    public ConstructionResDTO getConstructionDetail(String id) {
        return constructionMapper.getConstructionDetail(id);
    }

    @Override
    public Page<ConstructionResDTO> vxListConstruction(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return constructionMapper.vxListConstruction(pageReqDTO.of(), TokenUtil.getCurrentPersonNo());
    }

    @Override
    public void addConstruction(ConstructionReqDTO constructionReqDTO) {
        if (Objects.isNull(constructionReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        constructionReqDTO.setId(TokenUtil.getUuId());
        constructionReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = constructionMapper.addConstruction(constructionReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void modifyConstruction(ConstructionReqDTO constructionReqDTO) {
        if (Objects.isNull(constructionReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        constructionReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = constructionMapper.modifyConstruction(constructionReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void deleteConstruction(ConstructionReqDTO constructionReqDTO) {
        if (Objects.isNull(constructionReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        constructionReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = constructionMapper.deleteConstruction(constructionReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public void importConstruction(MultipartFile file, String planId) {
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat dayDate = new SimpleDateFormat("yyyy-MM-dd");
            String day = "";
            String typeName = "";
            List<ConstructionReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                Calendar cal = Calendar.getInstance();
                cells.getCell(0).setCellType(1);
                if (cells.getCell(0).getStringCellValue().contains("月")) {
                    day = cal.get(Calendar.YEAR) + "年" + cells.getCell(0).getStringCellValue().split("星期")[0];
                    Date date = new SimpleDateFormat("yyyy年MM月dd日").parse(day);
                    day = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    continue;
                } else if (cells.getCell(1) == null || "".equals(cells.getCell(1).getStringCellValue())) {
                    typeName = cells.getCell(0).getStringCellValue();
                    continue;
                } else if ("序号".equals(cells.getCell(0).getStringCellValue())) {
                    continue;
                }
                ConstructionReqDTO reqDTO = new ConstructionReqDTO();
                reqDTO.setDate(dayDate.parse(day));
                cal.setTime(dayDate.parse(day));
                cells.getCell(0).setCellType(1);
                reqDTO.setSort(cells.getCell(0) == null ? null : Integer.valueOf(cells.getCell(0).getStringCellValue()));
                cells.getCell(1).setCellType(1);
                reqDTO.setNo(cells.getCell(1) == null ? null : cells.getCell(1).getStringCellValue());
                cells.getCell(2).setCellType(1);
                reqDTO.setOrgName(cells.getCell(2) == null ? null : cells.getCell(2).getStringCellValue());
                cells.getCell(3).setCellType(1);
                String workDate = cells.getCell(3) == null ? null : cells.getCell(3).getStringCellValue();
                if (Objects.requireNonNull(workDate).contains("次日")) {
                    reqDTO.setIsDay(1);
                    workDate = workDate.replaceAll("次日", "");
                    reqDTO.setStartTime(sdf.parse(day + " " + workDate.split("~")[0]));
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    reqDTO.setEndTime(sdf.parse(dayDate.format(cal.getTime()) + " " + workDate.split("~")[1]));
                } else {
                    reqDTO.setIsDay(0);
                    reqDTO.setStartTime(sdf.parse(day + " " + workDate.split("~")[0]));
                    reqDTO.setEndTime(sdf.parse(day + " " + workDate.split("~")[1]));
                }
                cells.getCell(4).setCellType(1);
                reqDTO.setName(cells.getCell(4) == null ? null : cells.getCell(4).getStringCellValue());
                cells.getCell(5).setCellType(1);
                reqDTO.setRegion(cells.getCell(5) == null ? null : cells.getCell(5).getStringCellValue());
                cells.getCell(6).setCellType(1);
                reqDTO.setElectricArrange(cells.getCell(6) == null ? null : cells.getCell(6).getStringCellValue());
                cells.getCell(7).setCellType(1);
                reqDTO.setProtectMeasures(cells.getCell(7) == null ? null : cells.getCell(7).getStringCellValue());
                cells.getCell(8).setCellType(1);
                reqDTO.setCoordinationRequirement(cells.getCell(8) == null ? null : cells.getCell(8).getStringCellValue());
                cells.getCell(9).setCellType(1);
                String user = cells.getCell(9) == null ? null : cells.getCell(9).getStringCellValue();
                if (user != null && !user.isEmpty()) {
                    reqDTO.setUserName(user.split(":")[0]);
                    reqDTO.setPhone(user.split(":")[1]);
                }
                cells.getCell(10).setCellType(1);
                reqDTO.setRemark(cells.getCell(10) == null ? null : cells.getCell(10).getStringCellValue());
                reqDTO.setId(TokenUtil.getUuId());
                reqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
                reqDTO.setTypeName(typeName);
                temp.add(reqDTO);
            }
            fileInputStream.close();
            constructionMapper.importConstruction(temp, planId);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }
}
