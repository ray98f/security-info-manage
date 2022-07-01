package com.security.info.manage.controller;

import com.security.info.manage.annotation.LogMaker;
import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.LawCatalogReqDTO;
import com.security.info.manage.dto.req.LawCatalogUserRoleReqDTO;
import com.security.info.manage.dto.req.LawReqDTO;
import com.security.info.manage.dto.res.LawCatalogResDTO;
import com.security.info.manage.dto.res.LawResDTO;
import com.security.info.manage.entity.File;
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
import java.util.Map;

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
    public DataResponse<List<LawCatalogResDTO>> listLawCatalog() {
        return DataResponse.of(lawService.listLawCatalog());
    }

    @GetMapping("/catalog/listAll")
    @ApiOperation(value = "获取所有法律法规目录列表")
    public DataResponse<List<LawCatalogResDTO>> listAllLawCatalog(@RequestParam(required = false) String deptId) {
        return DataResponse.of(lawService.listAllLawCatalog(deptId));
    }

    @GetMapping("/vx/catalog/listAll")
    @ApiOperation(value = "微信端-获取所有法律法规目录列表")
    public DataResponse<Map<String, Object>> vxListAllLawCatalog(@RequestParam(required = false) String catalogId,
                                                                 @RequestParam(required = false) String searchKey) {
        return DataResponse.of(lawService.vxListAllLawCatalog(catalogId, searchKey));
    }

    @PostMapping("/catalog/modify")
    @ApiOperation(value = "法律法规目录修改")
    @LogMaker(value = "网页端-法律法规目录修改")
    public DataResponse<T> modifyLawCatalog(@RequestBody LawCatalogReqDTO lawCatalogReqDTO) {
        lawService.modifyLawCatalog(lawCatalogReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/catalog/add")
    @ApiOperation(value = "法律法规目录新增")
    @LogMaker(value = "网页端-法律法规目录新增")
    public DataResponse<T> addLawCatalog(@RequestBody LawCatalogReqDTO lawCatalogReqDTO) {
        lawService.addLawCatalog(lawCatalogReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/catalog/delete")
    @ApiOperation(value = "法律法规目录删除")
    @LogMaker(value = "网页端-法律法规目录删除")
    public DataResponse<T> deleteLawCatalog(@RequestBody LawCatalogReqDTO lawCatalogReqDTO) {
        lawService.deleteLawCatalog(lawCatalogReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/catalog/roleGet")
    @ApiOperation(value = "法律法规目录权限分配")
    @LogMaker(value = "网页端-法律法规目录权限分配")
    public DataResponse<List<String>> getLawCatalogRole(@RequestParam String userId) {
        return DataResponse.of(lawService.getLawCatalogRole(userId));
    }

    @PostMapping("/catalog/role")
    @ApiOperation(value = "法律法规目录权限分配")
    @LogMaker(value = "网页端-法律法规目录权限分配")
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
    @LogMaker(value = "网页端-法律法规文件上传")
    public DataResponse<T> addLaw(@RequestBody LawReqDTO lawReqDTO) {
        lawService.addLaw(lawReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "法律法规文件删除")
    @LogMaker(value = "网页端-法律法规文件删除")
    public DataResponse<T> deleteLaw(@RequestBody LawReqDTO lawReqDTO) {
        lawService.deleteLaw(lawReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/preview")
    @ApiOperation(value = "法律法规预览")
    public DataResponse<String> previewLaw(@RequestParam String url,
                                           @RequestParam String fileName) {
        return DataResponse.of(lawService.previewLaw(url, fileName));
    }

    @GetMapping("/vx/search")
    @ApiOperation(value = "法律法规文件搜索")
    public DataResponse<List<LawResDTO>> lawSearch(@RequestParam String searchKey) {
        return DataResponse.of(lawService.lawSearch(searchKey));
    }

    @GetMapping("/searchAll")
    @ApiOperation(value = "获取所有法律法规文件")
    public DataResponse<List<File>> lawAllSearch() {
        return DataResponse.of(lawService.lawAllSearch());
    }

}
