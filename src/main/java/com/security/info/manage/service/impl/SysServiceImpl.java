package com.security.info.manage.service.impl;

import com.security.info.manage.dto.req.LoginReqDTO;
import com.security.info.manage.dto.res.UserResDTO;
import com.security.info.manage.entity.Accident;
import com.security.info.manage.entity.RiskLevel;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.SysMapper;
import com.security.info.manage.service.SysService;
import com.security.info.manage.utils.AesUtils;
import com.security.info.manage.utils.MyAESUtil;
import com.security.info.manage.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class SysServiceImpl implements SysService {

    @Autowired
    private SysMapper sysMapper;

    @Override
    public Map<String, Object> login(LoginReqDTO loginReqDTO) throws Exception {
        if (Objects.isNull(loginReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        UserResDTO resDTO = sysMapper.selectUser(loginReqDTO.getUserName());
        if (Objects.isNull(resDTO) || Objects.isNull(resDTO.getId()) || !MyAESUtil.decrypt(loginReqDTO.getPassword()).equals(resDTO.getPassword())) {
            throw new CommonException(ErrorCode.LOGIN_PASSWORD_ERROR);
        }
        Map<String, Object> data = new HashMap<>(16);
        data.put("userInfo", resDTO);
        data.put("token", TokenUtil.createSimpleToken(resDTO));
        return data;
    }

    @Override
    public List<RiskLevel> listRiskLevel() {
        return sysMapper.listRiskLevel();
    }

    @Override
    public List<Accident> listAccident() {
        return sysMapper.listAccident();
    }
}
