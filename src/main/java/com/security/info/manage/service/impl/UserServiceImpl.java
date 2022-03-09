package com.security.info.manage.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.VxAccessToken;
import com.security.info.manage.dto.req.LoginReqDTO;
import com.security.info.manage.dto.req.PasswordReqDTO;
import com.security.info.manage.dto.req.UserReqDTO;
import com.security.info.manage.dto.res.VxDeptResDTO;
import com.security.info.manage.dto.res.VxUserResDTO;
import com.security.info.manage.entity.User;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.PostMapper;
import com.security.info.manage.mapper.UserMapper;
import com.security.info.manage.service.UserService;
import com.security.info.manage.utils.AesUtils;
import com.security.info.manage.utils.Constants;
import com.security.info.manage.utils.TokenUtil;
import com.security.info.manage.utils.VxApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostMapper postMapper;

    @Value("${vx-business.corpid}")
    private String corpid;

    @Value("${vx-business.corpsecret}")
    private String corpsecret;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 同步员工
     *
     * @return
     */
    @Override
    public void syncUser() {
        VxAccessToken accessToken = VxApiUtils.getAccessToken(corpid, corpsecret);
        if (accessToken == null) {
            throw new CommonException(ErrorCode.VX_ERROR, "accessToken返回为空!");
        }
        String url = Constants.VX_GET_ORG_IDS + "?access_token=" + accessToken.getToken();
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url)
                .build()
                .expand()
                .encode();
        URI uri = uriComponents.toUri();
        JSONObject res = restTemplate.getForEntity(uri, JSONObject.class).getBody();
        if (!Constants.SUCCESS.equals(Objects.requireNonNull(res).getString(Constants.ERR_CODE))) {
            throw new CommonException(ErrorCode.VX_ERROR, String.valueOf(res.get(Constants.ERR_MSG)));
        }
        if (res.getJSONArray("department_id") == null) {
            return;
        }
        List<VxDeptResDTO> list = JSONArray.parseArray(res.getJSONArray("department_id").toJSONString(), VxDeptResDTO.class);
        if (list != null && !list.isEmpty()) {
            for (VxDeptResDTO vxDeptResDTO : list) {
                url = Constants.VX_GET_USER_LIST + "?access_token=" + accessToken.getToken()
                        + "&department_id=" + vxDeptResDTO.getId() + "&fetch_child=0";
                UriComponents userUriComponents = UriComponentsBuilder.fromUriString(url)
                        .build()
                        .expand()
                        .encode();
                URI userUri = userUriComponents.toUri();
                JSONObject resUser = restTemplate.getForEntity(userUri, JSONObject.class).getBody();
                if (!Constants.SUCCESS.equals(Objects.requireNonNull(resUser).getString(Constants.ERR_CODE))) {
                    throw new CommonException(ErrorCode.VX_ERROR, String.valueOf(resUser.get(Constants.ERR_MSG)));
                }
                if (resUser.getJSONArray("userlist") == null) {
                    continue;
                }
                List<VxUserResDTO> userList = JSONArray.parseArray(res.getJSONArray("userlist").toJSONString(), VxUserResDTO.class);
                if (userList != null && !userList.isEmpty()) {
                    userMapper.insertUser(userList, TokenUtil.getCurrentPersonNo());
//                    postMapper.insertPost(userList, TokenUtil.getCurrentPersonNo());
//                    postMapper.insertUserPost(userList);
                }
            }
        }
    }

    @Override
    public UserReqDTO selectUserInfo(LoginReqDTO loginReqDTO) {
        if (Objects.isNull(loginReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        UserReqDTO userInfo = userMapper.selectUserInfo(null, loginReqDTO.getUserName());
        if (Objects.isNull(userInfo)) {
            throw new CommonException(ErrorCode.LOGIN_PASSWORD_ERROR);
        }
        if (userInfo.getStatus() == 1) {
            throw new CommonException(ErrorCode.USER_DISABLE);
        }
        if (!loginReqDTO.getPassword().equals(AesUtils.decrypt(userInfo.getPassword()))) {
            throw new CommonException(ErrorCode.LOGIN_PASSWORD_ERROR);
        }
        userInfo.setRoleIds(userMapper.selectUserRoles(userInfo.getId()));
        return userInfo;
    }

    @Override
    public void changePwd(PasswordReqDTO passwordReqDTO) {
        if (Objects.isNull(passwordReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        passwordReqDTO.setOldPwd(AesUtils.encrypt(passwordReqDTO.getOldPwd()));
        passwordReqDTO.setNewPwd(AesUtils.encrypt(passwordReqDTO.getNewPwd()));
        int result = userMapper.changePwd(passwordReqDTO, TokenUtil.getCurrentUserName());
        if (result < 0) {
            throw new CommonException(ErrorCode.USER_PWD_CHANGE_FAIL);
        }
    }

    @Override
    public void editUser(UserReqDTO userReqDTO) {
        if (Objects.isNull(userReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        UserReqDTO userInfo = userMapper.selectUserInfo(userReqDTO.getId(), userReqDTO.getUserName());
        if (!Objects.isNull(userInfo)) {
            throw new CommonException(ErrorCode.USER_EXIST);
        }
        int result = userMapper.editUser(userReqDTO, TokenUtil.getCurrentUserName());
        if (result > 0) {
            userMapper.deleteUserRole(userReqDTO.getId());
            result = userMapper.insertUserRole(userReqDTO.getId(), userReqDTO.getRoleIds(), TokenUtil.getCurrentUserName());
            if (result < 0) {
                throw new CommonException(ErrorCode.UPDATE_ERROR);
            }
        } else {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public List<User> listAllUser() {
        List<User> list = userMapper.listAllUser();
        if (null == list || list.isEmpty()) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        return list;
    }

    @Override
    public Page<User> listUser(Integer status, String userRealName, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return userMapper.listUser(pageReqDTO.of(), status, userRealName);
    }
}
