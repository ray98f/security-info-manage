package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.PostReqDTO;
import com.security.info.manage.dto.req.PostUserReqDTO;
import com.security.info.manage.dto.req.SpecialtyReqDTO;
import com.security.info.manage.dto.res.PostResDTO;
import com.security.info.manage.dto.res.PostWarnResDTO;
import com.security.info.manage.dto.res.SpecialtyResDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author frp
 */
public interface SpecialtyService {

    Page<SpecialtyResDTO> listSpecialty(PageReqDTO pageReqDTO);

    List<SpecialtyResDTO> listAllSpecialty();

    void modifySpecialty(SpecialtyReqDTO specialtyReqDTO);

    void addSpecialty(SpecialtyReqDTO specialtyReqDTO);

    void importSpecialty(MultipartFile file);

    void deleteSpecialty(SpecialtyReqDTO specialtyReqDTO);

}
