package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.NewUserReqDTO;
import com.security.info.manage.dto.req.PhysicalReqDTO;
import com.security.info.manage.dto.res.*;
import com.security.info.manage.entity.PhysicalFeedback;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author frp
 */
public interface PhysicalService {

    void exportPhysical(String sStartTime, String sEndTime, String eStartTime, String eEndTime, Integer type, HttpServletResponse response);

    Page<PhysicalResDTO> listPhysical(String sStartTime, String sEndTime, String eStartTime, String eEndTime, Integer type, Integer isBusiness, PageReqDTO pageReqDTO);

    void addPhysical(PhysicalReqDTO physicalReqDTO) throws Exception;

    void modifyPhysical(PhysicalReqDTO physicalReqDTO);

    void deletePhysical(PhysicalReqDTO physicalReqDTO);

    List<NewUserReqDTO> addNewUser(MultipartFile file);

    void bindNewUser(String id);

    PhysicalResDTO getPhysicalDetail(String id);

    void vxConfirmPhysicalUser(String id);

    Page<PhysicalUserResDTO> listPhysicalUser(String id, String deptId, Integer status, Integer result, PageReqDTO pageReqDTO);

    PhysicalUserResDTO getPhysicalUserDetail(String id);

    void userReview(String id);

    void uploadWord(MultipartFile file, String bizCode, String id) throws Exception;

    void uploadPdf(MultipartFile file, String bizCode, String id) throws Exception;

    Page<PhysicalFeedbackResDTO> listFeedback(String name, String startTime, String endTime, PageReqDTO pageReqDTO);

    PhysicalFeedbackResDTO getFeedbackDetailByPhysicalId(String id, String physicalId);

    void addFeedback(PhysicalFeedback physicalFeedback);

    void modifyFeedback(List<PhysicalFeedback> list);

    UserArchivesResDTO userArchives(String id);

    Page<PhysicalUserResDTO> vxUserArchives(PageReqDTO pageReqDTO);

    Page<PhysicalUserResDTO> vxMinePhysical(PageReqDTO pageReqDTO);

    Page<PhysicalWarnResDTO> listPhysicalWarn(PageReqDTO pageReqDTO, Integer type, String deptId);

    void handlePhysicalWarn(List<String> ids);
}
