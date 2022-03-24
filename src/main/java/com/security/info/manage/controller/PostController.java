package com.security.info.manage.controller;

import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.PostReqDTO;
import com.security.info.manage.dto.req.PostUserReqDTO;
import com.security.info.manage.dto.res.ApplianceWarnResDTO;
import com.security.info.manage.dto.res.DeptTreeResDTO;
import com.security.info.manage.dto.res.PostResDTO;
import com.security.info.manage.dto.res.PostWarnResDTO;
import com.security.info.manage.service.PostService;
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
@RequestMapping("/post")
@Api(tags = "岗位管理")
@Validated
public class PostController {

    @Resource
    private PostService postService;

    @GetMapping("/list")
    @ApiOperation(value = "获取岗位列表")
    public PageResponse<PostResDTO> listPost(@RequestParam(required = false) String name,
                                             @RequestParam(required = false) Integer status,
                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(postService.listPost(name, status, pageReqDTO));
    }

    @GetMapping("/listAll")
    @ApiOperation(value = "获取所有岗位列表")
    public DataResponse<List<PostResDTO>> listAllPost() {
        return DataResponse.of(postService.listAllPost());
    }

    @PostMapping("/modify")
    @ApiOperation(value = "岗位修改")
    public DataResponse<T> modifyPost(@RequestBody PostReqDTO postReqDTO) {
        postService.modifyPost(postReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/add")
    @ApiOperation(value = "岗位新增")
    public DataResponse<T> addPost(@RequestBody PostReqDTO postReqDTO) {
        postService.addPost(postReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "岗位删除")
    public DataResponse<T> deletePost(@RequestBody PostReqDTO postReqDTO) {
        postService.deletePost(postReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/binding")
    @ApiOperation(value = "岗位人员绑定")
    public DataResponse<T> bindingPost(@RequestBody PostUserReqDTO postUserReqDTO) {
        postService.bindingPost(postUserReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/warn/list")
    @ApiOperation(value = "获取岗位异动预警列表")
    public PageResponse<PostWarnResDTO> listPostWarn(@Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(postService.listPostWarn(pageReqDTO));
    }

    @PostMapping("/warn/handle")
    @ApiOperation(value = "处理岗位异动预警")
    public DataResponse<T> handlePostWarn(@RequestBody PostWarnResDTO postWarnResDTO) {
        postService.handlePostWarn(postWarnResDTO.getId());
        return DataResponse.success();
    }

}
