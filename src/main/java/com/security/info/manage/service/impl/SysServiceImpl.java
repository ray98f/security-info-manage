package com.security.info.manage.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.VxAccessToken;
import com.security.info.manage.dto.req.*;
import com.security.info.manage.dto.res.*;
import com.security.info.manage.entity.Accident;
import com.security.info.manage.entity.RiskLevel;
import com.security.info.manage.entity.Role;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.SysMapper;
import com.security.info.manage.service.SysService;
import com.security.info.manage.utils.*;
import com.security.info.manage.utils.treeTool.LawCatalogTreeToolUtils;
import com.security.info.manage.utils.treeTool.MenuTreeToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author frp
 */
@Service
@Slf4j
public class SysServiceImpl implements SysService {

    public static final String HTTPS = "https";
    public static final String HTTP = "http";
    public static final String SESSION_KEY = "session_key";
    public static final String USERID = "userid";
    public static final String TOKEN = "token";

    @Value("${vx-business.corpid}")
    private String corpid;

    @Value("${vx-business.corpsecret}")
    private String corpsecret;

    @Value("${vx-business.appcorpsecret}")
    private String appcorpsecret;

    @Value("${vx-business.pccorpsecret}")
    private String pccorpsecret;

    @Value("${vx-business.jumppage}")
    private String jumppage;

    @Autowired
    private SysMapper sysMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Integer vxAuditStatus() {
        Integer result = sysMapper.selectVxAuditStatus();
        return result == null ? 0 : result;
    }

    @Override
    public Map<String, Object> h5sign(String str) {
        VxAccessToken accessToken = VxApiUtils.getAccessToken(corpid, appcorpsecret);
        if (accessToken == null) {
            throw new CommonException(ErrorCode.VX_ERROR, "accessToken返回为空!");
        }
        String url = Constants.VX_GET_TICKET + "&access_token=" + accessToken.getToken();
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url)
                .build()
                .expand()
                .encode();
        URI uri = uriComponents.toUri();
        JSONObject res = restTemplate.getForEntity(uri, JSONObject.class).getBody();
        if (!Constants.SUCCESS.equals(Objects.requireNonNull(res).getString(Constants.ERR_CODE))) {
            throw new CommonException(ErrorCode.VX_ERROR, String.valueOf(res.get(Constants.ERR_MSG)));
        }
        if (res.getString("ticket") == null) {
            throw new CommonException(ErrorCode.VX_ERROR, "ticket返回为空");
        }
        String noncestr = TokenUtil.getUuId();
        long timestamp = System.currentTimeMillis() / 1000;
        String sign = "jsapi_ticket=" + res.getString("ticket") + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + str;
        String signature = new SHA1().getDigestOfString(sign.getBytes(StandardCharsets.UTF_8));
        Map<String, Object> data = new HashMap<>();
        data.put("appId", corpid);
        data.put("timestamp", timestamp);
        data.put("noncestr", noncestr);
        data.put("signature", signature);
        return data;
    }

    @Override
    public Map<String, Object> login(LoginReqDTO loginReqDTO) throws Exception {
        if (Objects.isNull(loginReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        UserResDTO resDTO = sysMapper.selectUserByUserName(loginReqDTO.getUserName());
        if (Objects.isNull(resDTO) || Objects.isNull(resDTO.getId()) || !MyAESUtil.decrypt(loginReqDTO.getPassword()).equals(MyAESUtil.decrypt(resDTO.getPassword()))) {
            throw new CommonException(ErrorCode.LOGIN_PASSWORD_ERROR);
        }
        Map<String, Object> data = new HashMap<>(16);
        data.put("userInfo", resDTO);
        data.put(TOKEN, TokenUtil.createSimpleToken(resDTO));
        return data;
    }

    @Override
    public Map<String, Object> scanLogin(String code) {
        VxAccessToken accessToken = VxApiUtils.getAccessToken(corpid, pccorpsecret);
        if (accessToken == null) {
            throw new CommonException(ErrorCode.VX_ERROR, "accessToken返回为空!");
        }
        String url = Constants.VX_GET_USERINFO + "?access_token=" + accessToken.getToken() + "&code=" + code;
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url)
                .build()
                .expand()
                .encode();
        URI uri = uriComponents.toUri();
        JSONObject res = restTemplate.getForEntity(uri, JSONObject.class).getBody();
        if (!Constants.SUCCESS.equals(Objects.requireNonNull(res).getString(Constants.ERR_CODE))) {
            throw new CommonException(ErrorCode.VX_ERROR, String.valueOf(res.get(Constants.ERR_MSG)));
        }
        if (res.getString("UserId") == null) {
            throw new CommonException(ErrorCode.USER_ERROR);
        }
        Map<String, Object> data = new HashMap<>(2);
        UserResDTO resDTO = sysMapper.selectUserById(res.getString("UserId"));
        if (Objects.isNull(resDTO) || Objects.isNull(resDTO.getId())) {
            throw new CommonException(ErrorCode.USER_ERROR);
        }
        data.put("userInfo", resDTO);
        data.put(TOKEN, TokenUtil.createSimpleToken(resDTO));
        return data;
    }

    @Override
    public Map<String, Object> vxLoginSimple(LoginReqDTO loginReqDTO) throws Exception {
        if (Objects.isNull(loginReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        UserResDTO resDTO = sysMapper.selectUserByMobile(loginReqDTO.getMobile());
        if (Objects.isNull(resDTO) || Objects.isNull(resDTO.getId()) || !MyAESUtil.decrypt(loginReqDTO.getPassword()).equals(MyAESUtil.decrypt(resDTO.getPassword()))) {
            throw new CommonException(ErrorCode.LOGIN_PASSWORD_ERROR);
        }
        Map<String, Object> data = new HashMap<>(16);
        data.put("userInfo", resDTO);
        data.put(TOKEN, TokenUtil.createLongTermToken(resDTO));
        return data;
    }

    @Override
    public Map<String, Object> vxLogin(String code) {
        if (Objects.isNull(code)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        VxAccessToken accessToken = VxApiUtils.getAccessToken(corpid, appcorpsecret);
        if (accessToken == null) {
            throw new CommonException(ErrorCode.VX_ERROR, "accessToken返回为空!");
        }
        String url = Constants.VX_GET_CODE2SESSION + "&access_token=" + accessToken.getToken() + "&js_code=" + code;
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url)
                .build()
                .expand()
                .encode();
        URI uri = uriComponents.toUri();
        JSONObject res = restTemplate.getForEntity(uri, JSONObject.class).getBody();
        if (!Constants.SUCCESS.equals(Objects.requireNonNull(res).getString(Constants.ERR_CODE))) {
            throw new CommonException(ErrorCode.VX_ERROR, String.valueOf(res.get(Constants.ERR_MSG)));
        }
        if (res.getString(SESSION_KEY) == null || res.getString(USERID) == null) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Map<String, Object> data = new HashMap<>(16);
        data.put(SESSION_KEY, res.getString(SESSION_KEY));
        UserResDTO resDTO = sysMapper.selectUserById(res.getString(USERID));
        if (Objects.isNull(resDTO) || Objects.isNull(resDTO.getId())) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        data.put("userInfo", resDTO);
        data.put(TOKEN, TokenUtil.createLongTermToken(resDTO));
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

    @Override
    public Page<OperationLogResDTO> listOperLog(String startTime, String endTime, Integer type, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return sysMapper.listOperLog(pageReqDTO.of(), startTime, endTime, type);
    }
}
