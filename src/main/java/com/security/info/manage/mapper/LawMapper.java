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

    List<LawCatalogResDTO> getRoot(String deptId);

    List<LawCatalogResDTO> getBody(String deptId);

    List<LawCatalogResDTO> getVxRoot(String userId);

    List<LawCatalogResDTO> getVxBody(String userId);

    Integer selectIfLawCatalogHadChild(String route);

    Integer modifyLawCatalog(LawCatalogReqDTO lawCatalogReqDTO);

    Integer addLawCatalog(LawCatalogReqDTO lawCatalogReqDTO);

    Integer addLawCatalogRole(LawCatalogUserRoleReqDTO lawCatalogUserRoleReqDTO);

    Page<LawResDTO> listLaw(Page<LawResDTO> page, String catalogId, String name);

    Integer addLaw(LawReqDTO lawReqDTO);

    Integer deleteLaw(LawReqDTO lawReqDTO);

}
