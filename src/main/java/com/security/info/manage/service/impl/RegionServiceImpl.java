package com.security.info.manage.service.impl;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.VxAccessToken;
import com.security.info.manage.dto.req.RegionReqDTO;
import com.security.info.manage.dto.req.RegionTypeReqDTO;
import com.security.info.manage.dto.res.RegionResDTO;
import com.security.info.manage.dto.res.RegionTypeResDTO;
import com.security.info.manage.dto.res.VxDeptResDTO;
import com.security.info.manage.dto.res.VxRegionResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.RegionMapper;
import com.security.info.manage.service.RegionService;
import com.security.info.manage.utils.Constants;
import com.security.info.manage.utils.TokenUtil;
import com.security.info.manage.utils.VxApiUtils;
import com.security.info.manage.utils.treeTool.RegionTreeToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.awt.*;
import java.io.BufferedInputStream;
import java.net.URI;
import java.util.*;
import java.util.List;

/**
 * @author frp
 */
@Service
@Slf4j
public class RegionServiceImpl implements RegionService {

    @Value("${vx-business.jumppage}")
    private String jumppage;

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private RestTemplate restTemplate;

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
    public List<RegionResDTO> listRegion() {
        List<RegionResDTO> root = regionMapper.listRegionRoot();
//        if (root != null && !root.isEmpty()) {
//            for (RegionResDTO regionResDTO : root) {
//                String name = regionMapper.selectParentNames(regionResDTO.getParentIds());
//                regionResDTO.setParentNames((name == null || "".equals(name) ? "/" + regionResDTO.getName() : name + "," + regionResDTO.getName()).replaceAll(",", "/"));
//                regionResDTO.setQrCode(null);
//            }
//        }
        List<RegionResDTO> child = regionMapper.listRegionBody();
//        if (child != null && !child.isEmpty()) {
//            for (RegionResDTO regionResDTO : child) {
//                String name = regionMapper.selectParentNames(regionResDTO.getParentIds());
//                regionResDTO.setParentNames((name == null || "".equals(name) ? "/" + regionResDTO.getName() : name + "," + regionResDTO.getName()).replaceAll(",", "/"));
//                regionResDTO.setQrCode(QrCodeUtil.generateAsBase64(jumppage +
//                        "?page=pages/database/detail1" +
//                        "&id="+ regionResDTO.getParentId() +
//                        "&ids=" + regionResDTO.getId() +
//                        "&name=" + name, initQrConfig(), "png"));
//            }
//        }
        RegionTreeToolUtils res = new RegionTreeToolUtils(root, child);
        return res.getTree();
    }

    @Override
    public RegionResDTO getRegionDetail(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        RegionResDTO regionResDTO = regionMapper.getRegionDetail(id);
        if (!Objects.isNull(regionResDTO)) {
            String name = regionMapper.selectParentNames(regionResDTO.getParentIds());
            regionResDTO.setParentNames(name == null || "".equals(name) ? regionResDTO.getName() : name + "," + regionResDTO.getName());
        }
        return regionResDTO;
    }

    @Override
    public RegionResDTO getRegionQr(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        RegionResDTO regionResDTO = regionMapper.getRegionDetail(id);
        if (!Objects.isNull(regionResDTO)) {
            String name = regionMapper.selectParentNames(regionResDTO.getParentIds());
            regionResDTO.setQrCode(QrCodeUtil.generateAsBase64(jumppage +
                    "?page=pages/database/detail1" +
                    "&id="+ regionResDTO.getParentId() +
                    "&ids=" + regionResDTO.getId() +
                    "&name=" + name, initQrConfig(), "png"));
        }
        return regionResDTO;
    }

    @Override
    public List<RegionResDTO> listAllRegion() {
        List<RegionResDTO> root = regionMapper.listAllRegionRoot();
//        if (root != null && !root.isEmpty()) {
//            for (RegionResDTO regionResDTO : root) {
//                String name = regionMapper.selectParentNames(regionResDTO.getParentIds());
//                regionResDTO.setParentNames(name == null || "".equals(name) ? regionResDTO.getName() : name + "," + regionResDTO.getName());
//            }
//        }
        List<RegionResDTO> body = regionMapper.listAllRegionBody();
//        if (body != null && !body.isEmpty()) {
//            for (RegionResDTO regionResDTO : body) {
//                String name = regionMapper.selectParentNames(regionResDTO.getParentIds());
//                regionResDTO.setParentNames(name == null || "".equals(name) ? regionResDTO.getName() : name + "," + regionResDTO.getName());
//            }
//        }
        RegionTreeToolUtils res = new RegionTreeToolUtils(root, body);
        return res.getTree();
    }

    @Override
    public List<VxRegionResDTO> vxListAllRegion() {
        List<RegionTypeResDTO> list = regionMapper.listFirstRegionType();
        List<VxRegionResDTO> res = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (RegionTypeResDTO regionTypeResDTO : list) {
                VxRegionResDTO vxRegionResDTO = new VxRegionResDTO();
                vxRegionResDTO.setType(regionTypeResDTO);
                vxRegionResDTO.setRegions(regionMapper.selectRegionRootByType(regionTypeResDTO));
                res.add(vxRegionResDTO);
            }
        }
        return res;
    }

    @Override
    public RegionResDTO vxGetRegionBody(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        RegionResDTO regionResDTO = regionMapper.getRegionDetail(id);
        if (!Objects.isNull(regionResDTO)) {
            regionResDTO.setChildren(regionMapper.selectRegionBodyByType(id));
        }
        return regionResDTO;
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

    @Override
    public void deleteRegion(RegionReqDTO regionReqDTO) {
        if (Objects.isNull(regionReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        regionReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        Integer result = regionMapper.deleteRegion(regionReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    private static QrConfig initQrConfig() {
        QrConfig config = new QrConfig(300, 300);
        // 设置边距，既二维码和背景之间的边距
        config.setMargin(2);
        // 设置前景色，既二维码颜色（青色）
        config.setForeColor(Color.BLACK.getRGB());
        // 设置背景色（灰色）
        config.setBackColor(Color.WHITE.getRGB());
        return config;
    }
}
