package com.security.info.manage.controller;

import com.security.info.manage.annotation.LogMaker;
import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.DangerReqDTO;
import com.security.info.manage.dto.res.*;
import com.security.info.manage.entity.EntryPlate;
import com.security.info.manage.service.DangerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author frp
 */
@Slf4j
@RestController
@RequestMapping("/danger")
@Api(tags = "隐患管理")
@Validated
public class DangerController {

    @Resource
    private DangerService dangerService;

    @GetMapping("/listDept")
    @ApiOperation(value = "获取隐患部门列表")
    public DataResponse<List<DeptTreeResDTO>> listDept(@RequestParam Integer type) {
        return DataResponse.of(dangerService.listDept(type));
    }

    @GetMapping("/listPlate")
    @ApiOperation(value = "获取隐患板块列表")
    public DataResponse<List<EntryPlate>> listPlate(@RequestParam(required = false) Integer type,
                                                    @RequestParam(required = false) String deptId,
                                                    @RequestParam(required = false) String deptName) {
        return DataResponse.of(dangerService.listPlate(type, deptId, deptName));
    }

    @PostMapping("/addPlate")
    @ApiOperation(value = "新增隐患板块")
    public DataResponse<T> addPlate(@RequestBody EntryPlate entryPlate) {
        dangerService.addPlate(entryPlate);
        return DataResponse.success();
    }

    @PostMapping("/modifyPlate")
    @ApiOperation(value = "修改隐患板块")
    public DataResponse<T> modifyPlate(@RequestBody EntryPlate entryPlate) {
        dangerService.modifyPlate(entryPlate);
        return DataResponse.success();
    }

    @PostMapping("/deletePlate")
    @ApiOperation(value = "删除隐患板块")
    public DataResponse<T> deletePlate(@RequestBody EntryPlate entryPlate) {
        dangerService.deletePlate(entryPlate);
        return DataResponse.success();
    }

    @GetMapping("/listEntry")
    @ApiOperation(value = "获取隐患词条列表")
    public DataResponse<List<EntryPlate.Entry>> listEntry(@RequestParam String plateId) {
        return DataResponse.of(dangerService.listEntry(plateId));
    }

    @PostMapping("/addEntry")
    @ApiOperation(value = "新增隐患词条")
    public DataResponse<T> addEntry(@RequestBody EntryPlate.Entry entry) {
        dangerService.addEntry(entry);
        return DataResponse.success();
    }

    @PostMapping("/modifyEntry")
    @ApiOperation(value = "修改隐患词条")
    public DataResponse<T> modifyEntry(@RequestBody EntryPlate.Entry entry) {
        dangerService.modifyEntry(entry);
        return DataResponse.success();
    }

    @PostMapping("/deleteEntry")
    @ApiOperation(value = "删除隐患词条")
    public DataResponse<T> deleteEntry(@RequestBody EntryPlate.Entry entry) {
        dangerService.deleteEntry(entry);
        return DataResponse.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取隐患列表")
    public PageResponse<DangerResDTO> listDanger(@RequestParam(required = false) Integer type,
                                                 @RequestParam(required = false) Integer status,
                                                 @RequestParam(required = false) String name,
                                                 @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(dangerService.listDanger(type, status, name, pageReqDTO));
    }

    @GetMapping("/vx/list")
    @ApiOperation(value = "微信端-获取我的隐患列表")
    public PageResponse<DangerResDTO> vxListDanger(@RequestParam @ApiParam(value = "1 我上报的 2 我整改的 3 我审核的") Integer type,
                                                   @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(dangerService.vxListDanger(type, pageReqDTO));
    }

    @GetMapping("/vx/nearby")
    @ApiOperation(value = "微信端-获取附近隐患列表")
    public PageResponse<DangerResDTO> vxNearbyDanger(@RequestParam @ApiParam(value = "纬度") Double lng,
                                                     @RequestParam @ApiParam(value = "经度") Double lat,
                                                     @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(dangerService.vxNearbyDanger(lng, lat, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取隐患详情")
    public DataResponse<DangerResDTO> getDangerDetail(@RequestParam String id) {
        return DataResponse.of(dangerService.getDangerDetail(id));
    }

    @PostMapping("/modify")
    @ApiOperation(value = "隐患修改")
    @LogMaker(value = "网页端-问题检查问题排查管理修改")
    public DataResponse<T> modifyDanger(@RequestBody DangerReqDTO dangerReqDTO) {
        dangerService.modifyDanger(dangerReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/add")
    @ApiOperation(value = "隐患新增")
    @LogMaker(value = "网页端-问题检查问题排查管理新增")
    public DataResponse<T> addDanger(@RequestBody DangerReqDTO dangerReqDTO) {
        dangerService.addDanger(dangerReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "隐患删除")
    @LogMaker(value = "网页端-问题检查问题排查管理删除")
    public DataResponse<T> deleteDanger(@RequestBody DangerReqDTO dangerReqDTO) {
        dangerService.deleteDanger(dangerReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/examine/user")
    @ApiOperation(value = "审核人员列表")
    public DataResponse<List<UserResDTO>> examineUserList(@RequestParam String deptId,
                                                          @RequestParam Integer userType) {
        return DataResponse.of(dangerService.examineUserList(deptId, userType));
    }

    @GetMapping("/examine")
    @ApiOperation(value = "审核隐患")
    @LogMaker(value = "网页端-问题检查问题排查管理审核")
    public DataResponse<T> examineDanger(@RequestParam @ApiParam(value = "隐患id") String dangerId,
                                         @RequestParam(required = false) @ApiParam(value = "人员id") String userId,
                                         @RequestParam(required = false) @ApiParam(value = "整改意见") String opinion,
                                         @RequestParam @ApiParam(value = "审核状态 0 未审核 1 审核通过 2 审核不通过") Integer status) {
        dangerService.examineDanger(dangerId, userId, opinion, status);
        return DataResponse.success();
    }

    @GetMapping("/issue")
    @ApiOperation(value = "下发隐患到责任部门")
    @LogMaker(value = "网页端-问题检查问题排查管理问题下发")
    public DataResponse<T> issueDanger(@RequestParam @ApiParam(value = "隐患id") String dangerId,
                                       @RequestParam @ApiParam(value = "部门id") String deptId,
                                       @RequestParam @ApiParam(value = "人员id") String userId,
                                       @RequestParam(required = false) @ApiParam(value = "整改期限") String rectifyTerm,
                                       @RequestParam(required = false) @ApiParam(value = "整改意见") String opinion) {
        dangerService.issueDanger(dangerId, deptId, userId, rectifyTerm, opinion);
        return DataResponse.success();
    }

    @GetMapping("/rectify")
    @ApiOperation(value = "问题整改")
    @LogMaker(value = "网页端-问题检查问题排查管理问题整改")
    public DataResponse<T> rectifyDanger(@RequestParam @ApiParam(value = "隐患id") String dangerId,
                                         @RequestParam(required = false) @ApiParam(value = "人员id") String userId,
                                         @RequestParam(required = false) @ApiParam(value = "整改措施") String rectifyMeasure,
                                         @RequestParam(required = false) @ApiParam(value = "整改后图片") String afterPic) {
        dangerService.rectifyDanger(dangerId, userId, rectifyMeasure, afterPic);
        return DataResponse.success();
    }

    @GetMapping("/rectify/examine")
    @ApiOperation(value = "问题整改审核")
    @LogMaker(value = "网页端-问题检查问题排查管理问题整改审核")
    public DataResponse<T> rectifyExamineDanger(@RequestParam @ApiParam(value = "隐患id") String dangerId,
                                                @RequestParam @ApiParam(value = "审核状态 0 未审核 1 审核通过 2 审核不通过") Integer status) {
        dangerService.rectifyExamineDanger(dangerId, status);
        return DataResponse.success();
    }

    @GetMapping("/rectify/pass")
    @ApiOperation(value = "检查人通过问题整改")
    @LogMaker(value = "网页端-问题检查问题排查管理问题整改通过")
    public DataResponse<T> rectifyPassDanger(@RequestParam @ApiParam(value = "隐患id") String dangerId,
                                             @RequestParam(required = false) @ApiParam(value = "问题整改情况复查") String condition,
                                             @RequestParam @ApiParam(value = "拟稿人审核状态 0 未开始 1 审核中 2 审核通过 3 审核不通过") Integer status) {
        dangerService.rectifyPassDanger(dangerId, condition, status);
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出隐患")
    @LogMaker(value = "网页端-问题检查问题排查管理导出")
    public void exportDanger(@RequestParam(required = false) Integer type,
                             @RequestParam(required = false) Integer status,
                             @RequestParam(required = false) String name,
                             HttpServletResponse response) {
        dangerService.exportDanger(type, status, name, response);
    }

    @GetMapping("/statistics/type")
    @ApiOperation(value = "问题类别统计")
    public DataResponse<List<DangerTypeStatisticsResDTO>> dangerTypeStatistics(@RequestParam String date) {
        return DataResponse.of(dangerService.dangerTypeStatistics(date));
    }

    @GetMapping("/statistics/dept")
    @ApiOperation(value = "问题责任单位统计")
    public DataResponse<List<DangerDeptStatisticsResDTO>> dangerDeptStatistics(@RequestParam String date) {
        return DataResponse.of(dangerService.dangerDeptStatistics(date));
    }

    @GetMapping("/statistics/region")
    @ApiOperation(value = "问题区域统计")
    public DataResponse<List<DangerRegionStatisticsResDTO>> dangerRegionStatistics(@RequestParam(required = false) String regionId,
                                                                                   @RequestParam String date) {
        return DataResponse.of(dangerService.dangerRegionStatistics(regionId, date));
    }

    @GetMapping("/statistics/listUnit")
    @ApiOperation(value = "获取责任单位列表")
    public DataResponse<List<String>> listUnitStatistics(@RequestParam String regionId) {
        return DataResponse.of(dangerService.listUnitStatistics(regionId));
    }

    @GetMapping("/statistics/listWorkArea")
    @ApiOperation(value = "获取责任工区列表")
    public DataResponse<List<String>> listWorkAreaStatistics(@RequestParam String regionId) {
        return DataResponse.of(dangerService.listWorkAreaStatistics(regionId));
    }

    @GetMapping("/statistics/chart")
    @ApiOperation(value = "新增问题统计")
    public DataResponse<DangerChartStatisticsResDTO> chartStatistics(@RequestParam(required = false) String regionId,
                                                                     @RequestParam(required = false) String unit,
                                                                     @RequestParam(required = false) String workArea) {
        return DataResponse.of(dangerService.chartStatistics(regionId, unit, workArea));
    }

    @GetMapping("/statistics/month")
    @ApiOperation(value = "安全隐患类别月统计统计")
    public DataResponse<List<DangerMonthStatisticsResDTO>> dangerMonthStatistics(@RequestParam String month) {
        return DataResponse.of(dangerService.dangerMonthStatistics(month));
    }

}
