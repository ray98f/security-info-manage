package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.req.RiskApplianceReqDTO;
import com.security.info.manage.dto.req.SpecialtyReqDTO;
import com.security.info.manage.dto.res.PostResDTO;
import com.security.info.manage.dto.res.RiskApplianceResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface RiskApplianceMapper {

    Page<RiskApplianceResDTO> listRiskAppliance(Page<PostResDTO> page, String name, Integer status);

    List<RiskApplianceResDTO> listAllRiskAppliance();

    Integer selectRiskApplianceIsExist(RiskApplianceReqDTO riskApplianceReqDTO);

    Integer modifyRiskAppliance(RiskApplianceReqDTO riskApplianceReqDTO);

    Integer addRiskAppliance(RiskApplianceReqDTO riskApplianceReqDTO);

    void importRiskAppliance(List<RiskApplianceReqDTO> list);

    Integer deleteRiskAppliance(RiskApplianceReqDTO riskApplianceReqDTO);

}
