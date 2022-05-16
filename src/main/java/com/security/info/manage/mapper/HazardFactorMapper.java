package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.PostHazardFactorReqDTO;
import com.security.info.manage.dto.res.DeptTreeResDTO;
import com.security.info.manage.dto.res.PostHazardFactorResDTO;
import com.security.info.manage.dto.res.VxDeptResDTO;
import com.security.info.manage.entity.HazardFactor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface HazardFactorMapper {

    Page<HazardFactor> listHazardFactor(Page<HazardFactor> page);

    List<HazardFactor> listAllHazardFactor();

    void importHazardFactor(List<HazardFactor> list);

    Integer addHazardFactor(HazardFactor hazardFactor);

    Integer modifyHazardFactor(HazardFactor hazardFactor);

    Integer deleteHazardFactor(HazardFactor hazardFactor);

    Integer selectPostHazardFactorIsExist(String postId);

    Integer addPostHazardFactor(PostHazardFactorReqDTO postHazardFactorReqDTO);

    Integer modifyPostHazardFactor(PostHazardFactorReqDTO postHazardFactorReqDTO);

    Integer deletePostHazardFactor(PostHazardFactorReqDTO postHazardFactorReqDTO);

    Page<PostHazardFactorResDTO> listPostHazardFactor(Page<PostHazardFactorResDTO> page);

}
