package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.RiskInfoReqDTO;
import com.security.info.manage.dto.res.RiskInfoResDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author frp
 */
public interface RiskService {

    Page<RiskInfoResDTO> listRisk(Integer level, Integer type, String module, PageReqDTO pageReqDTO);

    RiskInfoResDTO getRiskDetail(String id);

    void modifyRisk(RiskInfoReqDTO riskInfoReqDTO);

    void addRisk(RiskInfoReqDTO riskInfoReqDTO);

    void deleteRisk(RiskInfoReqDTO riskInfoReqDTO);

    void importRisk(MultipartFile file, Integer type);

}
