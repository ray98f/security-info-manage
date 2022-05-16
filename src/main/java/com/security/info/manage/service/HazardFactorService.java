package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.PostHazardFactorReqDTO;
import com.security.info.manage.dto.res.DeptTreeResDTO;
import com.security.info.manage.dto.res.PostHazardFactorResDTO;
import com.security.info.manage.entity.HazardFactor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author frp
 */
public interface HazardFactorService {

    Page<HazardFactor> listHazardFactor(PageReqDTO pageReqDTO);

    List<HazardFactor> listAllHazardFactor();

    void importHazardFactor(MultipartFile file);

    void addHazardFactor(HazardFactor hazardFactor);

    void modifyHazardFactor(HazardFactor hazardFactor);

    void deleteHazardFactor(HazardFactor hazardFactor);

    void addPostHazardFactor(PostHazardFactorReqDTO postHazardFactorReqDTO);

    void modifyPostHazardFactor(PostHazardFactorReqDTO postHazardFactorReqDTO);

    void deletePostHazardFactor(PostHazardFactorReqDTO postHazardFactorReqDTO);

    Page<PostHazardFactorResDTO> listPostHazardFactor(PageReqDTO pageReqDTO);
}
