package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.LoginReqDTO;
import com.security.info.manage.dto.req.MenuReqDTO;
import com.security.info.manage.dto.req.RoleReqDTO;
import com.security.info.manage.dto.req.UserRoleReqDTO;
import com.security.info.manage.dto.res.MenuResDTO;
import com.security.info.manage.dto.res.UserResDTO;
import com.security.info.manage.entity.Accident;
import com.security.info.manage.entity.RiskLevel;
import com.security.info.manage.entity.Role;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author frp
 */
public interface SysService {

    Map<String, Object> login(LoginReqDTO loginReqDTO) throws Exception;

    List<MenuResDTO> listUserMenu(String userId);

    List<RiskLevel> listRiskLevel();

    List<Accident> listAccident();

    Page<MenuResDTO> listMenu(PageReqDTO pageReqDTO);

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

}
