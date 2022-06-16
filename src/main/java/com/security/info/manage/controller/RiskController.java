package com.security.info.manage.controller;

import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.RiskInfoReqDTO;
import com.security.info.manage.dto.res.RiskInfoResDTO;
import com.security.info.manage.service.RiskService;
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
@RequestMapping("/risk")
@Api(tags = "风险管理")
@Validated
public class RiskController {

    @Resource
    private RiskService riskService;

    @GetMapping("/list")
    @ApiOperation(value = "获取风险列表")
    public PageResponse<RiskInfoResDTO> listRisk(@RequestParam(required = false) Integer level,
                                                 @RequestParam(required = false) Integer type,
                                                 @RequestParam(required = false) String module,
                                                 @RequestParam(required = false) String responsibilityDept,
                                                 @RequestParam(required = false) String responsibilityCenter,
                                                 @RequestParam(required = false) String responsibilityUser,
                                                 @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(riskService.listRisk(level, type, module, responsibilityDept, responsibilityCenter, responsibilityUser, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取风险详情")
    public DataResponse<RiskInfoResDTO> getRiskDetail(@RequestParam String id) {
        return DataResponse.of(riskService.getRiskDetail(id));
    }

    @PostMapping("/modify")
    @ApiOperation(value = "风险修改")
    public DataResponse<T> modifyRisk(@RequestBody RiskInfoReqDTO riskInfoReqDTO) {
        riskService.modifyRisk(riskInfoReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/add")
    @ApiOperation(value = "风险新增")
    public DataResponse<T> addRisk(@RequestBody RiskInfoReqDTO riskInfoReqDTO) {
        riskService.addRisk(riskInfoReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "风险删除")
    public DataResponse<T> deleteRisk(@RequestBody List<String> ids) {
        riskService.deleteRisk(ids);
        return DataResponse.success();
    }

    @PostMapping("/verify")
    @ApiOperation(value = "审核风险")
    public DataResponse<T> verifyRisk(@RequestBody RiskInfoReqDTO riskInfoReqDTO) {
        riskService.verifyRisk(riskInfoReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/import")
    @ApiOperation(value = "风险导入")
    public DataResponse<T> importRisk(@RequestParam MultipartFile file, Integer type) {
        riskService.importRisk(file, type);
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "风险导出")
    public void exportRisk(@RequestParam Integer type,
                           HttpServletResponse response) {
        riskService.exportRisk(response, type);
    }

}
