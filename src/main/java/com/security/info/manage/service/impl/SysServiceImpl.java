package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.LoginReqDTO;
import com.security.info.manage.dto.req.MenuReqDTO;
import com.security.info.manage.dto.req.RoleReqDTO;
import com.security.info.manage.dto.req.UserRoleReqDTO;
import com.security.info.manage.dto.res.LawCatalogResDTO;
import com.security.info.manage.dto.res.MenuResDTO;
import com.security.info.manage.dto.res.UserResDTO;
import com.security.info.manage.entity.Accident;
import com.security.info.manage.entity.RiskLevel;
import com.security.info.manage.entity.Role;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.SysMapper;
import com.security.info.manage.service.SysService;
import com.security.info.manage.utils.AesUtils;
import com.security.info.manage.utils.Constants;
import com.security.info.manage.utils.MyAESUtil;
import com.security.info.manage.utils.TokenUtil;
import com.security.info.manage.utils.treeTool.LawCatalogTreeToolUtils;
import com.security.info.manage.utils.treeTool.MenuTreeToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class SysServiceImpl implements SysService {

    public static final String HTTPS = "https";
    public static final String HTTP = "http";
    @Autowired
    private SysMapper sysMapper;

    @Override
    public Map<String, Object> login(LoginReqDTO loginReqDTO) throws Exception {
        if (Objects.isNull(loginReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        UserResDTO resDTO = sysMapper.selectUser(loginReqDTO.getUserName());
        if (Objects.isNull(resDTO) || Objects.isNull(resDTO.getId()) || !MyAESUtil.decrypt(loginReqDTO.getPassword()).equals(MyAESUtil.decrypt(resDTO.getPassword()))) {
            throw new CommonException(ErrorCode.LOGIN_PASSWORD_ERROR);
        }
        Map<String, Object> data = new HashMap<>(16);
        data.put("userInfo", resDTO);
        data.put("token", TokenUtil.createSimpleToken(resDTO));
        return data;
    }

    @Override
    public List<MenuResDTO> listUserMenu(String userId) {
        if (Objects.isNull(userId)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        List<MenuResDTO> extraRootList = sysMapper.listUserRootMenu(userId);
        if (Objects.isNull(extraRootList)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        List<MenuResDTO> extraBodyList = sysMapper.listUserBodyMenu(userId);
        MenuTreeToolUtils extraTree = new MenuTreeToolUtils(extraRootList, extraBodyList);
        return extraTree.getTree();
    }

    @Override
    public List<RiskLevel> listRiskLevel() {
        return sysMapper.listRiskLevel();
    }

    @Override
    public List<Accident> listAccident() {
        return sysMapper.listAccident();
    }

    @Override
    public Page<MenuResDTO> listMenu(Integer type, String name, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<MenuResDTO> page = sysMapper.listMenu(pageReqDTO.of(), type, name);
        List<MenuResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
            for (MenuResDTO res : list) {
                res.setParentNames(res.getParentNames().replace("root", "").replaceAll(",", "/") + "/" + res.getMenuName());
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public List<MenuResDTO> listAllMenu() {
        List<MenuResDTO> extraRootList = sysMapper.getMenuRoot();
        if (Objects.isNull(extraRootList)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        List<MenuResDTO> extraBodyList = sysMapper.getMenuBody();
        MenuTreeToolUtils extraTree = new MenuTreeToolUtils(extraRootList, extraBodyList);
        return extraTree.getTree();
    }

    @Override
    public void modifyMenu(MenuReqDTO menuReqDTO) {
        if (Objects.isNull(menuReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        if (menuReqDTO.getStatus() == 1 || menuReqDTO.getIsShow() == 0) {
            Integer result = sysMapper.selectIfMenuHadChild(menuReqDTO.getId());
            if (result > 0) {
                throw new CommonException(ErrorCode.CANT_UPDATE_HAD_CHILD);
            }
        }
        menuReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = sysMapper.modifyMenu(menuReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void addMenu(MenuReqDTO menuReqDTO) {
        if (Objects.isNull(menuReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        menuReqDTO.setId(TokenUtil.getUuId());
        menuReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = sysMapper.addMenu(menuReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void deleteMenu(MenuReqDTO menuReqDTO) {
        if (Objects.isNull(menuReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = sysMapper.selectIfMenuHadChild(menuReqDTO.getId());
        if (result > 0) {
            throw new CommonException(ErrorCode.CANT_DELETE_HAD_CHILD);
        }
        menuReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = sysMapper.deleteMenu(menuReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public List<Role> listAllRole() {
        return sysMapper.listAllRole();
    }

    @Override
    public Page<Role> listRole(Integer status, String roleName, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return sysMapper.listRole(pageReqDTO.of(), status, roleName);
    }

    @Override
    public void deleteRole(RoleReqDTO role) {
        Integer result = sysMapper.selectRoleUse(role.getId());
        if (result > 0) {
            throw new CommonException(ErrorCode.ROLE_USE_CANT_DELETE);
        }
        result = sysMapper.deleteRole(role.getId());
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public void insertRole(RoleReqDTO role) {
        if (Objects.isNull(role)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = sysMapper.selectRoleIsExist(role);
        if (result > 0) {
            throw new CommonException(ErrorCode.ROLE_EXIST);
        }
        role.setId(TokenUtil.getUuId());
        role.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = sysMapper.insertRole(role);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
        if (null != role.getMenuIds() && !role.getMenuIds().isEmpty()) {
            result = sysMapper.insertRoleMenu(role.getId(), role.getMenuIds(), role.getCreateBy());
            if (result < 0) {
                throw new CommonException(ErrorCode.INSERT_ERROR);
            }
        }
    }

    @Override
    public void updateRole(RoleReqDTO role) {
        if (Objects.isNull(role)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = sysMapper.selectRoleIsExist(role);
        if (result > 0) {
            throw new CommonException(ErrorCode.ROLE_EXIST);
        }
        role.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = sysMapper.updateRole(role);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
        sysMapper.deleteRoleMenus(role.getId());
        if (null != role.getMenuIds() && !role.getMenuIds().isEmpty()) {
            result = sysMapper.insertRoleMenu(role.getId(), role.getMenuIds(), role.getCreateBy());
            if (result < 0) {
                throw new CommonException(ErrorCode.INSERT_ERROR);
            }
        }
    }

    @Override
    public List<String> selectMenuIds(String roleId) {
        if (null == roleId || "".equals(roleId)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        return sysMapper.selectRoleMenuIds(roleId);
    }

    @Override
    public Page<UserResDTO> listUserRole(String roleId, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return sysMapper.listUserRole(pageReqDTO.of(), roleId);
    }

    @Override
    public void addUserRole(UserRoleReqDTO userRoleReqDTO) {
        if (Objects.isNull(userRoleReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = sysMapper.addUserRole(userRoleReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void deleteUserRole(UserRoleReqDTO userRoleReqDTO) {
        if (Objects.isNull(userRoleReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = sysMapper.deleteUserRole(userRoleReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }
}
