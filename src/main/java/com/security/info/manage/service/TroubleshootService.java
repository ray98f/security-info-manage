package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.ApplianceConfigReqDTO;
import com.security.info.manage.dto.req.ApplianceReqDTO;
import com.security.info.manage.dto.req.TroubleshootReqDTO;
import com.security.info.manage.dto.req.TroubleshootTypeReqDTO;
import com.security.info.manage.dto.res.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author frp
 */
public interface TroubleshootService {

    Page<TroubleshootTypeResDTO> listTroubleshootType(String name, PageReqDTO pageReqDTO);

    List<TroubleshootTypeResDTO> listAllTroubleshootType();

    TroubleshootTypeResDTO getTroubleshootTypeDetail(String id);

    void insertTroubleshootType(TroubleshootTypeReqDTO troubleshootTypeReqDTO);

    void modifyTroubleshootType(TroubleshootTypeReqDTO troubleshootTypeReqDTO);

    void deleteTroubleshootType(TroubleshootTypeReqDTO troubleshootTypeReqDTO);

    Page<TroubleshootResDTO> listTroubleshoot(String name, String typeId, PageReqDTO pageReqDTO);

    TroubleshootResDTO getTroubleshootDetail(String id);

    void insertTroubleshoot(TroubleshootReqDTO troubleshootReqDTO);

    void modifyTroubleshoot(TroubleshootReqDTO troubleshootReqDTO);

    void deleteTroubleshoot(TroubleshootReqDTO troubleshootReqDTO);

}
