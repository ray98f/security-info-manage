package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.NewUserReqDTO;
import com.security.info.manage.dto.req.PhysicalReqDTO;
import com.security.info.manage.dto.req.PhysicalResultImportReqDTO;
import com.security.info.manage.dto.res.PhysicalResDTO;
import com.security.info.manage.dto.res.PhysicalUserResDTO;
import com.security.info.manage.dto.res.PostResDTO;
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

    Integer addPhysical(PhysicalReqDTO physicalReqDTO);

    Integer modifyPhysical(PhysicalReqDTO physicalReqDTO);

    Integer modifyPhysicalUser(PhysicalReqDTO physicalReqDTO);

    Integer deletePhysical(PhysicalReqDTO physicalReqDTO);

    Integer addNewUser(List<NewUserReqDTO> list);

    PhysicalResDTO getPhysicalDetail(String id);

    Page<PhysicalUserResDTO> listPhysicalUser(Page<PhysicalUserResDTO> page, String id);

    PhysicalResult getPhysicalResult(String id);

    PhysicalUserResDTO selectPhysicalUserById(String id);

    Integer userReview(PhysicalUserResDTO physicalUserResDTO);

    void editPhysical(PhysicalResultImportReqDTO physicalResultImportReqDTO);

    void physicalResultImport(PhysicalResultImportReqDTO physicalResultImportReqDTO);

    Integer uploadFilePhysical(String url, String id, String userId, Integer type);

    Integer uploadFilePhysicalUser(String url, String id, String userId, Integer type);
}
