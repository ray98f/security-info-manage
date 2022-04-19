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

    Page<RiskInfoResDTO> listRisk(Page<RiskInfoResDTO> page, Integer level, Integer type, String module);

    RiskInfoResDTO getRiskDetail(String id);

    Integer selectRiskIsExist(RiskInfoReqDTO riskInfoReqDTO);

    Integer modifyRisk(RiskInfoReqDTO riskInfoReqDTO);

    Integer addRisk(RiskInfoReqDTO riskInfoReqDTO);

    Integer deleteRisk(RiskInfoReqDTO riskInfoReqDTO);

    void importRisk(List<RiskInfoReqDTO> list);
}
