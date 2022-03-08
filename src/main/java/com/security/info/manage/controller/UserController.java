package com.security.info.manage.controller;

import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.PageResponse;
import com.security.info.manage.dto.req.PasswordReqDTO;
import com.security.info.manage.dto.req.UserReqDTO;
import com.security.info.manage.entity.User;
import com.security.info.manage.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "用户")
@Validated
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 同步员工
     *
     * @return
     */
    @PostMapping("/sync")
    @ApiOperation(value = "同步员工")
    public DataResponse<T> syncUser() {
        userService.syncUser();
        return DataResponse.success();
    }

    /**
     * 修改密码
     *
     * @param passwordReqDTO 密码信息
     * @return DataResponse
     */
    @PostMapping("/change")
    @ApiOperation(value = "修改密码")
    public DataResponse<T> changePwd(@RequestBody @Valid PasswordReqDTO passwordReqDTO) {
        userService.changePwd(passwordReqDTO);
        return DataResponse.success();
    }

    /**
     * 修改用户信息
     *
     * @param userReqDTO 用户修改信息
     * @return <T>
     */
    @PostMapping("/modify")
    @ApiOperation(value = "修改用户信息")
    public DataResponse<T> edit(@RequestBody @Valid UserReqDTO userReqDTO) {
        userService.editUser(userReqDTO);
        return DataResponse.success();
    }

    /**
     * 获取所有用户
     */
    @GetMapping("/all")
    @ApiOperation(value = "获取所有用户")
    public DataResponse<List<User>> listAllUser(){
        return DataResponse.of(userService.listAllUser());
    }

    /**
     * 分页查询用户列表
     * @param status
     * @param userRealName
     * @param pageReqDTO
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "分页查询用户列表")
    public PageResponse<User> listUser(@RequestParam(required = false) @ApiParam("状态") Integer status,
                                       @RequestParam(required = false) @ApiParam("姓名") String userRealName,
                                       @Valid PageReqDTO pageReqDTO){
        return PageResponse.of(userService.listUser(status, userRealName, pageReqDTO));
    }
}