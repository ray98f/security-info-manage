package com.security.info.manage.mapper;

import com.security.info.manage.dto.res.UserResDTO;
import com.security.info.manage.entity.Accident;
import com.security.info.manage.entity.RiskLevel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface SysMapper {

    UserResDTO selectUser(String userName);

    List<RiskLevel> listRiskLevel();

    List<Accident> listAccident();
}
