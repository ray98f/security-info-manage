package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.ConstructionReqDTO;
import com.security.info.manage.dto.req.ConstructionTypeReqDTO;
import com.security.info.manage.dto.req.WeekPlanReqDTO;
import com.security.info.manage.dto.res.ConstructionResDTO;
import com.security.info.manage.dto.res.ConstructionTypeResDTO;
import com.security.info.manage.dto.res.WeekPlanResDTO;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;
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

    Page<WeekPlanResDTO> listWeekPlan(PageReqDTO pageReqDTO, String startTime, String endTime, String name);

    void modifyWeekPlan(WeekPlanReqDTO weekPlanReqDTO);

    void addWeekPlan(WeekPlanReqDTO weekPlanReqDTO);

    void deleteWeekPlan(WeekPlanReqDTO weekPlanReqDTO);

    Page<ConstructionResDTO> listConstruction(String planId, String startTime, String endTime, String name, String planName, String orgId, PageReqDTO pageReqDTO);

    ConstructionResDTO getConstructionDetail(String id);

    Page<ConstructionResDTO> vxListConstruction(PageReqDTO pageReqDTO);

    void addConstruction(ConstructionReqDTO constructionReqDTO);

    void modifyConstruction(ConstructionReqDTO constructionReqDTO);

    void deleteConstruction(List<String> ids);

    void importConstruction(MultipartFile file, String planId);

}
