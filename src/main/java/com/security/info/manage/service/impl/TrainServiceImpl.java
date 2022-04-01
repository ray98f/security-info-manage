package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.*;
import com.security.info.manage.dto.res.PostResDTO;
import com.security.info.manage.dto.res.TrainDetailResDTO;
import com.security.info.manage.dto.res.TrainResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.PostMapper;
import com.security.info.manage.mapper.TrainMapper;
import com.security.info.manage.service.PostService;
import com.security.info.manage.service.TrainService;
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
public class TrainServiceImpl implements TrainService {

    @Autowired
    private TrainMapper trainMapper;

    @Override
    public Page<TrainResDTO> listTrain(String startTime, String endTime, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return trainMapper.listTrain(pageReqDTO.of(), startTime, endTime);
    }

    @Override
    public TrainResDTO getTrainDetail(String id) {
        return trainMapper.getTrainDetail(id);
    }

    @Override
    public Page<TrainDetailResDTO> listTrainUserDetail(String id, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return trainMapper.listTrainUserDetail(pageReqDTO.of(), id);
    }

    @Override
    public void modifyTrain(TrainReqDTO trainReqDTO) {
        if (Objects.isNull(trainReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        trainReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        Integer result = trainMapper.modifyTrain(trainReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void addTrain(TrainReqDTO trainReqDTO) {
        if (Objects.isNull(trainReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        trainReqDTO.setId(TokenUtil.getUuId());
        trainReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        Integer result = trainMapper.addTrain(trainReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void deleteTrain(TrainReqDTO trainReqDTO) {
        if (Objects.isNull(trainReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        trainReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        Integer result = trainMapper.deleteTrain(trainReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importTrainDetail(MultipartFile file, String trainId) {
        try {
            FileInputStream fileInputStream = new FileInputStream(FileUtils.transferToFile(file));
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            List<TrainDetailReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                TrainDetailReqDTO reqDTO = new TrainDetailReqDTO();
                cells.getCell(0).setCellType(1);
                reqDTO.setUserNo(cells.getCell(0) == null ? null : cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(1);
                reqDTO.setUserName(cells.getCell(1) == null ? null : cells.getCell(1).getStringCellValue());
                reqDTO.setTrainTime(cells.getCell(2) == null ? null : cells.getCell(2).getDateCellValue());
                cells.getCell(3).setCellType(1);
                reqDTO.setTrainCondition(cells.getCell(3) == null ? null : cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(1);
                reqDTO.setTrainScore(cells.getCell(4) == null ? null : cells.getCell(4).getStringCellValue());
                reqDTO.setId(TokenUtil.getUuId());
                reqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
                temp.add(reqDTO);
            }
            fileInputStream.close();
            trainMapper.importTrainDetail(temp, trainId);
        } catch (IOException e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public List<TrainDetailResDTO> userArchives(String id) {
        return trainMapper.userArchives(id);
    }

}
