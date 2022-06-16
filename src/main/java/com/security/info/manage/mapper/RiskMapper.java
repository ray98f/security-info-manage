package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.req.RiskInfoReqDTO;
import com.security.info.manage.dto.res.RiskInfoResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface RiskMapper {

    Page<RiskInfoResDTO> listRisk(Page<RiskInfoResDTO> page, Integer level, Integer type, String module, String responsibilityDept, String responsibilityCenter, String responsibilityUser);

    List<RiskInfoResDTO> exportRisk(Integer type);

    RiskInfoResDTO getRiskDetail(String id);

    Integer selectRiskIsExist(RiskInfoReqDTO riskInfoReqDTO);

    Integer modifyRisk(RiskInfoReqDTO riskInfoReqDTO);

    Integer verifyRisk(String id, Integer status, String createBy);

    Integer addRisk(RiskInfoReqDTO riskInfoReqDTO);

    Integer deleteRisk(List<String> ids, String userId);

    void importRisk(List<RiskInfoReqDTO> list);
}
