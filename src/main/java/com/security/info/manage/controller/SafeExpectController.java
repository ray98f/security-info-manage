package com.security.info.manage.controller;

import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.SafeExpectModifyReqDTO;
import com.security.info.manage.dto.req.SafeExpectReqDTO;
import com.security.info.manage.dto.res.SafeExpectResDTO;
import com.security.info.manage.dto.res.SafeExpectUserResDTO;
import com.security.info.manage.service.SafeExpectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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
    public PageResponse<SafeExpectResDTO> listSafeExpect(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(safeExpectService.listSafeExpect(pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取安全预想会详情")
    public DataResponse<SafeExpectResDTO> getSafeExpectDetail(@RequestParam String id) {
        return DataResponse.of(safeExpectService.getSafeExpectDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "安全预想会新增")
    public DataResponse<T> addSafeExpect(@RequestBody SafeExpectReqDTO safeExpectReqDTO) {
        safeExpectService.addSafeExpect(safeExpectReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "安全预想会编辑")
    public DataResponse<T> modifySafeExpect(@RequestBody SafeExpectModifyReqDTO safeExpectModifyReqDTO) {
        safeExpectService.modifySafeExpect(safeExpectModifyReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "安全预想会删除")
    public DataResponse<T> deleteSafeExpect(@RequestBody SafeExpectReqDTO safeExpectReqDTO) {
        safeExpectService.deleteSafeExpect(safeExpectReqDTO);
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
    public DataResponse<SafeExpectUserResDTO> signSafeExpectUser(@RequestBody SafeExpectUserResDTO safeExpectUserResDTO) {
        safeExpectService.signSafeExpectUser(safeExpectUserResDTO);
        return DataResponse.success();
    }

    // todo 打印预览 打印

}
