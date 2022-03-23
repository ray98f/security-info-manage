package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.ApplianceConfigReqDTO;
import com.security.info.manage.dto.req.ApplianceTypeReqDTO;
import com.security.info.manage.dto.res.ApplianceConfigResDTO;
import com.security.info.manage.dto.res.ApplianceTypeTreeResDTO;
import com.security.info.manage.dto.res.DeptTreeResDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author frp
 */
public interface ApplianceService {

    List<ApplianceTypeTreeResDTO> listTypeTree(Integer status);

    ApplianceTypeTreeResDTO getTypeDetail(String id);

    void addType(ApplianceTypeReqDTO applianceTypeReqDTO);

    void modifyType(ApplianceTypeReqDTO applianceTypeReqDTO);

    void deleteType(ApplianceTypeReqDTO applianceTypeReqDTO);

    void importAppliance(MultipartFile file);

    Page<ApplianceConfigResDTO> listApplianceConfig(PageReqDTO pageReqDTO);

    void changeAppliance(ApplianceConfigReqDTO applianceConfigReqDTO);

}
