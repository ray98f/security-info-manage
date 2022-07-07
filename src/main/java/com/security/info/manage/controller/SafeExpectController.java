package com.security.info.manage.controller;

import com.security.info.manage.annotation.LogMaker;
import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.SafeExpectModifyReqDTO;
import com.security.info.manage.dto.req.SafeExpectReqDTO;
import com.security.info.manage.dto.res.SafeExpectResDTO;
import com.security.info.manage.dto.res.SafeExpectTemplateResDTO;
import com.security.info.manage.dto.res.SafeExpectUserResDTO;
import com.security.info.manage.entity.File;
import com.security.info.manage.service.SafeExpectService;
import com.security.info.manage.utils.DocUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
@Slf4j
@RestController
@RequestMapping("/safe/expect")
@Api(tags = "安全预想会管理")
@Validated
public class SafeExpectController {

    @Resource
    private SafeExpectService safeExpectService;

    @GetMapping("/list")
    @ApiOperation(value = "获取安全预想会列表")
    public PageResponse<SafeExpectResDTO> listSafeExpect(@RequestParam(required = false) String name,
                                                         @RequestParam(required = false) String startTime,
                                                         @RequestParam(required = false) String endTime,
                                                         @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(safeExpectService.listSafeExpect(pageReqDTO, startTime, endTime, name));
    }

    @GetMapping("/vx/list")
    @ApiOperation(value = "微信端-获取我的安全预想会列表")
    public PageResponse<SafeExpectResDTO> vxListSafeExpect(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(safeExpectService.vxListSafeExpect(pageReqDTO));
    }

    @GetMapping("/vx/detail")
    @ApiOperation(value = "微信端-获取安全预想会详情")
    public DataResponse<SafeExpectResDTO> getSafeExpectVxDetail(@RequestParam String id) {
        return DataResponse.of(safeExpectService.getSafeExpectVxDetail(id));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取安全预想会详情")
    public DataResponse<SafeExpectResDTO> getSafeExpectDetail(@RequestParam String id) {
        return DataResponse.of(safeExpectService.getSafeExpectDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "安全预想会新增")
    @LogMaker(value = "网页端-双重预防机制安全预想会新增")
    public DataResponse<T> addSafeExpect(@RequestBody SafeExpectReqDTO safeExpectReqDTO) {
        safeExpectService.addSafeExpect(safeExpectReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/template")
    @ApiOperation(value = "获取用户安全预想会模板")
    public DataResponse<List<SafeExpectTemplateResDTO>> listSafeExpectTemplate(@RequestParam String riskId) {
        return DataResponse.of(safeExpectService.listSafeExpectTemplate(riskId));
    }

    @PostMapping("/modify")
    @ApiOperation(value = "安全预想会编辑")
    @LogMaker(value = "网页端-双重预防机制安全预想会编辑")
    public DataResponse<T> modifySafeExpect(@RequestBody SafeExpectModifyReqDTO safeExpectModifyReqDTO) {
        safeExpectService.modifySafeExpect(safeExpectModifyReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "安全预想会删除")
    @LogMaker(value = "网页端-双重预防机制安全预想会删除")
    public DataResponse<T> deleteSafeExpect(@RequestBody SafeExpectReqDTO safeExpectReqDTO) {
        safeExpectService.deleteSafeExpect(safeExpectReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "安全预想会取消")
    @LogMaker(value = "网页端-双重预防机制安全预想会取消")
    public DataResponse<T> cancelSafeExpect(@RequestBody SafeExpectReqDTO safeExpectReqDTO) {
        safeExpectService.cancelSafeExpect(safeExpectReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/user/list")
    @ApiOperation(value = "安全预想会人员签到列表")
    public PageResponse<SafeExpectUserResDTO> listSafeExpectUser(@RequestParam String id,
                                                                 @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(safeExpectService.listSafeExpectUser(id, pageReqDTO));
    }

    @PostMapping("/user/sign")
    @ApiOperation(value = "安全预想会人员签到")
    @LogMaker(value = "网页端-双重预防机制安全预想会人员签到")
    public DataResponse<T> signSafeExpectUser(@RequestBody SafeExpectUserResDTO safeExpectUserResDTO) throws ParseException {
        safeExpectService.signSafeExpectUser(safeExpectUserResDTO);
        return DataResponse.success();
    }

    @GetMapping("/vx/user/sign")
    @ApiOperation(value = "微信端-安全预想会人员签到")
    @LogMaker(value = "微信小程序-双重预防机制安全预想会人员签到")
    public DataResponse<T> vxSignSafeExpectUser(@RequestParam String id,
                                                @RequestParam Integer isSign) {
        safeExpectService.vxSignSafeExpectUser(id, isSign);
        return DataResponse.success();
    }

    @GetMapping("/export/data")
    @ApiOperation(value = "安全预想会模板数据")
    public DataResponse<Map<String, Object>> exportSafeExpectData(@RequestParam String id) {
        return DataResponse.of(safeExpectService.exportSafeExpectData(id));
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出安全预想会word")
    @LogMaker(value = "网页端-双重预防机制导出安全预想会word")
    public DataResponse<File> exportSafeExpect(@RequestParam String id) throws Exception {
        return DataResponse.of(safeExpectService.exportSafeExpect(id));
    }
}
