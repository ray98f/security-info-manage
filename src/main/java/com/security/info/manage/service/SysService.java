package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.*;
import com.security.info.manage.dto.res.MenuResDTO;
import com.security.info.manage.dto.res.OperationLogResDTO;
import com.security.info.manage.dto.res.UserResDTO;
import com.security.info.manage.entity.Accident;
import com.security.info.manage.entity.RiskLevel;
import com.security.info.manage.entity.Role;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author frp
 */
public interface SysService {

    Integer vxAuditStatus();

    Map<String, Object> login(LoginReqDTO loginReqDTO) throws Exception;

    Map<String, Object> scanLogin(String code);

    Map<String, Object> vxLoginSimple(LoginReqDTO loginReqDTO) throws Exception;

    Map<String, Object> vxLogin(String code);

    List<MenuResDTO> listUserMenu(String userId);

    List<RiskLevel> listRiskLevel();

    List<Accident> listAccident();

    Page<MenuResDTO> listMenu(Integer type, String name, PageReqDTO pageReqDTO);

    List<MenuResDTO> listAllMenu();

    void modifyMenu(MenuReqDTO menuReqDTO);

    void addMenu(MenuReqDTO menuReqDTO);

    void deleteMenu(MenuReqDTO menuReqDTO);

    List<Role> listAllRole();

    Page<Role> listRole(Integer status,String roleName,PageReqDTO pageReqDTO);

    void deleteRole(RoleReqDTO role);

    void insertRole(RoleReqDTO role);

    void updateRole(RoleReqDTO role);

    List<String> selectMenuIds(String roleId);

    Page<UserResDTO> listUserRole(String roleId, PageReqDTO pageReqDTO);

    void addUserRole(UserRoleReqDTO userRoleReqDTO);

    void deleteUserRole(UserRoleReqDTO userRoleReqDTO);

    Page<OperationLogResDTO> listOperLog(String startTime, String endTime, Integer type, PageReqDTO pageReqDTO);

}
