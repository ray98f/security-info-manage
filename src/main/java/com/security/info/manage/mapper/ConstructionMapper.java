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

    Page<WeekPlanResDTO> listWeekPlan(Page<WeekPlanResDTO> page);

    Integer selectWeekPlanIsExist(WeekPlanReqDTO weekPlanReqDTO);

    Integer modifyWeekPlan(WeekPlanReqDTO weekPlanReqDTO);

    Integer addWeekPlan(WeekPlanReqDTO weekPlanReqDTO);

    Integer deleteWeekPlan(WeekPlanReqDTO weekPlanReqDTO);

    Page<ConstructionResDTO> listConstruction(Page<ConstructionResDTO> page, String planId);

    Integer addConstruction(ConstructionReqDTO constructionReqDTO);

    Integer importConstruction(List<ConstructionReqDTO> list);
}
