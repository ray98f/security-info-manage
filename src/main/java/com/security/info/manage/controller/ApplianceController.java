package com.security.info.manage.controller;

import com.security.info.manage.annotation.LogMaker;
import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.ApplianceConfigReqDTO;
import com.security.info.manage.dto.req.ApplianceReqDTO;
import com.security.info.manage.dto.req.ApplianceTypeReqDTO;
import com.security.info.manage.dto.res.*;
import com.security.info.manage.service.ApplianceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @author frp
 */
@Slf4j
@RestController
@RequestMapping("/appliance")
@Api(tags = "劳保用品管理")
@Validated
public class ApplianceController {

    @Resource
    private ApplianceService applianceService;

    @GetMapping("/list")
    @ApiOperation(value = "劳保用品列表")
    public PageResponse<ApplianceResDTO> listAppliance(@RequestParam(required = false) String name,
                                                       @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(applianceService.listAppliance(name, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "劳保用品详情")
    public DataResponse<ApplianceResDTO> getApplianceDetail(@RequestParam String id) {
        return DataResponse.of(applianceService.getApplianceDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增劳保用品")
    @LogMaker(value = "网页端-双重预防机制劳保用品新增")
    public DataResponse<T> insertAppliance(@RequestBody ApplianceReqDTO applianceReqDTO) {
        applianceService.insertAppliance(applianceReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "修改劳保用品")
    @LogMaker(value = "网页端-双重预防机制劳保用品修改")
    public DataResponse<T> modifyAppliance(@RequestBody ApplianceReqDTO applianceReqDTO) {
        applianceService.modifyAppliance(applianceReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除劳保用品")
    @LogMaker(value = "网页端-双重预防机制劳保用品删除")
    public DataResponse<T> deleteAppliance(@RequestBody ApplianceReqDTO applianceReqDTO) {
        applianceService.deleteAppliance(applianceReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/user/import")
    @ApiOperation(value = "劳保用品配备导入")
    @LogMaker(value = "网页端-双重预防机制劳保用品配备导入")
    public DataResponse<T> importApplianceConfig(@RequestParam MultipartFile file) throws Exception {
        applianceService.importApplianceConfig(file);
        return DataResponse.success();
    }

    @PostMapping("/user/export")
    @ApiOperation(value = "劳保用品配备导出")
    @LogMaker(value = "网页端-双重预防机制劳保用品配备导出")
    public void exportApplianceConfig(HttpServletResponse response) {
        applianceService.exportApplianceConfig(response);
    }

    @GetMapping("/user/list")
    @ApiOperation(value = "劳保用品配备列表")
    public PageResponse<ApplianceConfigResDTO> listApplianceConfig(@RequestParam(required = false) String name,
                                                                   @RequestParam(required = false) Integer status,
                                                                   @RequestParam(required = false) String startTime,
                                                                   @RequestParam(required = false) String endTime,
                                                                   @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(applianceService.listApplianceConfig(pageReqDTO, name, status, startTime, endTime));
    }

    @GetMapping("/vx/user/list")
    @ApiOperation(value = "微信端-劳保用品配备列表")
    public PageResponse<ApplianceConfigResDTO> vxListApplianceConfig(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(applianceService.vxListApplianceConfig(pageReqDTO));
    }

    @GetMapping("/vx/user/confirm")
    @ApiOperation(value = "微信端-劳保用品配备确认")
    @LogMaker(value = "微信小程序-双重预防机制劳保用品配备确认")
    public DataResponse<T> vxConfirmApplianceConfig(@RequestParam String id) {
        applianceService.vxConfirmApplianceConfig(id);
        return DataResponse.success();
    }

    @GetMapping("/user/detail")
    @ApiOperation(value = "劳保用品配备详情")
    public DataResponse<ApplianceConfigResDTO> getApplianceConfigDetail(@RequestParam String id) {
        return DataResponse.of(applianceService.getApplianceConfigDetail(id));
    }

    @PostMapping("/user/delete")
    @ApiOperation(value = "删除劳保用品配备")
    @LogMaker(value = "网页端-双重预防机制劳保用品配备删除")
    public DataResponse<T> deleteApplianceConfig(@RequestBody ApplianceConfigResDTO applianceConfigResDTO) {
        applianceService.deleteApplianceConfig(applianceConfigResDTO.getId());
        return DataResponse.success();
    }

    @PostMapping("/user/change")
    @ApiOperation(value = "更换劳保用品")
    @LogMaker(value = "网页端-双重预防机制更换劳保用品")
    public DataResponse<T> changeAppliance(@RequestBody ApplianceConfigReqDTO applianceConfigReqDTO) {
        applianceService.changeAppliance(applianceConfigReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/warn/list")
    @ApiOperation(value = "获取劳保到期预警列表")
    public PageResponse<ApplianceWarnResDTO> listApplianceWarn(@RequestParam(required = false) String deptId,
                                                               @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(applianceService.listApplianceWarn(deptId, pageReqDTO));
    }

    @PostMapping("/warn/handle")
    @ApiOperation(value = "处理劳保到期预警")
    @LogMaker(value = "网页端-双重预防机制处理劳保到期预警(批量)")
    public DataResponse<T> handleApplianceWarn(@RequestBody List<String> ids) {
        applianceService.handleApplianceWarn(ids);
        return DataResponse.success();
    }

    @GetMapping("/user/archives")
    @ApiOperation(value = "获取员工劳保用品领用记录")
    public DataResponse<List<ApplianceConfigResDTO>> userArchives(@RequestParam String id) {
        return DataResponse.of(applianceService.userArchives(id));
    }
}
