package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.RiskInfoReqDTO;
import com.security.info.manage.dto.res.RiskInfoResDTO;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author frp
 */
public interface RiskService {

    Page<RiskInfoResDTO> listRisk(Integer level, Integer type, String module, PageReqDTO pageReqDTO);

    RiskInfoResDTO getRiskDetail(String id);

    void modifyRisk(RiskInfoReqDTO riskInfoReqDTO);

    void addRisk(RiskInfoReqDTO riskInfoReqDTO);

    void deleteRisk(List<String> ids);

    void importRisk(MultipartFile file, Integer type);

    void exportRisk(HttpServletResponse response, Integer type);

}
