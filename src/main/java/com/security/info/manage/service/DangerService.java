package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.DangerReqDTO;
import com.security.info.manage.dto.res.DangerResDTO;
import com.security.info.manage.dto.res.DeptTreeResDTO;
import com.security.info.manage.dto.res.VxDeptResDTO;
import com.security.info.manage.entity.EntryPlate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author frp
 */
public interface DangerService {

    List<DeptTreeResDTO> listDept(Integer type);

    List<EntryPlate> listPlate(Integer type, String deptId);

    List<EntryPlate.Entry> listEntry(String plateId);

    Page<DangerResDTO> listDanger(Integer type, PageReqDTO pageReqDTO);

    Page<DangerResDTO> vxListDanger(Integer type, PageReqDTO pageReqDTO);

    Page<DangerResDTO> vxNearbyDanger(Double lng, Double lat, PageReqDTO pageReqDTO);

    DangerResDTO getDangerDetail(String id);

    void modifyDanger(DangerReqDTO dangerReqDTO);

    void addDanger(DangerReqDTO dangerReqDTO);

    void deleteDanger(DangerReqDTO dangerReqDTO);

    void examineDanger(String dangerId, String deptId, String opinion, Integer status);

    void issueDanger(String dangerId, String deptId, String rectifyTerm, String opinion);
}
