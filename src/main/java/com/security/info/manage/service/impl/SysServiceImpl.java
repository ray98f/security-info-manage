package com.security.info.manage.service.impl;

import com.security.info.manage.entity.Accident;
import com.security.info.manage.entity.RiskLevel;
import com.security.info.manage.mapper.SysMapper;
import com.security.info.manage.service.SysService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author frp
 */
@Service
@Slf4j
public class SysServiceImpl implements SysService {

    @Autowired
    private SysMapper sysMapper;

    @Override
    public List<RiskLevel> listRiskLevel() {
        return sysMapper.listRiskLevel();
    }

    @Override
    public List<Accident> listAccident() {
        return sysMapper.listAccident();
    }
}
