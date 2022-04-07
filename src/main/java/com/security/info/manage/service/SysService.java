package com.security.info.manage.service;

import com.security.info.manage.entity.Accident;
import com.security.info.manage.entity.RiskLevel;

import java.util.List;

/**
 * @author frp
 */
public interface SysService {

    List<RiskLevel> listRiskLevel();

    List<Accident> listAccident();

}
