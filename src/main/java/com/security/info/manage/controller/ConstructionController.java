package com.security.info.manage.controller;

import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.ConstructionReqDTO;
import com.security.info.manage.dto.req.ConstructionTypeReqDTO;
import com.security.info.manage.dto.req.TransportReqDTO;
import com.security.info.manage.dto.req.WeekPlanReqDTO;
import com.security.info.manage.dto.res.ConstructionResDTO;
import com.security.info.manage.dto.res.ConstructionTypeResDTO;
import com.security.info.manage.dto.res.TransportResDTO;
import com.security.info.manage.dto.res.WeekPlanResDTO;
import com.security.info.manage.service.ConstructionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author frp
 */
@Slf4j
@RestController
@RequestMapping("/construction")
@Api(tags = "施工作业管理")
@Validated
public class ConstructionController {

    @Resource
    private ConstructionService constructionService;

    @GetMapping("/type/list")
    @ApiOperation(value = "获取施工作业类型列表")
    public PageResponse<ConstructionTypeResDTO> listConstructionType(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(constructionService.listConstructionType(pageReqDTO));
    }

    @GetMapping("/type/listAll")
    @ApiOperation(value = "获取所有施工作业类型列表")
    public DataResponse<List<ConstructionTypeResDTO>> listAllConstructionType() {
        return DataResponse.of(constructionService.listAllConstructionType());
    }

    @PostMapping("/type/modify")
    @ApiOperation(value = "施工作业类型修改")
    public DataResponse<T> modifyConstructionType(@RequestBody ConstructionTypeReqDTO constructionTypeReqDTO) {
        constructionService.modifyConstructionType(constructionTypeReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/type/add")
    @ApiOperation(value = "施工作业类型新增")
    public DataResponse<T> addConstructionType(@RequestBody ConstructionTypeReqDTO constructionTypeReqDTO) {
        constructionService.addConstructionType(constructionTypeReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/type/delete")
    @ApiOperation(value = "施工作业类型删除")
    public DataResponse<T> deleteConstructionType(@RequestBody ConstructionTypeReqDTO constructionTypeReqDTO) {
        constructionService.deleteConstructionType(constructionTypeReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/weekPlan/list")
    @ApiOperation(value = "获取周计划列表")
    public PageResponse<WeekPlanResDTO> listWeekPlan(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(constructionService.listWeekPlan(pageReqDTO));
    }

    @PostMapping("/weekPlan/modify")
    @ApiOperation(value = "周计划修改")
    public DataResponse<T> modifyWeekPlan(@RequestBody WeekPlanReqDTO weekPlanReqDTO) {
        constructionService.modifyWeekPlan(weekPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/weekPlan/add")
    @ApiOperation(value = "周计划新增")
    public DataResponse<T> addWeekPlan(@RequestBody WeekPlanReqDTO weekPlanReqDTO) {
        constructionService.addWeekPlan(weekPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/weekPlan/delete")
    @ApiOperation(value = "周计划删除")
    public DataResponse<T> deleteWeekPlan(@RequestBody WeekPlanReqDTO weekPlanReqDTO) {
        constructionService.deleteWeekPlan(weekPlanReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取施工作业列表")
    public PageResponse<ConstructionResDTO> listConstruction(@RequestParam @ApiParam(value = "周计划id") String planId,
                                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(constructionService.listConstruction(planId, pageReqDTO));
    }

    @PostMapping("/add")
    @ApiOperation(value = "施工作业新增")
    public DataResponse<T> addConstruction(@RequestBody ConstructionReqDTO constructionReqDTO) {
        constructionService.addConstruction(constructionReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/import")
    @ApiOperation(value = "施工作业导入")
    public DataResponse<T> importConstruction(@RequestParam MultipartFile file, String planId) {
        constructionService.importConstruction(file, planId);
        return DataResponse.success();
    }
}
