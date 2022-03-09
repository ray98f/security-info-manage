package com.security.info.manage.service;

import com.security.info.manage.dto.res.DeptTreeResDTO;

import java.util.List;

/**
 * @author frp
 */
public interface DeptService {

    void syncDept(String orgId);

    List<DeptTreeResDTO> listTree();

    List<DeptTreeResDTO> listFirst();

}
