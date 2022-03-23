package com.security.info.manage.controller;

import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.NewUserReqDTO;
import com.security.info.manage.dto.req.PostReqDTO;
import com.security.info.manage.dto.req.PostUserReqDTO;
import com.security.info.manage.dto.req.TrainReqDTO;
import com.security.info.manage.dto.res.PostResDTO;
import com.security.info.manage.dto.res.TrainDetailResDTO;
import com.security.info.manage.dto.res.TrainResDTO;
import com.security.info.manage.service.PostService;
import com.security.info.manage.service.TrainService;
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
@RequestMapping("/train")
@Api(tags = "健康培训管理")
@Validated
public class TrainController {

    @Resource
    private TrainService trainService;

    @GetMapping("/list")
    @ApiOperation(value = "获取健康培训列表")
    public PageResponse<TrainResDTO> listTrain(@RequestParam(required = false) String startTime,
                                               @RequestParam(required = false) String endTime,
                                               @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(trainService.listTrain(startTime, endTime, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取健康培训详情")
    public DataResponse<TrainResDTO> getTrainDetail(@RequestParam String id) {
        return DataResponse.of(trainService.getTrainDetail(id));
    }

    @GetMapping("/user/list")
    @ApiOperation(value = "获取健康培训人员列表")
    public PageResponse<TrainDetailResDTO> listTrainUserDetail(@RequestParam String id,
                                                               @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(trainService.listTrainUserDetail(id, pageReqDTO));
    }

    @PostMapping("/modify")
    @ApiOperation(value = "健康培训修改")
    public DataResponse<T> modifyTrain(@RequestBody TrainReqDTO trainReqDTO) {
        trainService.modifyTrain(trainReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/add")
    @ApiOperation(value = "健康培训新增")
    public DataResponse<T> addTrain(@RequestBody TrainReqDTO trainReqDTO) {
        trainService.addTrain(trainReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "健康培训删除")
    public DataResponse<T> deleteTrain(@RequestBody TrainReqDTO trainReqDTO) {
        trainService.deleteTrain(trainReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/import")
    @ApiOperation(value = "健康培训结果导入")
    public DataResponse<T> importTrainDetail(@RequestParam MultipartFile file, String trainId) {
        trainService.importTrainDetail(file, trainId);
        return DataResponse.success();
    }

}
