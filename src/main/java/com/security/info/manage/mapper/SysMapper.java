package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.MenuReqDTO;
import com.security.info.manage.dto.req.RoleReqDTO;
import com.security.info.manage.dto.req.UserRoleReqDTO;
import com.security.info.manage.dto.res.MenuResDTO;
import com.security.info.manage.dto.res.OperationLogResDTO;
import com.security.info.manage.dto.res.UserResDTO;
import com.security.info.manage.entity.Accident;
import com.security.info.manage.entity.RiskLevel;
import com.security.info.manage.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface SysMapper {

    UserResDTO selectUser(String userName);

    List<MenuResDTO> listUserRootMenu(String userId);

    List<MenuResDTO> listUserBodyMenu(String userId);

    List<RiskLevel> listRiskLevel();

    List<Accident> listAccident();

    Page<MenuResDTO> listMenu(Page<MenuResDTO> page, Integer type, String name);

    List<MenuResDTO> getMenuRoot();

    List<MenuResDTO> getMenuBody();

    Integer selectIfMenuHadChild(String id);

    Integer modifyMenu(MenuReqDTO menuReqDTO);

    Integer addMenu(MenuReqDTO menuReqDTO);

    Integer deleteMenu(MenuReqDTO menuReqDTO);

    List<Role> listAllRole();

    Page<Role> listRole(Page<Role> page, Integer status, String roleName);

    Integer selectRoleUse(String id);

    Integer deleteRole(String id);

    Integer selectRoleIsExist(RoleReqDTO role);

    Integer insertRole(RoleReqDTO role);

    Integer updateRole(RoleReqDTO role);

    List<String> selectRoleMenuIds(String roleId);

    Integer insertRoleMenu(String roleId, List<String> menuIds, String doName);

    void deleteRoleMenus(String roleId);

    Page<UserResDTO> listUserRole(Page<UserResDTO> page, String roleId);

    Integer addUserRole(UserRoleReqDTO userRoleReqDTO);

    Integer deleteUserRole(UserRoleReqDTO userRoleReqDTO);

    Page<OperationLogResDTO> listOperLog(Page<OperationLogResDTO> page, String startTime, String endTime, Integer type);
}
