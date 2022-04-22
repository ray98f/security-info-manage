package com.security.info.manage.controller;

import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.DicDataReqDTO;
import com.security.info.manage.dto.req.DicReqDTO;
import com.security.info.manage.dto.res.DicDataResDTO;
import com.security.info.manage.dto.res.DicResDTO;
import com.security.info.manage.service.DicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/dic")
@Api(tags = "字典管理")
@Validated
public class DicController {

    @Resource
    private DicService dicService;

    @GetMapping("/type/list")
    @ApiOperation(value = "【字典管理】- 分页查询字典类型列表")
    public PageResponse<DicResDTO> getDics(@RequestParam(required = false) @ApiParam(value = "名称模糊查询字段") String name,
                                           @RequestParam(required = false) @ApiParam(value = "字典类型") String type,
                                           @RequestParam(required = false) @ApiParam(value = "字典状态：0-禁用，1-启用") Integer isEnable,
                                           @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(dicService.getDics(pageReqDTO, name, type, isEnable));
    }

    @PostMapping("/type/add")
    @ApiOperation(value = "【字典管理】- 新增字典类型")
    public DataResponse<T> addDic(@RequestBody DicReqDTO reqDTO) {
        dicService.addDic(reqDTO);
        return DataResponse.success();
    }

    @PostMapping("/type/modify")
    @ApiOperation(value = "【字典管理】- 修改字典类型")
    public DataResponse<T> updateDic(@RequestBody DicReqDTO reqDTO) {
        dicService.updateDic(reqDTO);
        return DataResponse.success();
    }

    @PostMapping("/type/delete")
    @ApiOperation(value = "【字典管理】- 删除字典类型")
    public DataResponse<T> deleteDic(@RequestBody DicReqDTO reqDTO) {
        dicService.deleteDic(reqDTO);
        return DataResponse.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "【字典数据】- 分页查询")
    public PageResponse<DicDataResDTO> getDicDataByPage(@Valid PageReqDTO pageReqDTO,
                                                        @RequestParam(required = false) @ApiParam(value = "字典名称，下拉框显示名称，实际传type") String type,
                                                        @RequestParam(required = false) @ApiParam(value = "字典标签") String value,
                                                        @RequestParam(required = false) @ApiParam(value = "状态") Integer isEnable) {
        return PageResponse.of(dicService.getDicDataByPage(pageReqDTO, type, value, isEnable));
    }

    @PostMapping("/add")
    @ApiOperation(value = "【字典数据】- 新增字典数据")
    public DataResponse<T> addDicData(@RequestBody DicDataReqDTO reqDTO) {
        dicService.addDicData(reqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "【字典数据】- 更新字典数据")
    public DataResponse<T> updateDicData(@RequestBody DicDataReqDTO reqDTO) {
        dicService.updateDicData(reqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "【字典数据】- 删除字典数据")
    public DataResponse<T> deleteDicData(@RequestBody DicDataReqDTO reqDTO) {
        dicService.deleteDicData(reqDTO);
        return DataResponse.success();
    }
}
