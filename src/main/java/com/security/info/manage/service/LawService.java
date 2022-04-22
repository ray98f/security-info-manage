package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.LawCatalogReqDTO;
import com.security.info.manage.dto.req.LawCatalogUserRoleReqDTO;
import com.security.info.manage.dto.req.LawReqDTO;
import com.security.info.manage.dto.res.LawCatalogResDTO;
import com.security.info.manage.dto.res.LawResDTO;

import java.util.List;

/**
 * @author frp
 */
public interface LawService {

    Page<LawCatalogResDTO> listLawCatalog(PageReqDTO pageReqDTO);

    List<LawCatalogResDTO> listAllLawCatalog(String deptId);

    void modifyLawCatalog(LawCatalogReqDTO trainReqDTO);

    void addLawCatalog(LawCatalogReqDTO trainReqDTO);

    void addLawCatalogRole(LawCatalogUserRoleReqDTO lawCatalogUserRoleReqDTO);

    Page<LawResDTO> listLaw(String catalogId, String name, PageReqDTO pageReqDTO);

    void addLaw(LawReqDTO lawReqDTO);

    void deleteLaw(LawReqDTO lawReqDTO);

    String previewLaw(String url);

}
