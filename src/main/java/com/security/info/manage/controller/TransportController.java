package com.security.info.manage.controller;

import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.TransportReqDTO;
import com.security.info.manage.dto.res.TransportResDTO;
import com.security.info.manage.service.TransportService;
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
@RequestMapping("/transport")
@Api(tags = "交通工具管理")
@Validated
public class TransportController {

    @Resource
    private TransportService transportService;

    @GetMapping("/list")
    @ApiOperation(value = "获取交通工具列表")
    public PageResponse<TransportResDTO> listTransport(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(transportService.listTransport(pageReqDTO));
    }

    @GetMapping("/listAll")
    @ApiOperation(value = "获取所有交通工具列表")
    public DataResponse<List<TransportResDTO>> listAllTransport() {
        return DataResponse.of(transportService.listAllTransport());
    }

    @PostMapping("/modify")
    @ApiOperation(value = "交通工具修改")
    public DataResponse<T> modifyTransport(@RequestBody TransportReqDTO transportReqDTO) {
        transportService.modifyTransport(transportReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/add")
    @ApiOperation(value = "交通工具新增")
    public DataResponse<T> addTransport(@RequestBody TransportReqDTO transportReqDTO) {
        transportService.addTransport(transportReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/import")
    @ApiOperation(value = "交通工具导入")
    public DataResponse<T> importTransport(@RequestParam MultipartFile file) {
        transportService.importTransport(file);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "交通工具删除")
    public DataResponse<T> deleteTransport(@RequestBody TransportReqDTO transportReqDTO) {
        transportService.deleteTransport(transportReqDTO);
        return DataResponse.success();
    }
}
