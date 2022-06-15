package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.ApplianceConfigReqDTO;
import com.security.info.manage.dto.req.ApplianceReqDTO;
import com.security.info.manage.dto.req.ApplianceTypeReqDTO;
import com.security.info.manage.dto.res.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author frp
 */
public interface ApplianceService {

    Page<ApplianceResDTO> listAppliance(String name, PageReqDTO pageReqDTO);

    ApplianceResDTO getApplianceDetail(String id);

    void insertAppliance(ApplianceReqDTO applianceReqDTO);

    void modifyAppliance(ApplianceReqDTO applianceReqDTO);

    void deleteAppliance(ApplianceReqDTO applianceReqDTO);

    void importApplianceConfig(MultipartFile file);

    void exportApplianceConfig(HttpServletResponse response);

    Page<ApplianceConfigResDTO> listApplianceConfig(PageReqDTO pageReqDTO, String name, Integer status, String startTime, String endTime);

    Page<ApplianceConfigResDTO> vxListApplianceConfig(PageReqDTO pageReqDTO);

    void vxConfirmApplianceConfig(String id);

    ApplianceConfigResDTO getApplianceConfigDetail(String id);

    void deleteApplianceConfig(String id);

    void changeAppliance(ApplianceConfigReqDTO applianceConfigReqDTO);

    Page<ApplianceWarnResDTO> listApplianceWarn(PageReqDTO pageReqDTO);

    void handleApplianceWarn(String id);

    List<ApplianceConfigResDTO> userArchives(String id);

}
