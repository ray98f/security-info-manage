package com.security.info.manage.controller;

import com.security.info.manage.annotation.LogMaker;
import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.*;
import com.security.info.manage.dto.res.*;
import com.security.info.manage.entity.Accident;
import com.security.info.manage.entity.RiskLevel;
import com.security.info.manage.entity.Role;
import com.security.info.manage.service.SysService;
import com.security.info.manage.service.TransportService;
import com.security.info.manage.utils.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
@Slf4j
@RestController
@RequestMapping("/sys")
@Api(tags = "系统管理")
@Validated
public class SysController {

    @Resource
    private SysService sysService;

    @GetMapping("/h5sign")
    @ApiOperation(value = "h5页面签名获取")
    public DataResponse<Map<String, Object>> h5sign(@RequestParam String str) {
        return DataResponse.of(sysService.h5sign(str));
    }

    @PostMapping("/login")
    @ApiOperation(value = "登录")
    @LogMaker(value = "网页端-平台登录")
    public DataResponse<Map<String, Object>> login(@RequestBody LoginReqDTO loginReqDTO) throws Exception {
        return DataResponse.of(sysService.login(loginReqDTO));
    }

    @GetMapping("/scan/login")
    @ApiOperation(value = "扫码登录")
    @LogMaker(value = "网页端-扫码登录")
    public DataResponse<Map<String, Object>> scanLogin(@RequestParam String code) {
        return DataResponse.of(sysService.scanLogin(code));
    }

    @GetMapping("/vx/audit/status")
    @ApiOperation(value = "微信小程序审核状态")
    public DataResponse<Map<String, Integer>> vxAuditStatus() {
        return DataResponse.of(new HashMap<String, Integer>(1) {{
            put("status", sysService.vxAuditStatus());
        }});
    }

    @PostMapping("/vx/login/simple")
    @ApiOperation(value = "微信普通手机号登录")
    public DataResponse<Map<String, Object>> vxLoginSimple(@RequestBody LoginReqDTO loginReqDTO) throws Exception {
        return DataResponse.of(sysService.vxLoginSimple(loginReqDTO));
    }

    @GetMapping("/vx/login")
    @ApiOperation(value = "微信授权登录")
    @LogMaker(value = "微信小程序-授权登录")
    public DataResponse<Map<String, Object>> vxLogin(@RequestParam String code) {
        return DataResponse.of(sysService.vxLogin(code));
    }

    @PostMapping("/user/menu")
    @ApiOperation(value = "获取登录用户菜单列表")
    public DataResponse<List<MenuResDTO>> listUserMenu() {
        return DataResponse.of(sysService.listUserMenu(TokenUtil.getCurrentPersonNo()));
    }

    @GetMapping("/listRiskLevel")
    @ApiOperation(value = "获取风险等级列表")
    public DataResponse<List<RiskLevel>> listRiskLevel() {
        return DataResponse.of(sysService.listRiskLevel());
    }

    @GetMapping("/listAccident")
    @ApiOperation(value = "获取事故列表")
    public DataResponse<List<Accident>> listAccident() {
        return DataResponse.of(sysService.listAccident());
    }

    @GetMapping("/menu/list")
    @ApiOperation(value = "获取菜单列表")
    public PageResponse<MenuResDTO> listMenu(@RequestParam(required = false) Integer type,
                                             @RequestParam(required = false) String name,
                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(sysService.listMenu(type, name, pageReqDTO));
    }

    @GetMapping("/menu/listAll")
    @ApiOperation(value = "获取所有菜单列表")
    public DataResponse<List<MenuResDTO>> listAllMenu() {
        return DataResponse.of(sysService.listAllMenu());
    }

    @PostMapping("/menu/modify")
    @ApiOperation(value = "菜单修改")
    @LogMaker(value = "网页端-系统设置菜单管理修改")
    public DataResponse<T> modifyMenu(@RequestBody MenuReqDTO menuReqDTO) {
        sysService.modifyMenu(menuReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/menu/add")
    @ApiOperation(value = "菜单新增")
    @LogMaker(value = "网页端-系统设置菜单管理新增")
    public DataResponse<T> addMenu(@RequestBody MenuReqDTO menuReqDTO) {
        sysService.addMenu(menuReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/menu/delete")
    @ApiOperation(value = "菜单删除")
    @LogMaker(value = "网页端-系统设置菜单管理删除")
    public DataResponse<T> deleteMenu(@RequestBody MenuReqDTO menuReqDTO) {
        sysService.deleteMenu(menuReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/role/listAll")
    @ApiOperation(value = "获取所有角色信息")
    public DataResponse<List<Role>> listAllRole() {
        return DataResponse.of(sysService.listAllRole());
    }

    @GetMapping("/role/list")
    @ApiOperation(value = "分页获取角色信息")
    public PageResponse<Role> listRole(@RequestParam(required = false) @ApiParam("状态") Integer status,
                                       @RequestParam(required = false) @ApiParam("角色名称") String roleName,
                                       @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(sysService.listRole(status, roleName, pageReqDTO));
    }

    @PostMapping("/role/delete")
    @ApiOperation(value = "删除角色")
    @LogMaker(value = "网页端-系统设置角色管理删除")
    public DataResponse<T> deleteRole(@RequestBody RoleReqDTO roleReqDTO) {
        sysService.deleteRole(roleReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/role/add")
    @ApiOperation(value = "新增角色")
    @LogMaker(value = "网页端-系统设置角色管理新增")
    public DataResponse<T> insertRole(@RequestBody RoleReqDTO roleReqDTO) {
        sysService.insertRole(roleReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/role/modify")
    @ApiOperation(value = "修改角色")
    @LogMaker(value = "网页端-系统设置角色管理修改")
    public DataResponse<T> updateRole(@RequestBody RoleReqDTO roleReqDTO) {
        sysService.updateRole(roleReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/menu/{roleId}")
    @ApiOperation(value = "获取角色对应菜单id")
    public DataResponse<List<String>> detailRoleMenu(@PathVariable String roleId) {
        return DataResponse.of(sysService.selectMenuIds(roleId));
    }

    @GetMapping("/role/user/list")
    @ApiOperation(value = "角色绑定人员列表")
    public PageResponse<UserResDTO> listUserRole(@RequestParam String roleId,
                                                 @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(sysService.listUserRole(roleId, pageReqDTO));
    }

    @PostMapping("/role/user/add")
    @ApiOperation(value = "新增角色绑定人员")
    @LogMaker(value = "网页端-系统设置角色绑定人员新增")
    public DataResponse<T> addUserRole(@RequestBody UserRoleReqDTO userRoleReqDTO) {
        sysService.addUserRole(userRoleReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/role/user/delete")
    @ApiOperation(value = "删除角色绑定人员")
    @LogMaker(value = "网页端-系统设置角色绑定人员删除")
    public DataResponse<T> deleteUserRole(@RequestBody UserRoleReqDTO userRoleReqDTO) {
        sysService.deleteUserRole(userRoleReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/oper/log")
    @ApiOperation(value = "获取操作日志列表")
    public PageResponse<OperationLogResDTO> listOperLog(@RequestParam(required = false) String startTime,
                                                        @RequestParam(required = false) String endTime,
                                                        @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(sysService.listOperLog(startTime, endTime, pageReqDTO));
    }

    @GetMapping("/menu")
    @ApiOperation(value = "menu新增初始数据")
    public DataResponse<T> menu() {
        sysService.menu();
        return DataResponse.success();
    }
}
