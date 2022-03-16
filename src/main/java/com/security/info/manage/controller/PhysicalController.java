package com.security.info.manage.controller;

import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.NewUserReqDTO;
import com.security.info.manage.dto.req.PhysicalReqDTO;
import com.security.info.manage.dto.res.PhysicalResDTO;
import com.security.info.manage.dto.res.PhysicalUserResDTO;
import com.security.info.manage.entity.PhysicalFeedback;
import com.security.info.manage.service.PhysicalService;
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
@RequestMapping("/physical")
@Api(tags = "体检管理")
@Validated
public class PhysicalController {

    @Resource
    private PhysicalService physicalService;

    @GetMapping("/list")
    @ApiOperation(value = "获取体检流程列表")
    public PageResponse<PhysicalResDTO> listPhysical(@RequestParam(required = false) String sStartTime,
                                                     @RequestParam(required = false) String sEndTime,
                                                     @RequestParam(required = false) String eStartTime,
                                                     @RequestParam(required = false) String eEndTime,
                                                     @RequestParam(required = false) Integer type,
                                                     @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(physicalService.listPhysical(sStartTime, sEndTime, eStartTime, eEndTime, type, pageReqDTO));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增体检流程")
    public DataResponse<T> addPhysical(@RequestBody PhysicalReqDTO physicalReqDTO) {
        physicalService.addPhysical(physicalReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "修改体检流程")
    public DataResponse<T> modifyPhysical(@RequestBody PhysicalReqDTO physicalReqDTO) {
        physicalService.modifyPhysical(physicalReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除体检流程")
    public DataResponse<T> deletePhysical(@RequestBody PhysicalReqDTO physicalReqDTO) {
        physicalService.deletePhysical(physicalReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/newUser")
    @ApiOperation(value = "新入职人员导入")
    public DataResponse<List<NewUserReqDTO>> addNewUser(@RequestParam MultipartFile file) {
        return DataResponse.of(physicalService.addNewUser(file));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取体检流程详情")
    public DataResponse<PhysicalResDTO> getPhysicalDetail(@RequestParam String id) {
        return DataResponse.of(physicalService.getPhysicalDetail(id));
    }

    @GetMapping("/user/list")
    @ApiOperation(value = "体检用户列表")
    public PageResponse<PhysicalUserResDTO> listPhysicalUser(@RequestParam String id,
                                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(physicalService.listPhysicalUser(id, pageReqDTO));
    }

    @GetMapping("/user/detail")
    @ApiOperation(value = "获取体检用户详情")
    public DataResponse<PhysicalUserResDTO> getPhysicalUserDetail(@RequestParam String id) {
        return DataResponse.of(physicalService.getPhysicalUserDetail(id));
    }

    @GetMapping("/user/review")
    @ApiOperation(value = "发起复检")
    public DataResponse<T> userReview(@RequestParam String id) {
        physicalService.userReview(id);
        return DataResponse.success();
    }

    @PostMapping("/file/word")
    @ApiOperation(value = "检查结果word文件上传")
    public DataResponse<T> uploadWord(@RequestParam MultipartFile file, String bizCode, String id) throws Exception {
        physicalService.uploadWord(file, bizCode, id);
        return DataResponse.success();
    }

    @PostMapping("/file/pdf")
    @ApiOperation(value = "检查结果pdf文件上传")
    public DataResponse<T> uploadPdf(@RequestParam MultipartFile file, String bizCode, String id) throws Exception {
        physicalService.uploadPdf(file, bizCode, id);
        return DataResponse.success();
    }

    @PostMapping("/feedback/add")
    @ApiOperation(value = "新增体检反馈")
    public DataResponse<T> addFeedback(@RequestBody PhysicalFeedback physicalFeedback) {
        physicalService.addFeedback(physicalFeedback);
        return DataResponse.success();
    }

    @PostMapping("/feedback/modify")
    @ApiOperation(value = "审批体检反馈")
    public DataResponse<T> modifyFeedback(@RequestBody PhysicalFeedback physicalFeedback) {
        physicalService.modifyFeedback(physicalFeedback);
        return DataResponse.success();
    }
}
