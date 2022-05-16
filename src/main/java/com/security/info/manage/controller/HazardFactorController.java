package com.security.info.manage.controller;

import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.PostHazardFactorReqDTO;
import com.security.info.manage.dto.res.DeptTreeResDTO;
import com.security.info.manage.dto.res.PostHazardFactorResDTO;
import com.security.info.manage.entity.HazardFactor;
import com.security.info.manage.service.DeptService;
import com.security.info.manage.service.HazardFactorService;
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
@RequestMapping("/hazard/factor")
@Api(tags = "危害因素管理")
@Validated
public class HazardFactorController {

    @Resource
    private HazardFactorService hazardFactorService;

    @GetMapping("/list")
    @ApiOperation(value = "获取禁忌证危害因素列表")
    public PageResponse<HazardFactor> listHazardFactor(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(hazardFactorService.listHazardFactor(pageReqDTO));
    }

    @GetMapping("/listAll")
    @ApiOperation(value = "获取所有禁忌证危害因素列表")
    public DataResponse<List<HazardFactor>> listAllHazardFactor() {
        return DataResponse.of(hazardFactorService.listAllHazardFactor());
    }

    @PostMapping("/import")
    @ApiOperation(value = "禁忌证危害因素导入")
    public DataResponse<T> importHazardFactor(@RequestParam MultipartFile file) {
        hazardFactorService.importHazardFactor(file);
        return DataResponse.success();
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增禁忌证危害因素")
    public DataResponse<T> addHazardFactor(@RequestBody HazardFactor hazardFactor) {
        hazardFactorService.addHazardFactor(hazardFactor);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "编辑禁忌证危害因素")
    public DataResponse<T> modifyHazardFactor(@RequestBody HazardFactor hazardFactor) {
        hazardFactorService.modifyHazardFactor(hazardFactor);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除禁忌证危害因素")
    public DataResponse<T> deleteHazardFactor(@RequestBody HazardFactor hazardFactor) {
        hazardFactorService.deleteHazardFactor(hazardFactor);
        return DataResponse.success();
    }

    @PostMapping("/post/add")
    @ApiOperation(value = "新增岗位危害因素配置")
    public DataResponse<T> addPostHazardFactor(@RequestBody PostHazardFactorReqDTO postHazardFactorReqDTO) {
        hazardFactorService.addPostHazardFactor(postHazardFactorReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/post/modify")
    @ApiOperation(value = "修改岗位危害因素配置")
    public DataResponse<T> modifyPostHazardFactor(@RequestBody PostHazardFactorReqDTO postHazardFactorReqDTO) {
        hazardFactorService.modifyPostHazardFactor(postHazardFactorReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/post/delete")
    @ApiOperation(value = "删除岗位危害因素配置")
    public DataResponse<T> deletePostHazardFactor(@RequestBody PostHazardFactorReqDTO postHazardFactorReqDTO) {
        hazardFactorService.deletePostHazardFactor(postHazardFactorReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/post/list")
    @ApiOperation(value = "岗位危害因素配置列表")
    public PageResponse<PostHazardFactorResDTO> listPostHazardFactor(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(hazardFactorService.listPostHazardFactor(pageReqDTO));
    }
}
