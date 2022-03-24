package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.RiskApplianceReqDTO;
import com.security.info.manage.dto.res.RiskApplianceResDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author frp
 */
public interface RiskApplianceService {

    Page<RiskApplianceResDTO> listRiskAppliance(PageReqDTO pageReqDTO);

    List<RiskApplianceResDTO> listAllRiskAppliance();

    void modifyRiskAppliance(RiskApplianceReqDTO riskApplianceReqDTO);

    void addRiskAppliance(RiskApplianceReqDTO riskApplianceReqDTO);

    void importRiskAppliance(MultipartFile file);

    void deleteRiskAppliance(RiskApplianceReqDTO riskApplianceReqDTO);

}
