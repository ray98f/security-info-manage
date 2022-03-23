package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.req.ApplianceConfigReqDTO;
import com.security.info.manage.dto.req.ApplianceTypeReqDTO;
import com.security.info.manage.dto.req.TrainDetailReqDTO;
import com.security.info.manage.dto.res.ApplianceConfigResDTO;
import com.security.info.manage.dto.res.ApplianceTypeTreeResDTO;
import com.security.info.manage.dto.res.DeptTreeResDTO;
import com.security.info.manage.dto.res.VxDeptResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface ApplianceMapper {

    List<ApplianceTypeTreeResDTO> getTypeRoot(Integer status);

    List<ApplianceTypeTreeResDTO> getTypeBody(Integer status);

    ApplianceTypeTreeResDTO getTypeDetail(String id);

    Integer selectHadChild(String id);

    Integer addType(ApplianceTypeReqDTO applianceTypeReqDTO);

    Integer modifyType(ApplianceTypeReqDTO applianceTypeReqDTO);

    Integer deleteType(ApplianceTypeReqDTO applianceTypeReqDTO);

    void importAppliance(List<ApplianceConfigReqDTO> list);

    Page<ApplianceConfigResDTO> listApplianceConfig(Page<ApplianceConfigResDTO> page);

    Integer changeAppliance(ApplianceConfigReqDTO applianceConfigReqDTO);

    void taskStatus();
}
