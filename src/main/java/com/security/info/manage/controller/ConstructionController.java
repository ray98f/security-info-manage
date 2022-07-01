package com.security.info.manage.controller;

import com.security.info.manage.annotation.LogMaker;
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
    @LogMaker(value = "网页端-双重预防机制施工作业类型修改")
    public DataResponse<T> modifyConstructionType(@RequestBody ConstructionTypeReqDTO constructionTypeReqDTO) {
        constructionService.modifyConstructionType(constructionTypeReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/type/add")
    @ApiOperation(value = "施工作业类型新增")
    @LogMaker(value = "网页端-双重预防机制施工作业类型新增")
    public DataResponse<T> addConstructionType(@RequestBody ConstructionTypeReqDTO constructionTypeReqDTO) {
        constructionService.addConstructionType(constructionTypeReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/type/delete")
    @ApiOperation(value = "施工作业类型删除")
    @LogMaker(value = "网页端-双重预防机制施工作业类型删除")
    public DataResponse<T> deleteConstructionType(@RequestBody ConstructionTypeReqDTO constructionTypeReqDTO) {
        constructionService.deleteConstructionType(constructionTypeReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/weekPlan/list")
    @ApiOperation(value = "获取周计划列表")
    public PageResponse<WeekPlanResDTO> listWeekPlan(@RequestParam(required = false) @ApiParam(value = "开始时间") String startTime,
                                                     @RequestParam(required = false) @ApiParam(value = "结束时间") String endTime,
                                                     @RequestParam(required = false) @ApiParam(value = "作业名称") String name,
                                                     @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(constructionService.listWeekPlan(pageReqDTO, startTime, endTime, name));
    }

    @PostMapping("/weekPlan/modify")
    @ApiOperation(value = "周计划修改")
    @LogMaker(value = "网页端-双重预防机制周计划修改")
    public DataResponse<T> modifyWeekPlan(@RequestBody WeekPlanReqDTO weekPlanReqDTO) {
        constructionService.modifyWeekPlan(weekPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/weekPlan/add")
    @ApiOperation(value = "周计划新增")
    @LogMaker(value = "网页端-双重预防机制周计划新增")
    public DataResponse<T> addWeekPlan(@RequestBody WeekPlanReqDTO weekPlanReqDTO) {
        constructionService.addWeekPlan(weekPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/weekPlan/delete")
    @ApiOperation(value = "周计划删除")
    @LogMaker(value = "网页端-双重预防机制周计划删除")
    public DataResponse<T> deleteWeekPlan(@RequestBody WeekPlanReqDTO weekPlanReqDTO) {
        constructionService.deleteWeekPlan(weekPlanReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取施工作业列表")
    public PageResponse<ConstructionResDTO> listConstruction(@RequestParam(required = false) @ApiParam(value = "周计划id") String planId,
                                                             @RequestParam(required = false) @ApiParam(value = "开始时间") String startTime,
                                                             @RequestParam(required = false) @ApiParam(value = "结束时间") String endTime,
                                                             @RequestParam(required = false) @ApiParam(value = "作业名称") String name,
                                                             @RequestParam(required = false) @ApiParam(value = "周计划名称") String planName,
                                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(constructionService.listConstruction(planId, startTime, endTime, name, planName, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取施工作业详情")
    public DataResponse<ConstructionResDTO> getConstructionDetail(@RequestParam String id) {
        return DataResponse.of(constructionService.getConstructionDetail(id));
    }

    @GetMapping("/vx/list")
    @ApiOperation(value = "微信端-我的施工作业列表")
    public PageResponse<ConstructionResDTO> vxListConstruction(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(constructionService.vxListConstruction(pageReqDTO));
    }

    @PostMapping("/add")
    @ApiOperation(value = "施工作业新增")
    @LogMaker(value = "网页端-双重预防机制周计划删除")
    public DataResponse<T> addConstruction(@RequestBody ConstructionReqDTO constructionReqDTO) {
        constructionService.addConstruction(constructionReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "施工作业修改")
    @LogMaker(value = "网页端-双重预防机制施工作业修改")
    public DataResponse<T> modifyConstruction(@RequestBody ConstructionReqDTO constructionReqDTO) {
        constructionService.modifyConstruction(constructionReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "施工作业删除")
    @LogMaker(value = "网页端-双重预防机制施工作业删除")
    public DataResponse<T> deleteConstruction(@RequestBody List<String> ids) {
        constructionService.deleteConstruction(ids);
        return DataResponse.success();
    }

    @PostMapping("/import")
    @ApiOperation(value = "施工作业导入")
    @LogMaker(value = "网页端-双重预防机制施工作业导入")
    public DataResponse<T> importConstruction(@RequestParam MultipartFile file, String planId) {
        constructionService.importConstruction(file, planId);
        return DataResponse.success();
    }
}
