package com.security.info.manage.controller;

import com.security.info.manage.dto.res.DeptTreeResDTO;
import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.service.OrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author frp
 */
@Slf4j
@RestController
@RequestMapping("/org")
@Api(tags = "组织机构管理")
@Validated
public class OrganizationController {

    @Resource
    private OrganizationService organizationService;

    @GetMapping("/sync")
    @ApiOperation(value = "同步企业微信组织机构")
    public DataResponse<T> syncOrg(@RequestParam(required = false) String orgId) {
        organizationService.syncOrg(orgId);
        return DataResponse.success();
    }

    @GetMapping("/listTree")
    @ApiOperation(value = "公司层级结构获取")
    public DataResponse<List<DeptTreeResDTO>> listTree() {
        return DataResponse.of(organizationService.listTree());
    }

    @GetMapping("/listFirst")
    @ApiOperation(value = "获取一级目录列表")
    public DataResponse<List<DeptTreeResDTO>> listFirst() {
        return DataResponse.of(organizationService.listFirst());
    }
}
