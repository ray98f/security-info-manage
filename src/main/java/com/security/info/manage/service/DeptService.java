package com.security.info.manage.service;

import com.security.info.manage.dto.res.DeptTreeResDTO;
import com.security.info.manage.dto.res.UserResDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author frp
 */
public interface DeptService {

    void syncDept(String orgId);

    List<DeptTreeResDTO> listSyncDept();

    List<DeptTreeResDTO> listTree();

    List<DeptTreeResDTO> listFirst();

    List<UserResDTO> getDeptUser(String deptId, String dangerId);

}
