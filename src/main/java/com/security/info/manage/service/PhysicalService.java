package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.NewUserReqDTO;
import com.security.info.manage.dto.req.PhysicalReqDTO;
import com.security.info.manage.dto.res.*;
import com.security.info.manage.entity.PhysicalFeedback;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author frp
 */
public interface PhysicalService {

    Page<PhysicalResDTO> listPhysical(String sStartTime, String sEndTime, String eStartTime, String eEndTime, Integer type, PageReqDTO pageReqDTO);

    void addPhysical(PhysicalReqDTO physicalReqDTO);

    void modifyPhysical(PhysicalReqDTO physicalReqDTO);

    void deletePhysical(PhysicalReqDTO physicalReqDTO);

    List<NewUserReqDTO> addNewUser(MultipartFile file);

    PhysicalResDTO getPhysicalDetail(String id);

    void vxConfirmPhysicalUser(String id);

    Page<PhysicalUserResDTO> listPhysicalUser(String id, PageReqDTO pageReqDTO);

    PhysicalUserResDTO getPhysicalUserDetail(String id);

    void userReview(String id);

    void uploadWord(MultipartFile file, String bizCode, String id) throws Exception;

    void uploadPdf(MultipartFile file, String bizCode, String id) throws Exception;

    Page<PhysicalFeedbackResDTO> listFeedback(String name, PageReqDTO pageReqDTO);

    PhysicalFeedbackResDTO getFeedbackDetailByPhysicalId(String id, String physicalId);

    void addFeedback(PhysicalFeedback physicalFeedback);

    void modifyFeedback(PhysicalFeedback physicalFeedback);

    UserArchivesResDTO userArchives(String id);

    Page<PhysicalUserResDTO> vxUserArchives(PageReqDTO pageReqDTO);

    Page<PhysicalUserResDTO> vxMinePhysical(PageReqDTO pageReqDTO);

    Page<PhysicalWarnResDTO> listPhysicalWarn(PageReqDTO pageReqDTO);

    void handlePhysicalWarn(String id);
}
