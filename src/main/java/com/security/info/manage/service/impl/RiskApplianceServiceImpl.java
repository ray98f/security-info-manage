package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.RiskApplianceReqDTO;
import com.security.info.manage.dto.res.RiskApplianceResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.RiskApplianceMapper;
import com.security.info.manage.service.RiskApplianceService;
import com.security.info.manage.utils.FileUtils;
import com.security.info.manage.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.security.info.manage.utils.Constants.XLS;
import static com.security.info.manage.utils.Constants.XLSX;

/**
 * @author frp
 */
@Service
@Slf4j
public class RiskApplianceServiceImpl implements RiskApplianceService {

    @Autowired
    private RiskApplianceMapper riskApplianceMapper;

    @Override
    public Page<RiskApplianceResDTO> listRiskAppliance(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return riskApplianceMapper.listRiskAppliance(pageReqDTO.of());
    }

    @Override
    public List<RiskApplianceResDTO> listAllRiskAppliance() {
        return riskApplianceMapper.listAllRiskAppliance();
    }

    @Override
    public void modifyRiskAppliance(RiskApplianceReqDTO riskApplianceReqDTO) {
        if (Objects.isNull(riskApplianceReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = riskApplianceMapper.selectRiskApplianceIsExist(riskApplianceReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        riskApplianceReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = riskApplianceMapper.modifyRiskAppliance(riskApplianceReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void addRiskAppliance(RiskApplianceReqDTO riskApplianceReqDTO) {
        if (Objects.isNull(riskApplianceReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = riskApplianceMapper.selectRiskApplianceIsExist(riskApplianceReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        riskApplianceReqDTO.setId(TokenUtil.getUuId());
        riskApplianceReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = riskApplianceMapper.addRiskAppliance(riskApplianceReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void importRiskAppliance(MultipartFile file) {
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
            List<RiskApplianceReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                RiskApplianceReqDTO reqDTO = new RiskApplianceReqDTO();
                cells.getCell(0).setCellType(1);
                reqDTO.setName(cells.getCell(0) == null ? null : cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(1);
                reqDTO.setStatus(cells.getCell(1) == null ? null : ("启用".equals(cells.getCell(1).getStringCellValue()) ? 0 : 1));
                reqDTO.setId(TokenUtil.getUuId());
                reqDTO.setUserId(TokenUtil.getCurrentPersonNo());
                temp.add(reqDTO);
            }
            fileInputStream.close();
            riskApplianceMapper.importRiskAppliance(temp);
        } catch (IOException e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public void deleteRiskAppliance(RiskApplianceReqDTO riskApplianceReqDTO) {
        if (Objects.isNull(riskApplianceReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        riskApplianceReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        Integer result = riskApplianceMapper.deleteRiskAppliance(riskApplianceReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }
}
