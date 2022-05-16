package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.NewUserReqDTO;
import com.security.info.manage.dto.req.PhysicalNoticeReqDTO;
import com.security.info.manage.dto.req.PhysicalReqDTO;
import com.security.info.manage.dto.req.PhysicalResultImportReqDTO;
import com.security.info.manage.dto.res.*;
import com.security.info.manage.entity.PhysicalFeedback;
import com.security.info.manage.entity.PhysicalResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface PhysicalMapper {

    Page<PhysicalResDTO> listPhysical(Page<PostResDTO> page, String sStartTime, String sEndTime, String eStartTime, String eEndTime, Integer type);

    PhysicalUserCountResDTO countPhysicalUser(String id);

    Integer addPhysical(PhysicalReqDTO physicalReqDTO);

    Integer modifyPhysical(PhysicalReqDTO physicalReqDTO);

    Integer modifyPhysicalUser(PhysicalReqDTO physicalReqDTO);

    Integer deletePhysical(PhysicalReqDTO physicalReqDTO);

    Integer addNewUser(List<NewUserReqDTO> list);

    PhysicalResDTO getPhysicalDetail(String id);

    Integer vxConfirmPhysicalUser(String id, String userId);

    Page<PhysicalUserResDTO> listPhysicalUser(Page<PhysicalUserResDTO> page, String id);

    PhysicalResult getPhysicalResult(String id);

    PhysicalUserResDTO selectPhysicalUserById(String id);

    PhysicalUserResDTO selectLatestPhysicalUserByUserId(String id);

    List<PhysicalUserResDTO> selectPhysicalUserByUserId(String id);

    Page<PhysicalUserResDTO> selectVxPhysicalUserByUserId(Page<PhysicalUserResDTO> page, String id);

    Page<PhysicalUserResDTO> selectVxMinePhysical(Page<PhysicalUserResDTO> page, String id);

    Integer ifPhysicalHadFeedback(String id, String physicalId);

    List<PhysicalUserResDTO> selectPhysicalUserByPhysicalId(String id);

    Integer userReview(PhysicalUserResDTO physicalUserResDTO);

    void editPhysical(PhysicalResultImportReqDTO physicalResultImportReqDTO);

    String selectPhysicalIdByPhysicalUserId(String id);

    void physicalResultImport(PhysicalResultImportReqDTO physicalResultImportReqDTO);

    Integer uploadFilePhysical(String url, String id, String userId, Integer type);

    Integer uploadFilePhysicalUser(String url, String id, String userId, Integer type);

    Integer insertPhysicalNotice(PhysicalNoticeReqDTO physicalNoticeReqDTO);

    Page<PhysicalFeedbackResDTO> listFeedback(Page<PhysicalFeedback> page, String name);

    PhysicalFeedbackResDTO getFeedbackDetailByPhysicalId(String id, String physicalId);

    Integer insertPhysicalFeedback(PhysicalFeedback physicalFeedback);

    Integer modifyPhysicalFeedback(PhysicalFeedback physicalFeedback);

    List<PhysicalUserResDTO> listExpiredPhysicalUser();

    void addPhysicalWarning(List<PhysicalUserResDTO> list);

    Page<PhysicalWarnResDTO> listPhysicalWarn(Page<PhysicalWarnResDTO> page, Integer type);

    Integer handlePhysicalWarn(String id);
}
