package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.req.DangerReqDTO;
import com.security.info.manage.dto.res.*;
import com.security.info.manage.entity.EntryPlate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface DangerMapper {

    List<DeptTreeResDTO> listDept(Integer type);

    List<EntryPlate> listPlate(Integer type, String deptId, String deptName);

    Integer selectPlateIsExist(EntryPlate entryPlate);

    Integer addPlate(EntryPlate entryPlate);

    Integer modifyPlate(EntryPlate entryPlate);

    Integer deletePlate(EntryPlate entryPlate);

    List<EntryPlate.Entry> listEntry(String plateId);

    Integer addEntry(EntryPlate.Entry entry);

    Integer modifyEntry(EntryPlate.Entry entry);

    Integer deleteEntry(EntryPlate.Entry entry);

    Integer selectUserStatus(String dangerId, String userId);

    Integer selectExamineUserStatus(String dangerId, String userId, Integer type);

    Integer selectCheckUserStatus(String dangerId, String userId);

    Integer selectCreateUserStatus(String dangerId, String userId);

    List<DangerExamineResDTO> listDangerExamine(String dangerId);

    DangerRectifyResDTO getDangerRectify(String dangerId);

    Page<DangerResDTO> listDanger(Page<DangerResDTO> page, Integer type, Integer status, String name, String userId);

    Page<DangerResDTO> vxListDanger(Page<DangerResDTO> page, Integer type, String userId);

    Page<DangerResDTO> vxNearbyDanger(Page<DangerResDTO> page, Double maxLat, Double minLat, Double minLng, Double maxLng, String userId);

    DangerResDTO getDangerDetail(String id);

    Integer selectIsDangerExamine(String id);

    Integer modifyDanger(DangerReqDTO dangerReqDTO);

    Integer addDanger(DangerReqDTO dangerReqDTO);

    Integer deleteDanger(DangerReqDTO dangerReqDTO);

    List<UserResDTO> examineUserList(List<String> ids, Integer userType);

    DangerExamineResDTO selectUserType(String dangerId);

    String selectCheckUserId(String dangerId);

    String selectRectifyUserId(String dangerId);

    Integer examineDanger(String id, String opinion, Integer status, String userId, String dangerId, Integer userType, String examineUserId);

    Integer issueDanger(String dangerId, String deptId, List<String> list, String rectifyTerm, String opinion, String createBy);

    Integer rectifyDanger(String dangerId, String userId, String rectifyMeasure, String afterPic, String createBy);

    Integer rectifyExamineDanger(String dangerId, Integer status, String createBy);

    Integer rectifyPassDanger(String dangerId, String condition, Integer status, String createBy);

    List<DangerExportResDTO> exportDanger(Integer type, Integer status, String name, String userId);

    List<DangerTypeStatisticsResDTO> dangerTypeStatistics(String date);

    List<DangerDeptStatisticsResDTO> selectAllDeptName();

    DangerDeptStatisticsResDTO.DeptStatistics dangerDeptStatistics(String deptId, String date);

    List<DangerRegionStatisticsResDTO> selectRootRegion(String regionId);

    List<String> selectBodyRegion(String regionId);

    RegionStatisticsResDTO dangerRegionStatistics(List<String> regions, String date);

    List<String> listUnitStatistics(String regionId);

    List<String> listWorkAreaStatistics(String regionId);

    List<DangerChartStatisticsResDTO.ChartStatistics> newAddStatistics(String regionId, String unit, String workArea);

    List<DangerChartStatisticsResDTO.ChartStatistics> legacyStatistics(String regionId, String unit, String workArea);

    DangerMonthStatisticsResDTO dangerMonthStatistics(Integer type, String month);
}
