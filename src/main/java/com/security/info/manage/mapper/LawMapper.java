package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.req.LawCatalogReqDTO;
import com.security.info.manage.dto.req.LawCatalogUserRoleReqDTO;
import com.security.info.manage.dto.req.LawReqDTO;
import com.security.info.manage.dto.res.LawCatalogResDTO;
import com.security.info.manage.dto.res.LawResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface LawMapper {

    Page<LawCatalogResDTO> listLawCatalog(Page<LawCatalogResDTO> page);

    List<LawCatalogResDTO> getRoot();

    List<LawCatalogResDTO> getBody();

    List<LawCatalogResDTO> getRootByDeptId(String deptId);

    List<LawCatalogResDTO> getBodyByDeptId(String deptId);

    List<LawCatalogResDTO> getVxRoot(String userId);

    List<LawCatalogResDTO> getVxBody(String catalogId, String userId);

    Integer selectIfLawCatalogHadChild(String route);

    Integer selectIfLawCatalogHadLaw(String id);

    Integer modifyLawCatalog(LawCatalogReqDTO lawCatalogReqDTO);

    Integer addLawCatalog(LawCatalogReqDTO lawCatalogReqDTO);

    Integer deleteLawCatalog(LawCatalogReqDTO lawCatalogReqDTO);

    List<String> getLawCatalogRole(String userId);

    Integer addLawCatalogRole(LawCatalogUserRoleReqDTO lawCatalogUserRoleReqDTO);

    List<LawResDTO> vxListLaw(String catalogId, String searchKey);

    Page<LawResDTO> listLaw(Page<LawResDTO> page, String catalogId, String name);

    Integer addLaw(LawReqDTO lawReqDTO);

    Integer modifyLaw(LawReqDTO lawReqDTO);

    Integer deleteLaw(LawReqDTO lawReqDTO);

    LawResDTO selectLawByFileId(String fileId, String userId);

    LawResDTO selectLawByFileIdAndCatalogIds(List<String> catalogIds, String fileId, String userId);

    List<String> selectCatalogIds(String catalogId);

}
