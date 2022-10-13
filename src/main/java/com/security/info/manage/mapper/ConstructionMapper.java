package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.req.ConstructionReqDTO;
import com.security.info.manage.dto.req.ConstructionTypeReqDTO;
import com.security.info.manage.dto.req.WeekPlanReqDTO;
import com.security.info.manage.dto.res.ConstructionResDTO;
import com.security.info.manage.dto.res.PostResDTO;
import com.security.info.manage.dto.res.ConstructionTypeResDTO;
import com.security.info.manage.dto.res.WeekPlanResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface ConstructionMapper {

    Page<ConstructionTypeResDTO> listConstructionType(Page<PostResDTO> page);

    List<ConstructionTypeResDTO> listAllConstructionType();

    Integer selectConstructionTypeIsExist(ConstructionTypeReqDTO constructionTypeReqDTO);

    Integer modifyConstructionType(ConstructionTypeReqDTO constructionTypeReqDTO);

    Integer addConstructionType(ConstructionTypeReqDTO constructionTypeReqDTO);

    Integer deleteConstructionType(ConstructionTypeReqDTO constructionTypeReqDTO);

    Page<WeekPlanResDTO> listWeekPlan(Page<WeekPlanResDTO> page, String startTime, String endTime, String name);

    Integer selectWeekPlanIsExist(WeekPlanReqDTO weekPlanReqDTO);

    Integer modifyWeekPlan(WeekPlanReqDTO weekPlanReqDTO);

    Integer addWeekPlan(WeekPlanReqDTO weekPlanReqDTO);

    Integer deleteWeekPlan(WeekPlanReqDTO weekPlanReqDTO);

    Page<ConstructionResDTO> listConstruction(Page<ConstructionResDTO> page, String planId, String startTime, String endTime, String name, String planName, String orgId);

    ConstructionResDTO getConstructionDetail(String id);

    Page<ConstructionResDTO> vxListConstruction(Page<ConstructionResDTO> page, String userId);

    String selectUserId(String userName);

    Integer addConstruction(ConstructionReqDTO constructionReqDTO);

    Integer modifyConstruction(ConstructionReqDTO constructionReqDTO);

    Integer deleteConstruction(List<String> ids, String userId);

    Integer importConstruction(List<ConstructionReqDTO> list, String planId);
}
