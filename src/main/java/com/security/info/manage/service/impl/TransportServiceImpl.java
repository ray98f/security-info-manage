package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.TransportReqDTO;
import com.security.info.manage.dto.res.TransportResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.TransportMapper;
import com.security.info.manage.service.TransportService;
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
import org.springframework.transaction.annotation.Transactional;
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
public class TransportServiceImpl implements TransportService {


    @Autowired
    private TransportMapper transportMapper;

    @Override
    public Page<TransportResDTO> listTransport(PageReqDTO pageReqDTO, String name, Integer status) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return transportMapper.listTransport(pageReqDTO.of(), name, status);
    }

    @Override
    public List<TransportResDTO> listAllTransport() {
        return transportMapper.listAllTransport();
    }

    @Override
    public void modifyTransport(TransportReqDTO transportReqDTO) {
        if (Objects.isNull(transportReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = transportMapper.selectTransportIsExist(transportReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        transportReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = transportMapper.modifyTransport(transportReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void addTransport(TransportReqDTO transportReqDTO) {
        if (Objects.isNull(transportReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = transportMapper.selectTransportIsExist(transportReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        transportReqDTO.setId(TokenUtil.getUuId());
        transportReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = transportMapper.addTransport(transportReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importTransport(MultipartFile file) {
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
            List<TransportReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                TransportReqDTO reqDTO = new TransportReqDTO();
                cells.getCell(0).setCellType(1);
                reqDTO.setName(cells.getCell(0) == null ? null : cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(1);
                reqDTO.setStatus(cells.getCell(1) == null ? null : ("启用".equals(cells.getCell(1).getStringCellValue()) ? 0 : 1));
                reqDTO.setId(TokenUtil.getUuId());
                reqDTO.setUserId(TokenUtil.getCurrentPersonNo());
                temp.add(reqDTO);
            }
            fileInputStream.close();
            if (temp.size() > 0) {
                transportMapper.importTransport(temp);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public void deleteTransport(TransportReqDTO transportReqDTO) {
        if (Objects.isNull(transportReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        transportReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        Integer result = transportMapper.deleteTransport(transportReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }
}
