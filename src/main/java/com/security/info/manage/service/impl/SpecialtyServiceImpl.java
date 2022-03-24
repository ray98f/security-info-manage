package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.ApplianceConfigReqDTO;
import com.security.info.manage.dto.req.PostReqDTO;
import com.security.info.manage.dto.req.PostUserReqDTO;
import com.security.info.manage.dto.req.SpecialtyReqDTO;
import com.security.info.manage.dto.res.PostResDTO;
import com.security.info.manage.dto.res.PostWarnResDTO;
import com.security.info.manage.dto.res.SpecialtyResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.PostMapper;
import com.security.info.manage.mapper.SpecialtyMapper;
import com.security.info.manage.service.PostService;
import com.security.info.manage.service.SpecialtyService;
import com.security.info.manage.utils.FileUtils;
import com.security.info.manage.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
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

/**
 * @author frp
 */
@Service
@Slf4j
public class SpecialtyServiceImpl implements SpecialtyService {

    @Autowired
    private SpecialtyMapper specialtyMapper;

    @Override
    public Page<SpecialtyResDTO> listSpecialty(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return specialtyMapper.listSpecialty(pageReqDTO.of());
    }

    @Override
    public List<SpecialtyResDTO> listAllSpecialty() {
        return specialtyMapper.listAllSpecialty();
    }

    @Override
    public void modifySpecialty(SpecialtyReqDTO specialtyReqDTO) {
        if (Objects.isNull(specialtyReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = specialtyMapper.selectSpecialtyIsExist(specialtyReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        specialtyReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = specialtyMapper.modifySpecialty(specialtyReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void addSpecialty(SpecialtyReqDTO specialtyReqDTO) {
        if (Objects.isNull(specialtyReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = specialtyMapper.selectSpecialtyIsExist(specialtyReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        specialtyReqDTO.setId(TokenUtil.getUuId());
        specialtyReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = specialtyMapper.addSpecialty(specialtyReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void importSpecialty(MultipartFile file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(FileUtils.transferToFile(file));
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            List<SpecialtyReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                SpecialtyReqDTO reqDTO = new SpecialtyReqDTO();
                cells.getCell(0).setCellType(1);
                reqDTO.setName(cells.getCell(0) == null ? null : cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(1);
                reqDTO.setStatus(cells.getCell(1) == null ? null : ("启用".equals(cells.getCell(1).getStringCellValue()) ? 0 : 1));
                reqDTO.setId(TokenUtil.getUuId());
                cells.getCell(2).setCellType(1);
                reqDTO.setSort(cells.getCell(2) == null ? null : Integer.valueOf(cells.getCell(2).getStringCellValue()));
                reqDTO.setId(TokenUtil.getUuId());
                reqDTO.setUserId(TokenUtil.getCurrentPersonNo());
                temp.add(reqDTO);
            }
            fileInputStream.close();
            specialtyMapper.importSpecialty(temp);
        } catch (IOException e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public void deleteSpecialty(SpecialtyReqDTO specialtyReqDTO) {
        if (Objects.isNull(specialtyReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        specialtyReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        Integer result = specialtyMapper.deleteSpecialty(specialtyReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }
}
