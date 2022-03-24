package com.security.info.manage.controller;

import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.PostReqDTO;
import com.security.info.manage.dto.req.SpecialtyReqDTO;
import com.security.info.manage.dto.res.DeptTreeResDTO;
import com.security.info.manage.dto.res.PostResDTO;
import com.security.info.manage.dto.res.SpecialtyResDTO;
import com.security.info.manage.service.DeptService;
import com.security.info.manage.service.SpecialtyService;
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
@RequestMapping("/specialty")
@Api(tags = "专业管理")
@Validated
public class SpecialtyController {

    @Resource
    private SpecialtyService specialtyService;

    @GetMapping("/list")
    @ApiOperation(value = "获取专业列表")
    public PageResponse<SpecialtyResDTO> listSpecialty(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(specialtyService.listSpecialty(pageReqDTO));
    }

    @GetMapping("/listAll")
    @ApiOperation(value = "获取所有专业列表")
    public DataResponse<List<SpecialtyResDTO>> listAllSpecialty() {
        return DataResponse.of(specialtyService.listAllSpecialty());
    }

    @PostMapping("/modify")
    @ApiOperation(value = "专业修改")
    public DataResponse<T> modifySpecialty(@RequestBody SpecialtyReqDTO specialtyReqDTO) {
        specialtyService.modifySpecialty(specialtyReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/add")
    @ApiOperation(value = "专业新增")
    public DataResponse<T> addSpecialty(@RequestBody SpecialtyReqDTO specialtyReqDTO) {
        specialtyService.addSpecialty(specialtyReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/import")
    @ApiOperation(value = "专业导入")
    public DataResponse<T> importSpecialty(@RequestParam MultipartFile file) {
        specialtyService.importSpecialty(file);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "专业删除")
    public DataResponse<T> deleteSpecialty(@RequestBody SpecialtyReqDTO specialtyReqDTO) {
        specialtyService.deleteSpecialty(specialtyReqDTO);
        return DataResponse.success();
    }
}
