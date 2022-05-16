package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.req.ApplianceConfigReqDTO;
import com.security.info.manage.dto.req.ApplianceReqDTO;
import com.security.info.manage.dto.req.ApplianceTypeReqDTO;
import com.security.info.manage.dto.req.TrainDetailReqDTO;
import com.security.info.manage.dto.res.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface ApplianceMapper {

    Page<ApplianceResDTO> listAppliance(Page<ApplianceResDTO> page);

    ApplianceResDTO getApplianceDetail(String id);

    Integer selectApplianceIsExist(ApplianceReqDTO applianceReqDTO);

    Integer insertAppliance(ApplianceReqDTO applianceReqDTO);

    Integer modifyAppliance(ApplianceReqDTO applianceReqDTO);

    Integer deleteAppliance(ApplianceReqDTO applianceReqDTO);

    void importApplianceConfig(List<ApplianceConfigReqDTO> list);

    Page<ApplianceConfigResDTO> listApplianceConfig(Page<ApplianceConfigResDTO> page);

    Page<ApplianceConfigResDTO> vxListApplianceConfig(Page<ApplianceConfigResDTO> page, String userId);

    Integer vxConfirmApplianceConfig(String id, String userId);

    ApplianceConfigResDTO getApplianceConfigDetail(String id);

    void deleteApplianceConfig(String id, String userId);

    Integer changeAppliance(ApplianceConfigResDTO applianceConfigResDTO);

    List<ApplianceConfigResDTO> listExpiredAppliance();

    void taskStatus();

    void addApplianceWarning(List<ApplianceConfigResDTO> list, String userId);

    Page<ApplianceWarnResDTO> listApplianceWarn(Page<ApplianceWarnResDTO> page);

    Integer modifyApplianceWarn(String id);

    List<ApplianceConfigResDTO> userArchives(String id);
}
