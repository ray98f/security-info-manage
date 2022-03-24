package com.security.info.manage.controller;

import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.RiskApplianceReqDTO;
import com.security.info.manage.dto.res.RiskApplianceResDTO;
import com.security.info.manage.service.RiskApplianceService;
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
@RequestMapping("/risk/appliance")
@Api(tags = "风险器具管理")
@Validated
public class RiskApplianceController {

    @Resource
    private RiskApplianceService riskApplianceService;

    @GetMapping("/list")
    @ApiOperation(value = "获取风险器具列表")
    public PageResponse<RiskApplianceResDTO> listRiskAppliance(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(riskApplianceService.listRiskAppliance(pageReqDTO));
    }

    @GetMapping("/listAll")
    @ApiOperation(value = "获取所有风险器具列表")
    public DataResponse<List<RiskApplianceResDTO>> listAllRiskAppliance() {
        return DataResponse.of(riskApplianceService.listAllRiskAppliance());
    }

    @PostMapping("/modify")
    @ApiOperation(value = "风险器具修改")
    public DataResponse<T> modifyRiskAppliance(@RequestBody RiskApplianceReqDTO riskApplianceReqDTO) {
        riskApplianceService.modifyRiskAppliance(riskApplianceReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/add")
    @ApiOperation(value = "风险器具新增")
    public DataResponse<T> addRiskAppliance(@RequestBody RiskApplianceReqDTO riskApplianceReqDTO) {
        riskApplianceService.addRiskAppliance(riskApplianceReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/import")
    @ApiOperation(value = "风险器具导入")
    public DataResponse<T> importRiskAppliance(@RequestParam MultipartFile file) {
        riskApplianceService.importRiskAppliance(file);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "风险器具删除")
    public DataResponse<T> deleteRiskAppliance(@RequestBody RiskApplianceReqDTO riskApplianceReqDTO) {
        riskApplianceService.deleteRiskAppliance(riskApplianceReqDTO);
        return DataResponse.success();
    }
}
