package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.RiskInfoReqDTO;
import com.security.info.manage.dto.res.RiskInfoResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.RiskMapper;
import com.security.info.manage.service.RiskService;
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

import java.io.IOException;
import java.util.Objects;

import static com.security.info.manage.utils.Constants.XLS;
import static com.security.info.manage.utils.Constants.XLSX;

/**
 * @author frp
 */
@Service
@Slf4j
public class RiskServiceImpl implements RiskService {

    @Autowired
    private RiskMapper riskMapper;

    @Override
    public Page<RiskInfoResDTO> listRisk(Integer level, Integer type, String module, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return riskMapper.listRisk(pageReqDTO.of(), level, type, module);
    }

    @Override
    public RiskInfoResDTO getRiskDetail(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        return riskMapper.getRiskDetail(id);
    }

    @Override
    public void modifyRisk(RiskInfoReqDTO riskInfoReqDTO) {
        if (Objects.isNull(riskInfoReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = riskMapper.selectRiskIsExist(riskInfoReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        riskInfoReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = riskMapper.modifyRisk(riskInfoReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void addRisk(RiskInfoReqDTO riskInfoReqDTO) {
        if (Objects.isNull(riskInfoReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = riskMapper.selectRiskIsExist(riskInfoReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        riskInfoReqDTO.setId(TokenUtil.getUuId());
        riskInfoReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = riskMapper.addRisk(riskInfoReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void deleteRisk(RiskInfoReqDTO riskInfoReqDTO) {
        if (Objects.isNull(riskInfoReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        riskInfoReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = riskMapper.deleteRisk(riskInfoReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public void importRisk(MultipartFile file, Integer type) {
        // todo 风险分级导入
        if (type == 1) {
            try {
                Workbook workbook;
                String fileName = file.getOriginalFilename();
                if (Objects.requireNonNull(fileName).endsWith(XLS)) {
                    workbook = new HSSFWorkbook(file.getInputStream());
                } else if (fileName.endsWith(XLSX)) {
                    workbook = new XSSFWorkbook(file.getInputStream());
                } else {
                    throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
                }
                Sheet sheet = Objects.requireNonNull(workbook).getSheet("sheet1");
                int rows = sheet.getLastRowNum();
                if(rows == 0){
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type == 2) {

        }
    }

}
