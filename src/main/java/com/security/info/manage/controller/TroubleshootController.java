package com.security.info.manage.controller;

import com.security.info.manage.annotation.LogMaker;
import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.TroubleshootReqDTO;
import com.security.info.manage.dto.req.TroubleshootTypeReqDTO;
import com.security.info.manage.dto.res.TroubleshootResDTO;
import com.security.info.manage.dto.res.TroubleshootTypeResDTO;
import com.security.info.manage.service.TroubleshootService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author frp
 */
@Slf4j
@RestController
@RequestMapping("/troubleshoot")
@Api(tags = "隐患排查管理")
@Validated
public class TroubleshootController {

    @Resource
    private TroubleshootService troubleshootService;

    @GetMapping("/type/list")
    @ApiOperation(value = "隐患排查手册类型列表")
    public PageResponse<TroubleshootTypeResDTO> listTroubleshootType(@RequestParam(required = false) String name,
                                                                     @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(troubleshootService.listTroubleshootType(name, pageReqDTO));
    }

    @GetMapping("/type/listAll")
    @ApiOperation(value = "隐患排查手册所有类型列表")
    public DataResponse<List<TroubleshootTypeResDTO>> listAllTroubleshootType() {
        return DataResponse.of(troubleshootService.listAllTroubleshootType());
    }

    @GetMapping("/type/detail")
    @ApiOperation(value = "隐患排查手册类型详情")
    public DataResponse<TroubleshootTypeResDTO> getTroubleshootTypeDetail(@RequestParam String id) {
        return DataResponse.of(troubleshootService.getTroubleshootTypeDetail(id));
    }

    @PostMapping("/type/add")
    @ApiOperation(value = "新增隐患排查手册类型")
    @LogMaker(value = "网页端-双重预防机制隐患排查手册类型新增")
    public DataResponse<T> insertTroubleshoot(@RequestBody TroubleshootTypeReqDTO troubleshootTypeReqDTO) {
        troubleshootService.insertTroubleshootType(troubleshootTypeReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/type/modify")
    @ApiOperation(value = "修改隐患排查手册类型")
    @LogMaker(value = "网页端-双重预防机制隐患排查手册类型修改")
    public DataResponse<T> modifyTroubleshoot(@RequestBody TroubleshootTypeReqDTO troubleshootTypeReqDTO) {
        troubleshootService.modifyTroubleshootType(troubleshootTypeReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/type/delete")
    @ApiOperation(value = "删除隐患排查手册类型")
    @LogMaker(value = "网页端-双重预防机制隐患排查手册类型删除")
    public DataResponse<T> deleteTroubleshoot(@RequestBody TroubleshootTypeReqDTO troubleshootTypeReqDTO) {
        troubleshootService.deleteTroubleshootType(troubleshootTypeReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "隐患排查手册列表")
    public PageResponse<TroubleshootResDTO> listTroubleshoot(@RequestParam(required = false) String name,
                                                             @RequestParam(required = false) String typeId,
                                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(troubleshootService.listTroubleshoot(name, typeId, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "隐患排查手册详情")
    public DataResponse<TroubleshootResDTO> getTroubleshootDetail(@RequestParam String id) {
        return DataResponse.of(troubleshootService.getTroubleshootDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增隐患排查手册")
    @LogMaker(value = "网页端-双重预防机制隐患排查手册新增")
    public DataResponse<T> insertTroubleshoot(@RequestBody TroubleshootReqDTO troubleshootReqDTO) {
        troubleshootService.insertTroubleshoot(troubleshootReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "修改隐患排查手册")
    @LogMaker(value = "网页端-双重预防机制隐患排查手册修改")
    public DataResponse<T> modifyTroubleshoot(@RequestBody TroubleshootReqDTO troubleshootReqDTO) {
        troubleshootService.modifyTroubleshoot(troubleshootReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除隐患排查手册")
    @LogMaker(value = "网页端-双重预防机制隐患排查手册删除")
    public DataResponse<T> deleteTroubleshoot(@RequestBody TroubleshootReqDTO troubleshootReqDTO) {
        troubleshootService.deleteTroubleshoot(troubleshootReqDTO);
        return DataResponse.success();
    }
}
