package com.security.info.manage.controller;

import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.ApplianceConfigReqDTO;
import com.security.info.manage.dto.req.ApplianceTypeReqDTO;
import com.security.info.manage.dto.res.ApplianceConfigResDTO;
import com.security.info.manage.dto.res.ApplianceTypeTreeResDTO;
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

    @GetMapping("/type/listTree")
    @ApiOperation(value = "劳保用品类别获取")
    public DataResponse<List<ApplianceTypeTreeResDTO>> listTypeTree(@RequestParam(required = false) Integer status) {
        return DataResponse.of(applianceService.listTypeTree(status));
    }

    @GetMapping("/type/detail")
    @ApiOperation(value = "劳保用品类别详情")
    public DataResponse<ApplianceTypeTreeResDTO> getTypeDetail(@RequestParam String id) {
        return DataResponse.of(applianceService.getTypeDetail(id));
    }

    @PostMapping("/type/add")
    @ApiOperation(value = "劳保用品类别新增")
    public DataResponse<T> addType(@RequestBody ApplianceTypeReqDTO applianceTypeReqDTO) {
        applianceService.addType(applianceTypeReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/type/modify")
    @ApiOperation(value = "劳保用品类别修改")
    public DataResponse<T> modifyType(@RequestBody ApplianceTypeReqDTO applianceTypeReqDTO) {
        applianceService.modifyType(applianceTypeReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/type/delete")
    @ApiOperation(value = "劳保用品类别删除")
    public DataResponse<T> deleteType(@RequestBody ApplianceTypeReqDTO applianceTypeReqDTO) {
        applianceService.deleteType(applianceTypeReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/import")
    @ApiOperation(value = "劳保用品配备导入")
    public DataResponse<T> importAppliance(@RequestParam MultipartFile file) {
        applianceService.importAppliance(file);
        return DataResponse.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "劳保用品配备列表")
    public PageResponse<ApplianceConfigResDTO> listApplianceConfig(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(applianceService.listApplianceConfig(pageReqDTO));
    }

    @PostMapping("/change")
    @ApiOperation(value = "劳保用品配备列表")
    public DataResponse<T> changeAppliance(@RequestBody ApplianceConfigReqDTO applianceConfigReqDTO) {
        applianceService.changeAppliance(applianceConfigReqDTO);
        return DataResponse.success();
    }
}
