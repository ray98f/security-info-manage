package com.security.info.manage.service;

import com.security.info.manage.dto.res.DeptTreeResDTO;

import java.util.List;

/**
 * @author frp
 */
public interface OrganizationService {

    void syncOrg(String orgId);

    List<DeptTreeResDTO> listTree();

    List<DeptTreeResDTO> listFirst();

}
