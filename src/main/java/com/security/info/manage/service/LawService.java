package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.LawCatalogReqDTO;
import com.security.info.manage.dto.req.LawCatalogUserRoleReqDTO;
import com.security.info.manage.dto.req.LawReqDTO;
import com.security.info.manage.dto.res.LawCatalogResDTO;
import com.security.info.manage.dto.res.LawResDTO;
import com.security.info.manage.entity.File;

import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
public interface LawService {

    List<LawCatalogResDTO> listLawCatalog();

    List<LawCatalogResDTO> listAllLawCatalog(String deptId);

    Map<String, Object> vxListAllLawCatalog(String catalogId, String searchKey);

    void modifyLawCatalog(LawCatalogReqDTO trainReqDTO);

    void addLawCatalog(LawCatalogReqDTO trainReqDTO);

    void deleteLawCatalog(LawCatalogReqDTO trainReqDTO);

    void addLawCatalogRole(LawCatalogUserRoleReqDTO lawCatalogUserRoleReqDTO);

    Page<LawResDTO> listLaw(String catalogId, String name, PageReqDTO pageReqDTO);

    void addLaw(LawReqDTO lawReqDTO);

    void deleteLaw(LawReqDTO lawReqDTO);

    String previewLaw(String url, String fileName);

    List<LawResDTO> lawSearch(String searchKey);

    List<File> lawAllSearch();

}
