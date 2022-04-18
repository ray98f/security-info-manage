package com.security.info.manage.service;

import com.security.info.manage.dto.req.LoginReqDTO;
import com.security.info.manage.entity.Accident;
import com.security.info.manage.entity.RiskLevel;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author frp
 */
public interface SysService {

    Map<String, Object> login(LoginReqDTO loginReqDTO) throws Exception;

    List<RiskLevel> listRiskLevel();

    List<Accident> listAccident();

}
