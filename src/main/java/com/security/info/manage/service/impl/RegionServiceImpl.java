package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.RegionReqDTO;
import com.security.info.manage.dto.req.RegionTypeReqDTO;
import com.security.info.manage.dto.res.RegionResDTO;
import com.security.info.manage.dto.res.RegionTypeResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.RegionMapper;
import com.security.info.manage.service.RegionService;
import com.security.info.manage.utils.TokenUtil;
import com.security.info.manage.utils.treeTool.RegionTreeToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class RegionServiceImpl implements RegionService {


    @Autowired
    private RegionMapper regionMapper;

    @Override
    public Page<RegionTypeResDTO> listRegionType(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return regionMapper.listRegionType(pageReqDTO.of());
    }

    @Override
    public List<RegionTypeResDTO> listAllRegionType() {
        return regionMapper.listAllRegionType();
    }

    @Override
    public void modifyRegionType(RegionTypeReqDTO transportReqDTO) {
        if (Objects.isNull(transportReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = regionMapper.selectRegionTypeIsExist(transportReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        transportReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = regionMapper.modifyRegionType(transportReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void addRegionType(RegionTypeReqDTO transportReqDTO) {
        if (Objects.isNull(transportReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = regionMapper.selectRegionTypeIsExist(transportReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        transportReqDTO.setId(TokenUtil.getUuId());
        transportReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = regionMapper.addRegionType(transportReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void deleteRegionType(RegionTypeReqDTO transportReqDTO) {
        if (Objects.isNull(transportReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        transportReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        Integer result = regionMapper.deleteRegionType(transportReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }
    @Override
    public Page<RegionResDTO> listRegion(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<RegionResDTO> page = regionMapper.listRegion(pageReqDTO.of());
        List<RegionResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
            for (RegionResDTO regionResDTO : list) {
                regionResDTO.setParentNames(regionMapper.selectParentNames(regionResDTO.getParentIds()));
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public RegionResDTO getRegionDetail(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        RegionResDTO regionResDTO = regionMapper.getRegionDetail(id);
        if (!Objects.isNull(regionResDTO)) {
            regionResDTO.setParentNames(regionMapper.selectParentNames(regionResDTO.getParentIds()));
        }
        return regionResDTO;
    }

    @Override
    public List<RegionResDTO> listAllRegion() {
        List<RegionResDTO> root = regionMapper.listAllRegionRoot();
        if (root != null && !root.isEmpty()) {
            for (RegionResDTO regionResDTO : root) {
                regionResDTO.setParentNames(regionMapper.selectParentNames(regionResDTO.getParentIds()));
            }
        }
        List<RegionResDTO> body = regionMapper.listAllRegionBody();
        if (body != null && !body.isEmpty()) {
            for (RegionResDTO regionResDTO : body) {
                regionResDTO.setParentNames(regionMapper.selectParentNames(regionResDTO.getParentIds()));
            }
        }
        RegionTreeToolUtils res = new RegionTreeToolUtils(root, body);
        return res.getTree();
    }

    @Override
    public void modifyRegion(RegionReqDTO regionReqDTO) {
        if (Objects.isNull(regionReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = regionMapper.selectRegionIsExist(regionReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        regionReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = regionMapper.modifyRegion(regionReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void addRegion(RegionReqDTO regionReqDTO) {
        if (Objects.isNull(regionReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = regionMapper.selectRegionIsExist(regionReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        regionReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = regionMapper.addRegion(regionReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }
}
