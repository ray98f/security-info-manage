package com.security.info.manage.controller;

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
    public PageResponse<ApplianceResDTO> listAppliance(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(applianceService.listAppliance(pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "劳保用品详情")
    public DataResponse<ApplianceResDTO> getApplianceDetail(@RequestParam String id) {
        return DataResponse.of(applianceService.getApplianceDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增劳保用品")
    public DataResponse<T> insertAppliance(@RequestBody ApplianceReqDTO applianceReqDTO) {
        applianceService.insertAppliance(applianceReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "修改劳保用品")
    public DataResponse<T> modifyAppliance(@RequestBody ApplianceReqDTO applianceReqDTO) {
        applianceService.modifyAppliance(applianceReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除劳保用品")
    public DataResponse<T> deleteAppliance(@RequestBody ApplianceReqDTO applianceReqDTO) {
        applianceService.deleteAppliance(applianceReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/user/import")
    @ApiOperation(value = "劳保用品配备导入")
    public DataResponse<T> importApplianceConfig(@RequestParam MultipartFile file) {
        applianceService.importApplianceConfig(file);
        return DataResponse.success();
    }

    @GetMapping("/user/list")
    @ApiOperation(value = "劳保用品配备列表")
    public PageResponse<ApplianceConfigResDTO> listApplianceConfig(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(applianceService.listApplianceConfig(pageReqDTO));
    }

    @GetMapping("/user/detail")
    @ApiOperation(value = "劳保用品配备详情")
    public DataResponse<ApplianceConfigResDTO> getApplianceConfigDetail(@RequestParam String id) {
        return DataResponse.of(applianceService.getApplianceConfigDetail(id));
    }

    @PostMapping("/user/change")
    @ApiOperation(value = "更换劳保用品")
    public DataResponse<T> changeAppliance(@RequestBody ApplianceConfigReqDTO applianceConfigReqDTO) {
        applianceService.changeAppliance(applianceConfigReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/warn/list")
    @ApiOperation(value = "获取劳保到期预警列表")
    public PageResponse<ApplianceWarnResDTO> listApplianceWarn(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(applianceService.listApplianceWarn(pageReqDTO));
    }

    @PostMapping("/warn/handle")
    @ApiOperation(value = "处理劳保到期预警")
    public DataResponse<T> handleApplianceWarn(@RequestBody ApplianceWarnResDTO applianceWarnResDTO) {
        applianceService.handleApplianceWarn(applianceWarnResDTO.getId());
        return DataResponse.success();
    }

    @GetMapping("/user/archives")
    @ApiOperation(value = "获取员工劳保用品领用记录")
    public DataResponse<List<ApplianceConfigResDTO>> userArchives(@RequestParam String id) {
        return DataResponse.of(applianceService.userArchives(id));
    }
}
