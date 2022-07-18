package com.security.info.manage.controller;

import com.security.info.manage.annotation.LogMaker;
import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.NewUserReqDTO;
import com.security.info.manage.dto.req.PhysicalReqDTO;
import com.security.info.manage.dto.res.*;
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
import javax.servlet.http.HttpServletResponse;
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

    @GetMapping("/export")
    @ApiOperation(value = "体检流程导出")
    @LogMaker(value = "网页端-职业健康体检流程导出")
    public void exportPhysical(@RequestParam(required = false) String sStartTime,
                               @RequestParam(required = false) String sEndTime,
                               @RequestParam(required = false) String eStartTime,
                               @RequestParam(required = false) String eEndTime,
                               @RequestParam(required = false) Integer type,
                               HttpServletResponse response) {
        physicalService.exportPhysical(sStartTime, sEndTime, eStartTime, eEndTime, type, response);
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取体检流程列表")
    public PageResponse<PhysicalResDTO> listPhysical(@RequestParam(required = false) String sStartTime,
                                                     @RequestParam(required = false) String sEndTime,
                                                     @RequestParam(required = false) String eStartTime,
                                                     @RequestParam(required = false) String eEndTime,
                                                     @RequestParam(required = false) Integer type,
                                                     @RequestParam(required = false) Integer isBusiness,
                                                     @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(physicalService.listPhysical(sStartTime, sEndTime, eStartTime, eEndTime, type, isBusiness, pageReqDTO));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增体检流程")
    @LogMaker(value = "网页端-职业健康体检流程新增")
    public DataResponse<T> addPhysical(@RequestBody PhysicalReqDTO physicalReqDTO) throws Exception {
        physicalService.addPhysical(physicalReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "修改体检流程")
    @LogMaker(value = "网页端-职业健康体检流程修改")
    public DataResponse<T> modifyPhysical(@RequestBody PhysicalReqDTO physicalReqDTO) {
        physicalService.modifyPhysical(physicalReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除体检流程")
    @LogMaker(value = "网页端-职业健康体检流程删除")
    public DataResponse<T> deletePhysical(@RequestBody PhysicalReqDTO physicalReqDTO) {
        physicalService.deletePhysical(physicalReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/newUser")
    @ApiOperation(value = "新入职人员导入")
    public DataResponse<List<NewUserReqDTO>> addNewUser(@RequestParam MultipartFile file) {
        return DataResponse.of(physicalService.addNewUser(file));
    }

    @GetMapping("/newUser/bind")
    @ApiOperation(value = "入职绑定")
    public DataResponse<T> bindNewUser(@RequestParam String id) {
        physicalService.bindNewUser(id);
        return DataResponse.success();
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取体检流程详情")
    public DataResponse<PhysicalResDTO> getPhysicalDetail(@RequestParam String id) {
        return DataResponse.of(physicalService.getPhysicalDetail(id));
    }

    @GetMapping("/vx/user/confirm")
    @ApiOperation(value = "微信端-体检结果确认")
    @LogMaker(value = "微信小程序-职业健康体检结果确认")
    public DataResponse<T> vxConfirmPhysicalUser(@RequestParam String id) {
        physicalService.vxConfirmPhysicalUser(id);
        return DataResponse.success();
    }

    @GetMapping("/user/list")
    @ApiOperation(value = "体检用户列表")
    public PageResponse<PhysicalUserResDTO> listPhysicalUser(@RequestParam String id,
                                                             @RequestParam(required = false) String deptId,
                                                             @RequestParam(required = false) Integer status,
                                                             @RequestParam(required = false) Integer result,
                                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(physicalService.listPhysicalUser(id, deptId, status, result, pageReqDTO));
    }

    @GetMapping("/user/detail")
    @ApiOperation(value = "获取体检用户详情")
    public DataResponse<PhysicalUserResDTO> getPhysicalUserDetail(@RequestParam String id) {
        return DataResponse.of(physicalService.getPhysicalUserDetail(id));
    }

    @GetMapping("/user/review")
    @ApiOperation(value = "发起复检")
    @LogMaker(value = "网页端-职业健康发起复检")
    public DataResponse<T> userReview(@RequestParam String id) {
        physicalService.userReview(id);
        return DataResponse.success();
    }

    @PostMapping("/file/word")
    @ApiOperation(value = "检查结果word文件上传")
    @LogMaker(value = "网页端-职业健康体检检查结果word文件上传")
    public DataResponse<T> uploadWord(@RequestParam MultipartFile file, String bizCode, String id) throws Exception {
        physicalService.uploadWord(file, bizCode, id);
        return DataResponse.success();
    }

    @PostMapping("/file/pdf")
    @ApiOperation(value = "检查结果pdf文件上传")
    @LogMaker(value = "网页端-职业健康体检检查结果pdf文件上传")
    public DataResponse<T> uploadPdf(@RequestParam MultipartFile file, String bizCode, String id) throws Exception {
        physicalService.uploadPdf(file, bizCode, id);
        return DataResponse.success();
    }

    @GetMapping("/feedback/list")
    @ApiOperation(value = "体检反馈列表")
    public PageResponse<PhysicalFeedbackResDTO> listFeedback(@RequestParam(required = false) String name,
                                                             @RequestParam(required = false) String startTime,
                                                             @RequestParam(required = false) String endTime,
                                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(physicalService.listFeedback(name, startTime, endTime, pageReqDTO));
    }

    @GetMapping("/feedback/detail/byPhysicalId")
    @ApiOperation(value = "根据体检流程获取体检反馈详情")
    public DataResponse<PhysicalFeedbackResDTO> getFeedbackDetailByPhysicalId(@RequestParam String id,
                                                                              @RequestParam String physicalId) {
        return DataResponse.of(physicalService.getFeedbackDetailByPhysicalId(id, physicalId));
    }

    @PostMapping("/feedback/add")
    @ApiOperation(value = "新增体检反馈")
    @LogMaker(value = "网页端-职业健康体检反馈新增")
    public DataResponse<T> addFeedback(@RequestBody PhysicalFeedback physicalFeedback) {
        physicalService.addFeedback(physicalFeedback);
        return DataResponse.success();
    }

    @PostMapping("/feedback/modify")
    @ApiOperation(value = "审批体检反馈")
    @LogMaker(value = "网页端-职业健康体检反馈审批")
    public DataResponse<T> modifyFeedback(@RequestBody List<PhysicalFeedback> list) {
        physicalService.modifyFeedback(list);
        return DataResponse.success();
    }

    @GetMapping("/user/archives")
    @ApiOperation(value = "获取员工档案")
    public DataResponse<UserArchivesResDTO> userArchives(@RequestParam String id) {
        return DataResponse.of(physicalService.userArchives(id));
    }

    @GetMapping("/vx/user/archives")
    @ApiOperation(value = "微信端-获取员工档案")
    public PageResponse<PhysicalUserResDTO> vxUserArchives(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(physicalService.vxUserArchives(pageReqDTO));
    }

    @GetMapping("/vx/mine")
    @ApiOperation(value = "微信端-我的体检")
    public PageResponse<PhysicalUserResDTO> vxMinePhysical(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(physicalService.vxMinePhysical(pageReqDTO));
    }

    @GetMapping("/warn/list")
    @ApiOperation(value = "获取体检预警列表")
    public PageResponse<PhysicalWarnResDTO> listPhysicalWarn(@RequestParam(required = false) Integer type,
                                                             @RequestParam(required = false) String deptId,
                                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(physicalService.listPhysicalWarn(pageReqDTO, type, deptId));
    }

    @PostMapping("/warn/handle")
    @ApiOperation(value = "处理体检预警")
    @LogMaker(value = "网页端-职业健康处理体检预警")
    public DataResponse<T> handlePhysicalWarn(@RequestBody List<String> ids) {
        physicalService.handlePhysicalWarn(ids);
        return DataResponse.success();
    }
}
