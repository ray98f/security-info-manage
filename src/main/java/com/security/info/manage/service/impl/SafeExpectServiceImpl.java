package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.SafeExpectModifyReqDTO;
import com.security.info.manage.dto.req.SafeExpectReqDTO;
import com.security.info.manage.dto.res.SafeExpectResDTO;
import com.security.info.manage.dto.res.SafeExpectUserResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.SafeExpectMapper;
import com.security.info.manage.service.SafeExpectService;
import com.security.info.manage.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class SafeExpectServiceImpl implements SafeExpectService {


    @Autowired
    private SafeExpectMapper safeExpectMapper;

    @Override
    public Page<SafeExpectResDTO> listSafeExpect(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return safeExpectMapper.listSafeExpect(pageReqDTO.of());
    }

    @Override
    public SafeExpectResDTO getSafeExpectDetail(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        SafeExpectResDTO safeExpectResDTO = safeExpectMapper.getSafeExpectDetail(id);
        if (!Objects.isNull(safeExpectResDTO)) {
            safeExpectResDTO.setSafeExpectInfoReqDTO(safeExpectMapper.getSafeExpectInfoDetail(id));
            safeExpectResDTO.setSafeExpectCollectionUnionReqDTO(safeExpectMapper.getSafeExpectCollectionUnionDetail(id));
        }
        return safeExpectResDTO;
    }

    @Override
    public void addSafeExpect(SafeExpectReqDTO safeExpectReqDTO) {
        if (Objects.isNull(safeExpectReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = safeExpectMapper.selectSafeExpectIsExist(safeExpectReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        safeExpectReqDTO.setId(TokenUtil.getUuId());
        safeExpectReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = safeExpectMapper.addSafeExpect(safeExpectReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
        if (result > 0) {
            result = safeExpectMapper.insertSafeExpectUser(safeExpectReqDTO.getId(), safeExpectReqDTO.getUserIds());
            if (result < 0) {
                throw new CommonException(ErrorCode.INSERT_ERROR);
            }
        }
    }

    @Override
    public void modifySafeExpect(SafeExpectModifyReqDTO safeExpectModifyReqDTO) {
        if (Objects.isNull(safeExpectModifyReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        if (!Objects.isNull(safeExpectModifyReqDTO.getSafeExpectInfo())) {
            Integer result = safeExpectMapper.modifySafeExpectInfo(safeExpectModifyReqDTO.getSafeExpectInfo());
            if (result < 0) {
                throw new CommonException(ErrorCode.UPDATE_ERROR);
            }
        }
        if (!Objects.isNull(safeExpectModifyReqDTO.getSafeExpectCollectionUnion())) {
            Integer result = safeExpectMapper.modifySafeExpectCollectionUnion(safeExpectModifyReqDTO.getSafeExpectCollectionUnion());
            if (result < 0) {
                throw new CommonException(ErrorCode.UPDATE_ERROR);
            }
        }
    }

    @Override
    public void deleteSafeExpect(SafeExpectReqDTO safeExpectReqDTO) {
        if (Objects.isNull(safeExpectReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        safeExpectReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = safeExpectMapper.deleteSafeExpect(safeExpectReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public Page<SafeExpectUserResDTO> listSafeExpectUser(String id, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return safeExpectMapper.listSafeExpectUser(pageReqDTO.of(), id);
    }

    @Override
    public void signSafeExpectUser(SafeExpectUserResDTO safeExpectUserResDTO) {
        if (Objects.isNull(safeExpectUserResDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = safeExpectMapper.signSafeExpectUser(safeExpectUserResDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

}
