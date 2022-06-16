package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.SafeExpectModifyReqDTO;
import com.security.info.manage.dto.req.SafeExpectReqDTO;
import com.security.info.manage.dto.res.SafeExpectResDTO;
import com.security.info.manage.dto.res.SafeExpectTemplateResDTO;
import com.security.info.manage.dto.res.SafeExpectUserResDTO;
import com.security.info.manage.entity.File;

import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
public interface SafeExpectService {

    Page<SafeExpectResDTO> listSafeExpect(PageReqDTO pageReqDTO, String startTime, String endTime, String name);

    Page<SafeExpectResDTO> vxListSafeExpect(PageReqDTO pageReqDTO);

    SafeExpectResDTO getSafeExpectVxDetail(String id);

    SafeExpectResDTO getSafeExpectDetail(String id);

    void addSafeExpect(SafeExpectReqDTO safeExpectReqDTO);

    List<SafeExpectTemplateResDTO> listSafeExpectTemplate(String riskId);

    void modifySafeExpect(SafeExpectModifyReqDTO safeExpectModifyReqDTO);

    void deleteSafeExpect(SafeExpectReqDTO safeExpectReqDTO);

    void cancelSafeExpect(SafeExpectReqDTO safeExpectReqDTO);

    Page<SafeExpectUserResDTO> listSafeExpectUser(String id, PageReqDTO pageReqDTO);

    void signSafeExpectUser(SafeExpectUserResDTO safeExpectUserResDTO);

    void vxSignSafeExpectUser(String id);

    Map<String, Object> exportSafeExpectData(String id);

    File exportSafeExpect(String id) throws Exception;
}
