package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.NewUserReqDTO;
import com.security.info.manage.dto.req.PhysicalReqDTO;
import com.security.info.manage.dto.res.PhysicalResDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author frp
 */
public interface PhysicalService {

    Page<PhysicalResDTO> listPhysical(String sStartTime, String sEndTime, String eStartTime, String eEndTime, Integer type, PageReqDTO pageReqDTO);

    void addPhysical(PhysicalReqDTO physicalReqDTO);

    void modifyPhysical(PhysicalReqDTO physicalReqDTO);

    void deletePhysical(PhysicalReqDTO physicalReqDTO);

    List<NewUserReqDTO> addNewUser(MultipartFile file);
}
