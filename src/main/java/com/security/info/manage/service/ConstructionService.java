package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.ConstructionReqDTO;
import com.security.info.manage.dto.req.ConstructionTypeReqDTO;
import com.security.info.manage.dto.req.WeekPlanReqDTO;
import com.security.info.manage.dto.res.ConstructionResDTO;
import com.security.info.manage.dto.res.ConstructionTypeResDTO;
import com.security.info.manage.dto.res.WeekPlanResDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author frp
 */
public interface ConstructionService {

    Page<ConstructionTypeResDTO> listConstructionType(PageReqDTO pageReqDTO);

    List<ConstructionTypeResDTO> listAllConstructionType();

    void modifyConstructionType(ConstructionTypeReqDTO constructionTypeReqDTO);

    void addConstructionType(ConstructionTypeReqDTO constructionTypeReqDTO);

    void deleteConstructionType(ConstructionTypeReqDTO constructionTypeReqDTO);

    Page<WeekPlanResDTO> listWeekPlan(PageReqDTO pageReqDTO);

    void modifyWeekPlan(WeekPlanReqDTO weekPlanReqDTO);

    void addWeekPlan(WeekPlanReqDTO weekPlanReqDTO);

    void deleteWeekPlan(WeekPlanReqDTO weekPlanReqDTO);

    Page<ConstructionResDTO> listConstruction(String planId, PageReqDTO pageReqDTO);

    void addConstruction(ConstructionReqDTO constructionReqDTO);

    void importConstruction(MultipartFile file, String planId);

}
