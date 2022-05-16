package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.ApplianceConfigReqDTO;
import com.security.info.manage.dto.req.PostHazardFactorReqDTO;
import com.security.info.manage.dto.res.PhysicalResDTO;
import com.security.info.manage.dto.res.PostHazardFactorResDTO;
import com.security.info.manage.entity.HazardFactor;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.DeptMapper;
import com.security.info.manage.mapper.HazardFactorMapper;
import com.security.info.manage.service.HazardFactorService;
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
public class HazardFactorServiceImpl implements HazardFactorService {

    @Autowired
    private HazardFactorMapper hazardFactorMapper;

    @Override
    public Page<HazardFactor> listHazardFactor(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return hazardFactorMapper.listHazardFactor(pageReqDTO.of());
    }

    @Override
    public List<HazardFactor> listAllHazardFactor() {
        return hazardFactorMapper.listAllHazardFactor();
    }

    @Override
    public void importHazardFactor(MultipartFile file) {
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
            List<HazardFactor> temp = new ArrayList<>();
            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                HazardFactor reqDTO = new HazardFactor();
                cells.getCell(0).setCellType(1);
                reqDTO.setName(cells.getCell(0) == null ? null : cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(1);
                reqDTO.setBeforeCheckProj(cells.getCell(1) == null ? null : cells.getCell(1).getStringCellValue());
                cells.getCell(2).setCellType(1);
                reqDTO.setOnCheckProj(cells.getCell(2) == null ? null : cells.getCell(2).getStringCellValue());
                cells.getCell(3).setCellType(1);
                reqDTO.setAfterCheckProj(cells.getCell(3) == null ? null : cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(1);
                reqDTO.setHealthCheck(cells.getCell(4) == null ? null : cells.getCell(4).getStringCellValue());
                cells.getCell(5).setCellType(1);
                reqDTO.setAfterCheck(cells.getCell(5) == null ? null : cells.getCell(5).getStringCellValue());
                cells.getCell(6).setCellType(1);
                reqDTO.setCheckCycle(cells.getCell(6) == null ? null : cells.getCell(6).getStringCellValue());
                cells.getCell(7).setCellType(1);
                reqDTO.setContraindication(cells.getCell(7) == null ? null : cells.getCell(7).getStringCellValue());
                reqDTO.setId(TokenUtil.getUuId());
                reqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
                temp.add(reqDTO);
            }
            fileInputStream.close();
            if (temp.size() > 0) {
                hazardFactorMapper.importHazardFactor(temp);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public void addHazardFactor(HazardFactor hazardFactor) {
        if (Objects.isNull(hazardFactor)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        hazardFactor.setId(TokenUtil.getUuId());
        hazardFactor.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = hazardFactorMapper.addHazardFactor(hazardFactor);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void modifyHazardFactor(HazardFactor hazardFactor) {
        if (Objects.isNull(hazardFactor)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        hazardFactor.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = hazardFactorMapper.modifyHazardFactor(hazardFactor);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void deleteHazardFactor(HazardFactor hazardFactor) {
        if (Objects.isNull(hazardFactor)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        hazardFactor.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = hazardFactorMapper.deleteHazardFactor(hazardFactor);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public void addPostHazardFactor(PostHazardFactorReqDTO postHazardFactorReqDTO) {
        if (Objects.isNull(postHazardFactorReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = hazardFactorMapper.selectPostHazardFactorIsExist(postHazardFactorReqDTO.getPostId());
        if (result > 0) {
            throw new CommonException(ErrorCode.POST_HAZARD_FACTOR_EXIST);
        }
        if (postHazardFactorReqDTO.getHazardFactorIds().size() > 0) {
            result = hazardFactorMapper.addPostHazardFactor(postHazardFactorReqDTO);
            if (result < 0) {
                throw new CommonException(ErrorCode.INSERT_ERROR);
            }
        }
    }

    @Override
    public void modifyPostHazardFactor(PostHazardFactorReqDTO postHazardFactorReqDTO) {
        if (Objects.isNull(postHazardFactorReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = hazardFactorMapper.selectPostHazardFactorIsExist(postHazardFactorReqDTO.getPostId());
        if (result > 0) {
            throw new CommonException(ErrorCode.POST_HAZARD_FACTOR_EXIST);
        }
        result = hazardFactorMapper.modifyPostHazardFactor(postHazardFactorReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void deletePostHazardFactor(PostHazardFactorReqDTO postHazardFactorReqDTO) {
        if (Objects.isNull(postHazardFactorReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = hazardFactorMapper.deletePostHazardFactor(postHazardFactorReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public Page<PostHazardFactorResDTO> listPostHazardFactor(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return hazardFactorMapper.listPostHazardFactor(pageReqDTO.of());
    }

}
