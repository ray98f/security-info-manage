package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.RegionReqDTO;
import com.security.info.manage.dto.req.RegionTypeReqDTO;
import com.security.info.manage.dto.res.RegionResDTO;
import com.security.info.manage.dto.res.RegionTypeResDTO;
import com.security.info.manage.dto.res.VxRegionResDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author frp
 */
public interface RegionService {

    Page<RegionTypeResDTO> listRegionType(PageReqDTO pageReqDTO);

    List<RegionTypeResDTO> listAllRegionType();

    void modifyRegionType(RegionTypeReqDTO regionTypeReqDTO);

    void addRegionType(RegionTypeReqDTO regionTypeReqDTO);

    void deleteRegionType(RegionTypeReqDTO regionTypeReqDTO);

    List<RegionResDTO> listRegion();

    RegionResDTO getRegionDetail(String id);

    List<RegionResDTO> listAllRegion();

    List<VxRegionResDTO> vxListAllRegion();

    List<RegionResDTO> vxGetRegionBody(String id);

    void modifyRegion(RegionReqDTO regionReqDTO);

    void addRegion(RegionReqDTO regionReqDTO);

    void deleteRegion(RegionReqDTO regionReqDTO);

}
