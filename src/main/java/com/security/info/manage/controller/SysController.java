package com.security.info.manage.controller;

import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.LoginReqDTO;
import com.security.info.manage.dto.res.TransportResDTO;
import com.security.info.manage.entity.Accident;
import com.security.info.manage.entity.RiskLevel;
import com.security.info.manage.service.SysService;
import com.security.info.manage.service.TransportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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

    @PostMapping("/login")
    @ApiOperation(value = "登录")
    public DataResponse<Map<String, Object>> login(@RequestBody LoginReqDTO loginReqDTO) throws Exception {
        return DataResponse.of(sysService.login(loginReqDTO));
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
}
