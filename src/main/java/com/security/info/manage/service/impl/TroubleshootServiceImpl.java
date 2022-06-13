package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.*;
import com.security.info.manage.dto.res.*;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.ApplianceMapper;
import com.security.info.manage.mapper.TroubleshootMapper;
import com.security.info.manage.service.ApplianceService;
import com.security.info.manage.service.MsgService;
import com.security.info.manage.service.TroubleshootService;
import com.security.info.manage.utils.DateUtils;
import com.security.info.manage.utils.ExcelPortUtil;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
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
public class TroubleshootServiceImpl implements TroubleshootService {

    @Autowired
    private TroubleshootMapper troubleshootMapper;

    @Override
    public Page<TroubleshootTypeResDTO> listTroubleshootType(String name, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return troubleshootMapper.listTroubleshootType(pageReqDTO.of(), name);
    }

    @Override
    public List<TroubleshootTypeResDTO> listAllTroubleshootType() {
        return troubleshootMapper.listAllTroubleshootType();
    }

    @Override
    public TroubleshootTypeResDTO getTroubleshootTypeDetail(String id) {
        return troubleshootMapper.getTroubleshootTypeDetail(id);
    }

    @Override
    public void insertTroubleshootType(TroubleshootTypeReqDTO troubleshootTypeReqDTO) {
        if (Objects.isNull(troubleshootTypeReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = troubleshootMapper.selectTroubleshootTypeIsExist(troubleshootTypeReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        troubleshootTypeReqDTO.setId(TokenUtil.getUuId());
        troubleshootTypeReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = troubleshootMapper.insertTroubleshootType(troubleshootTypeReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void modifyTroubleshootType(TroubleshootTypeReqDTO troubleshootTypeReqDTO) {
        if (Objects.isNull(troubleshootTypeReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = troubleshootMapper.selectTroubleshootTypeIsExist(troubleshootTypeReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        troubleshootTypeReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = troubleshootMapper.modifyTroubleshootType(troubleshootTypeReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void deleteTroubleshootType(TroubleshootTypeReqDTO troubleshootTypeReqDTO) {
        if (Objects.isNull(troubleshootTypeReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        troubleshootTypeReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = troubleshootMapper.deleteTroubleshootType(troubleshootTypeReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public Page<TroubleshootResDTO> listTroubleshoot(String name, String typeId, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return troubleshootMapper.listTroubleshoot(pageReqDTO.of(), name, typeId);
    }

    @Override
    public TroubleshootResDTO getTroubleshootDetail(String id) {
        return troubleshootMapper.getTroubleshootDetail(id);
    }

    @Override
    public void insertTroubleshoot(TroubleshootReqDTO troubleshootReqDTO) {
        if (Objects.isNull(troubleshootReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = troubleshootMapper.selectTroubleshootIsExist(troubleshootReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        troubleshootReqDTO.setId(TokenUtil.getUuId());
        troubleshootReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = troubleshootMapper.insertTroubleshoot(troubleshootReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void modifyTroubleshoot(TroubleshootReqDTO troubleshootReqDTO) {
        if (Objects.isNull(troubleshootReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = troubleshootMapper.selectTroubleshootIsExist(troubleshootReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        troubleshootReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = troubleshootMapper.modifyTroubleshoot(troubleshootReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void deleteTroubleshoot(TroubleshootReqDTO troubleshootReqDTO) {
        if (Objects.isNull(troubleshootReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        troubleshootReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = troubleshootMapper.deleteTroubleshoot(troubleshootReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

}
