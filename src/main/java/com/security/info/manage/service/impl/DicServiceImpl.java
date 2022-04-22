package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.DicDataReqDTO;
import com.security.info.manage.dto.req.DicReqDTO;
import com.security.info.manage.dto.res.DicDataResDTO;
import com.security.info.manage.dto.res.DicResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.DicMapper;
import com.security.info.manage.service.DicService;
import com.security.info.manage.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DicServiceImpl implements DicService {

    @Autowired
    private DicMapper dicMapper;

    @Override
    public Page<DicResDTO> getDics(PageReqDTO pageReqDTO, String name, String type, Integer isEnable) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return dicMapper.listDicType(pageReqDTO.of(), name, type, isEnable);
    }

    @Override
    public void addDic(DicReqDTO reqDTO) {
        if (Objects.isNull(reqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = dicMapper.selectDicTypeIsExist(reqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DIC_TYPE_EXIST);
        }
        reqDTO.setId(TokenUtil.getUuId());
        reqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = dicMapper.addDicType(reqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void updateDic(DicReqDTO reqDTO) {
        if (Objects.isNull(reqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = dicMapper.selectDicTypeIsExist(reqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DIC_TYPE_EXIST);
        }
        reqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = dicMapper.modifyDicType(reqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void deleteDic(DicReqDTO reqDTO) {
        if (Objects.isNull(reqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        reqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = dicMapper.deleteDicType(reqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public Page<DicDataResDTO> getDicDataByPage(PageReqDTO pageReqDTO, String type, String value, Integer isEnable) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return dicMapper.listDicData(pageReqDTO.of(), type, value, isEnable);
    }

    @Override
    public void addDicData(DicDataReqDTO reqDTO) {
        if (Objects.isNull(reqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = dicMapper.selectDicDataIsExist(reqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DIC_DATA_EXIST);
        }
        reqDTO.setId(TokenUtil.getUuId());
        reqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = dicMapper.addDicData(reqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void updateDicData(DicDataReqDTO reqDTO) {
        if (Objects.isNull(reqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = dicMapper.selectDicDataIsExist(reqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DIC_DATA_EXIST);
        }
        reqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = dicMapper.modifyDicData(reqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void deleteDicData(DicDataReqDTO reqDTO) {
        if (Objects.isNull(reqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        reqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = dicMapper.deleteDicData(reqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

}
