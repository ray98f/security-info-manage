package com.security.info.manage.controller;

import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.LawCatalogReqDTO;
import com.security.info.manage.dto.req.LawCatalogUserRoleReqDTO;
import com.security.info.manage.dto.req.LawReqDTO;
import com.security.info.manage.dto.res.LawCatalogResDTO;
import com.security.info.manage.dto.res.LawResDTO;
import com.security.info.manage.service.LawService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author frp
 */
@Slf4j
@RestController
@RequestMapping("/law")
@Api(tags = "法律法规管理")
@Validated
public class LawController {

    @Resource
    private LawService lawService;

    @GetMapping("/catalog/list")
    @ApiOperation(value = "获取法律法规目录列表")
    public PageResponse<LawCatalogResDTO> listLawCatalog(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(lawService.listLawCatalog(pageReqDTO));
    }

    @GetMapping("/catalog/listAll")
    @ApiOperation(value = "获取所有法律法规目录列表")
    public DataResponse<List<LawCatalogResDTO>> listAllLawCatalog(@RequestParam(required = false) String deptId) {
        return DataResponse.of(lawService.listAllLawCatalog(deptId));
    }

    @PostMapping("/catalog/modify")
    @ApiOperation(value = "法律法规目录修改")
    public DataResponse<T> modifyLawCatalog(@RequestBody LawCatalogReqDTO lawCatalogReqDTO) {
        lawService.modifyLawCatalog(lawCatalogReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/catalog/add")
    @ApiOperation(value = "法律法规目录新增")
    public DataResponse<T> addLawCatalog(@RequestBody LawCatalogReqDTO lawCatalogReqDTO) {
        lawService.addLawCatalog(lawCatalogReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/catalog/role")
    @ApiOperation(value = "法律法规目录权限分配")
    public DataResponse<T> addLawCatalogRole(@RequestBody LawCatalogUserRoleReqDTO lawCatalogUserRoleReqDTO) {
        lawService.addLawCatalogRole(lawCatalogUserRoleReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取法律法规列表")
    public PageResponse<LawResDTO> listLaw(@RequestParam(required = false) String catalogId,
                                           @RequestParam(required = false) String name,
                                           @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(lawService.listLaw(catalogId, name, pageReqDTO));
    }

    @PostMapping("/add")
    @ApiOperation(value = "法律法规文件上传")
    public DataResponse<T> addLaw(@RequestBody LawReqDTO lawReqDTO) {
        lawService.addLaw(lawReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "法律法规文件删除")
    public DataResponse<T> deleteLaw(@RequestBody LawReqDTO lawReqDTO) {
        lawService.deleteLaw(lawReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/preview")
    @ApiOperation(value = "法律法规预览")
    public DataResponse<String> previewLaw(@RequestParam String url) {
        return DataResponse.of(lawService.previewLaw(url));
    }

}
