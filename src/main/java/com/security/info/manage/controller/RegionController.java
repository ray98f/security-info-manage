package com.security.info.manage.controller;

import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.RegionReqDTO;
import com.security.info.manage.dto.req.RegionTypeReqDTO;
import com.security.info.manage.dto.res.RegionResDTO;
import com.security.info.manage.dto.res.RegionTypeResDTO;
import com.security.info.manage.service.RegionService;
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
@RequestMapping("/region")
@Api(tags = "区域管理")
@Validated
public class RegionController {

    @Resource
    private RegionService regionService;

    @GetMapping("/type/list")
    @ApiOperation(value = "获取区域类型列表")
    public PageResponse<RegionTypeResDTO> listRegionType(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(regionService.listRegionType(pageReqDTO));
    }

    @GetMapping("/type/listAll")
    @ApiOperation(value = "获取所有区域类型列表")
    public DataResponse<List<RegionTypeResDTO>> listAllRegionType() {
        return DataResponse.of(regionService.listAllRegionType());
    }

    @PostMapping("/type/modify")
    @ApiOperation(value = "区域类型修改")
    public DataResponse<T> modifyRegionType(@RequestBody RegionTypeReqDTO regionTypeReqDTO) {
        regionService.modifyRegionType(regionTypeReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/type/add")
    @ApiOperation(value = "区域类型新增")
    public DataResponse<T> addRegionType(@RequestBody RegionTypeReqDTO regionTypeReqDTO) {
        regionService.addRegionType(regionTypeReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/type/delete")
    @ApiOperation(value = "区域类型删除")
    public DataResponse<T> deleteRegionType(@RequestBody RegionTypeReqDTO regionTypeReqDTO) {
        regionService.deleteRegionType(regionTypeReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取区域列表")
    public PageResponse<RegionResDTO> listRegion(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(regionService.listRegion(pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取区域详情")
    public DataResponse<RegionResDTO> getRegionDetail(@RequestParam String id) {
        return DataResponse.of(regionService.getRegionDetail(id));
    }

    @GetMapping("/listAll")
    @ApiOperation(value = "获取所有区域列表")
    public DataResponse<List<RegionResDTO>> listAllRegion() {
        return DataResponse.of(regionService.listAllRegion());
    }

    @PostMapping("/modify")
    @ApiOperation(value = "区域修改")
    public DataResponse<T> modifyRegion(@RequestBody RegionReqDTO regionReqDTO) {
        regionService.modifyRegion(regionReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/add")
    @ApiOperation(value = "区域新增")
    public DataResponse<T> addRegion(@RequestBody RegionReqDTO regionReqDTO) {
        regionService.addRegion(regionReqDTO);
        return DataResponse.success();
    }

}