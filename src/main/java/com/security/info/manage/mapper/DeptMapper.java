package com.security.info.manage.mapper;

import com.security.info.manage.dto.res.DeptTreeResDTO;
import com.security.info.manage.dto.res.UserResDTO;
import com.security.info.manage.dto.res.VxDeptResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface DeptMapper {

    void syncOrg(List<VxDeptResDTO> list, String userId);

    List<DeptTreeResDTO> listSyncDept();

    List<DeptTreeResDTO> getBody();

    List<DeptTreeResDTO> getRoot();

    List<DeptTreeResDTO> listCompanyList();

    DeptTreeResDTO selectParent(String deptId);

    List<UserResDTO> selectDepartmentUser(String deptId, String dangerId, Integer type);

    String upRecursion(String deptId);

    List<String> downRecursion(String deptId);
}
