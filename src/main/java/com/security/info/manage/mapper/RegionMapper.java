package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.req.RegionReqDTO;
import com.security.info.manage.dto.req.RegionTypeReqDTO;
import com.security.info.manage.dto.res.PostResDTO;
import com.security.info.manage.dto.res.RegionResDTO;
import com.security.info.manage.dto.res.RegionTypeResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface RegionMapper {

    Page<RegionTypeResDTO> listRegionType(Page<PostResDTO> page);

    List<RegionTypeResDTO> listAllRegionType();

    Integer selectRegionTypeIsExist(RegionTypeReqDTO transportReqDTO);

    Integer modifyRegionType(RegionTypeReqDTO transportReqDTO);

    Integer addRegionType(RegionTypeReqDTO transportReqDTO);

    Integer deleteRegionType(RegionTypeReqDTO transportReqDTO);

    Page<RegionResDTO> listRegion(Page<PostResDTO> page);

    List<RegionResDTO> listAllRegionRoot();

    List<RegionResDTO> listAllRegionBody();

    RegionResDTO getRegionDetail(String id);

    String selectParentNames(String parentIds);

    Integer selectRegionIsExist(RegionReqDTO regionReqDTO);

    Integer modifyRegion(RegionReqDTO regionReqDTO);

    Integer addRegion(RegionReqDTO regionReqDTO);

}
