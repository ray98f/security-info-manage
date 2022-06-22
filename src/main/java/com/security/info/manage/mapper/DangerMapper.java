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

    List<DangerExamineResDTO> listDangerExamine(String dangerId);

    Page<DangerResDTO> listDanger(Page<DangerResDTO> page, Integer type, String userId);

    Page<DangerResDTO> vxListDanger(Page<DangerResDTO> page, Integer type, String userId);

    Page<DangerResDTO> vxNearbyDanger(Page<DangerResDTO> page, Double maxLat, Double minLat, Double minLng, Double maxLng, String userId);

    DangerResDTO getDangerDetail(String id);

    Integer selectIsDangerExamine(String id);

    Integer modifyDanger(DangerReqDTO dangerReqDTO);

    Integer addDanger(DangerReqDTO dangerReqDTO);

    Integer deleteDanger(DangerReqDTO dangerReqDTO);

    DangerExamineResDTO selectUserType(String dangerId);

    String selectCheckUserId(String dangerId);

    Integer examineDanger(String id, String opinion, Integer status, String userId, String dangerId, Integer userType, String examineUserId);

    Integer issueDanger(String dangerId, String deptId, String userId, String rectifyTerm, String opinion, String createBy);

    List<DangerTypeStatisticsResDTO> dangerTypeStatistics(String date);

    List<DangerDeptStatisticsResDTO> selectAllDeptName();

    DangerDeptStatisticsResDTO.DeptStatistics dangerDeptStatistics(String deptId, String date);

    List<DangerRegionStatisticsResDTO> selectRootRegion(String regionId);

    List<String> selectBodyRegion(String regionId);

    DangerRegionStatisticsResDTO.RegionStatistics dangerRegionStatistics(List<String> regions, String date);

    List<String> listUnitStatistics(String regionId);

    List<String> listWorkAreaStatistics(String regionId);

    List<DangerChartStatisticsResDTO.ChartStatistics> newAddStatistics(String regionId, String unit, String workArea);

    List<DangerChartStatisticsResDTO.ChartStatistics> legacyStatistics(String regionId, String unit, String workArea);
}
