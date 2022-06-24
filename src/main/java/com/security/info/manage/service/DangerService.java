package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.DangerReqDTO;
import com.security.info.manage.dto.res.*;
import com.security.info.manage.entity.EntryPlate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author frp
 */
public interface DangerService {

    List<DeptTreeResDTO> listDept(Integer type);

    List<EntryPlate> listPlate(Integer type, String deptId, String deptName);

    void addPlate(EntryPlate entryPlate);

    void modifyPlate(EntryPlate entryPlate);

    void deletePlate(EntryPlate entryPlate);

    List<EntryPlate.Entry> listEntry(String plateId);

    void addEntry(EntryPlate.Entry entry);

    void modifyEntry(EntryPlate.Entry entry);

    void deleteEntry(EntryPlate.Entry entry);

    Page<DangerResDTO> listDanger(Integer type, PageReqDTO pageReqDTO);

    Page<DangerResDTO> vxListDanger(Integer type, PageReqDTO pageReqDTO);

    Page<DangerResDTO> vxNearbyDanger(Double lng, Double lat, PageReqDTO pageReqDTO);

    DangerResDTO getDangerDetail(String id);

    void modifyDanger(DangerReqDTO dangerReqDTO);

    void addDanger(DangerReqDTO dangerReqDTO);

    void deleteDanger(DangerReqDTO dangerReqDTO);

    List<UserResDTO> examineUserList(String deptId, Integer userType);

    void examineDanger(String dangerId, String userId, String opinion, Integer status);

    void issueDanger(String dangerId, String deptId, String userId, String rectifyTerm, String opinion);

    void exportDanger(HttpServletResponse response);

    List<DangerTypeStatisticsResDTO> dangerTypeStatistics(String date);

    List<DangerDeptStatisticsResDTO> dangerDeptStatistics(String date);

    List<DangerRegionStatisticsResDTO> dangerRegionStatistics(String regionId, String date);

    List<String> listUnitStatistics(String regionId);

    List<String> listWorkAreaStatistics(String regionId);

    DangerChartStatisticsResDTO chartStatistics(String regionId, String unit, String workArea);
}
