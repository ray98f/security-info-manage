package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.req.DangerReqDTO;
import com.security.info.manage.dto.res.DangerExamineResDTO;
import com.security.info.manage.dto.res.DangerResDTO;
import com.security.info.manage.dto.res.DeptTreeResDTO;
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

    List<EntryPlate> listPlate(Integer type, String deptId);

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

    Integer issueDanger(String dangerId, String deptId, String rectifyTerm, String opinion, String createBy);
}
