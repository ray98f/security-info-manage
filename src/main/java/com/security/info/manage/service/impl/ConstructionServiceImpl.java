package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
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
import com.security.info.manage.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

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
    public void importConstruction(MultipartFile file) {
        // todo 施工作业导入
    }
}
