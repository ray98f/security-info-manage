package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.ApplianceConfigReqDTO;
import com.security.info.manage.dto.req.ApplianceReqDTO;
import com.security.info.manage.dto.req.TroubleshootReqDTO;
import com.security.info.manage.dto.req.TroubleshootTypeReqDTO;
import com.security.info.manage.dto.res.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface TroubleshootMapper {

    Page<TroubleshootTypeResDTO> listTroubleshootType(Page<TroubleshootTypeResDTO> page, String name);

    List<TroubleshootTypeResDTO> listAllTroubleshootType();

    TroubleshootTypeResDTO getTroubleshootTypeDetail(String id);

    Integer selectTroubleshootTypeIsExist(TroubleshootTypeReqDTO troubleshootTypeReqDTO);

    Integer insertTroubleshootType(TroubleshootTypeReqDTO troubleshootTypeReqDTO);

    Integer modifyTroubleshootType(TroubleshootTypeReqDTO troubleshootTypeReqDTO);

    Integer deleteTroubleshootType(TroubleshootTypeReqDTO troubleshootTypeReqDTO);

    Page<TroubleshootResDTO> listTroubleshoot(Page<TroubleshootResDTO> page, String name, String typeId);

    TroubleshootResDTO getTroubleshootDetail(String id);

    Integer selectTroubleshootIsExist(TroubleshootReqDTO troubleshootReqDTO);

    Integer insertTroubleshoot(TroubleshootReqDTO troubleshootReqDTO);

    Integer modifyTroubleshoot(TroubleshootReqDTO troubleshootReqDTO);

    Integer deleteTroubleshoot(TroubleshootReqDTO troubleshootReqDTO);
}
