package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.DangerReqDTO;
import com.security.info.manage.dto.req.VxSendTextMsgReqDTO;
import com.security.info.manage.dto.res.DangerExamineResDTO;
import com.security.info.manage.dto.res.DangerResDTO;
import com.security.info.manage.dto.res.DeptTreeResDTO;
import com.security.info.manage.dto.res.UserResDTO;
import com.security.info.manage.entity.EntryPlate;
import com.security.info.manage.entity.User;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.FileMapper;
import com.security.info.manage.mapper.DangerMapper;
import com.security.info.manage.mapper.SysMapper;
import com.security.info.manage.service.DangerService;
import com.security.info.manage.service.DeptService;
import com.security.info.manage.service.MsgService;
import com.security.info.manage.utils.ObjectUtils;
import com.security.info.manage.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author frp
 */
@Service
@Slf4j
public class DangerServiceImpl implements DangerService {

    @Autowired
    private DangerMapper dangerMapper;

    @Autowired
    private SysMapper sysMapper;

    @Resource
    private DeptService deptService;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private MsgService msgService;

    @Override
    public List<DeptTreeResDTO> listDept(Integer type) {
        return dangerMapper.listDept(type);
    }

    @Override
    public List<EntryPlate> listPlate(Integer type, String deptId) {
        return dangerMapper.listPlate(type, deptId);
    }

    @Override
    public void addPlate(EntryPlate entryPlate) {
        if (Objects.isNull(entryPlate)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = dangerMapper.selectPlateIsExist(entryPlate);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        entryPlate.setId(TokenUtil.getUuId());
        result = dangerMapper.addPlate(entryPlate);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void modifyPlate(EntryPlate entryPlate) {
        if (Objects.isNull(entryPlate)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = dangerMapper.selectPlateIsExist(entryPlate);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        result = dangerMapper.modifyPlate(entryPlate);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void deletePlate(EntryPlate entryPlate) {
        if (Objects.isNull(entryPlate)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = dangerMapper.deletePlate(entryPlate);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public List<EntryPlate.Entry> listEntry(String plateId) {
        return dangerMapper.listEntry(plateId);
    }

    @Override
    public void addEntry(EntryPlate.Entry entry) {
        if (Objects.isNull(entry)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        entry.setId(TokenUtil.getUuId());
        Integer result = dangerMapper.addEntry(entry);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void modifyEntry(EntryPlate.Entry entry) {
        if (Objects.isNull(entry)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = dangerMapper.modifyEntry(entry);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void deleteEntry(EntryPlate.Entry entry) {
        if (Objects.isNull(entry)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = dangerMapper.deleteEntry(entry);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public Page<DangerResDTO> listDanger(Integer type, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<DangerResDTO> page;
        if (sysMapper.selectIfAdmin(TokenUtil.getCurrentPersonNo()) == 1) {
            page = dangerMapper.listDanger(pageReqDTO.of(), type, null);
        } else {
            page = dangerMapper.listDanger(pageReqDTO.of(), type, TokenUtil.getCurrentPersonNo());
        }
        List<DangerResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
            for (DangerResDTO resDTO : list) {
                if (resDTO.getBeforePic() != null && !"".equals(resDTO.getBeforePic())) {
                    resDTO.setBeforePicFile(fileMapper.selectFileInfo(Arrays.asList(resDTO.getBeforePic().split(","))));
                }
                resDTO.setDangerExamines(dangerMapper.listDangerExamine(resDTO.getId()));
                resDTO.setUserStatus(dangerMapper.selectUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public Page<DangerResDTO> vxListDanger(Integer type, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<DangerResDTO> page = dangerMapper.vxListDanger(pageReqDTO.of(), type, TokenUtil.getCurrentPersonNo());
        List<DangerResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
            for (DangerResDTO resDTO : list) {
                if (resDTO.getBeforePic() != null && !"".equals(resDTO.getBeforePic())) {
                    resDTO.setBeforePicFile(fileMapper.selectFileInfo(Arrays.asList(resDTO.getBeforePic().split(","))));
                }
                resDTO.setDangerExamines(dangerMapper.listDangerExamine(resDTO.getId()));
                resDTO.setUserStatus(dangerMapper.selectUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public Page<DangerResDTO> vxNearbyDanger(Double lng, Double lat, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Map<String, Double> data = ObjectUtils.findNeighDrugstore(lng, lat, 3.0);
        Page<DangerResDTO> page = dangerMapper.vxNearbyDanger(pageReqDTO.of(), data.get("maxLat"), data.get("minLat"), data.get("minLng"), data.get("maxLng"), TokenUtil.getCurrentPersonNo());
        List<DangerResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
            for (DangerResDTO resDTO : list) {
                if (resDTO.getBeforePic() != null && !"".equals(resDTO.getBeforePic())) {
                    resDTO.setBeforePicFile(fileMapper.selectFileInfo(Arrays.asList(resDTO.getBeforePic().split(","))));
                }
                resDTO.setDangerExamines(dangerMapper.listDangerExamine(resDTO.getId()));
                resDTO.setUserStatus(dangerMapper.selectUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public DangerResDTO getDangerDetail(String id) {
        DangerResDTO res = dangerMapper.getDangerDetail(id);
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (res.getBeforePic() != null && !"".equals(res.getBeforePic())) {
            res.setBeforePicFile(fileMapper.selectFileInfo(Arrays.asList(res.getBeforePic().split(","))));
        }
        res.setDangerExamines(dangerMapper.listDangerExamine(id));
        res.setUserStatus(dangerMapper.selectUserStatus(id, TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
        return res;
    }

    @Override
    public void modifyDanger(DangerReqDTO dangerReqDTO) {
        if (Objects.isNull(dangerReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = dangerMapper.selectIsDangerExamine(dangerReqDTO.getId());
        if (result != 0 && result != 4) {
            throw new CommonException(ErrorCode.RESOURCE_USE);
        }
        dangerReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = dangerMapper.modifyDanger(dangerReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        } else if (dangerReqDTO.getIsUse() == 1 && dangerReqDTO.getExamineUserId() != null) {
            VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
            vxSendTextMsgReqDTO.setTouser(dangerReqDTO.getExamineUserId());
            vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的隐患排查审批通知，请前往小程序查看处理。"));
            msgService.sendTextMsg(vxSendTextMsgReqDTO);
        }
    }

    @Override
    public void addDanger(DangerReqDTO dangerReqDTO) {
        if (Objects.isNull(dangerReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        dangerReqDTO.setId(TokenUtil.getUuId());
        dangerReqDTO.setNo(TokenUtil.getUuId());
        dangerReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = dangerMapper.addDanger(dangerReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        } else if (dangerReqDTO.getIsUse() == 1 && dangerReqDTO.getExamineUserId() != null) {
            VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
            vxSendTextMsgReqDTO.setTouser(dangerReqDTO.getExamineUserId());
            vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的隐患排查审批通知，请前往小程序查看处理。"));
            msgService.sendTextMsg(vxSendTextMsgReqDTO);
        }
    }

    @Override
    public void deleteDanger(DangerReqDTO dangerReqDTO) {
        if (Objects.isNull(dangerReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = dangerMapper.selectIsDangerExamine(dangerReqDTO.getId());
        if (result != 0 && result != 4) {
            throw new CommonException(ErrorCode.RESOURCE_USE);
        }
        dangerReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = dangerMapper.deleteDanger(dangerReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public void examineDanger(String dangerId, String deptId, String opinion, Integer status) {
        List<UserResDTO> users = deptService.getDeptUser(deptId, dangerId);
        if (users == null || users.isEmpty()) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        DangerExamineResDTO res = dangerMapper.selectUserType(dangerId);
        if (res.getUserType() == 1) {
            for (UserResDTO userResDTO : users) {
                if ("副部长".equals(userResDTO.getUserName())) {
                    Integer result = dangerMapper.examineDanger(res.getId(), opinion, status, TokenUtil.getCurrentPersonNo(), dangerId, res.getUserType() + 1, userResDTO.getId());
                    if (result < 0) {
                        throw new CommonException(ErrorCode.UPDATE_ERROR);
                    } else if (userResDTO.getId() != null && !"".equals(userResDTO.getId())) {
                        VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
                        vxSendTextMsgReqDTO.setTouser(userResDTO.getId());
                        vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的隐患排查审批通知，请前往小程序查看处理。"));
                        msgService.sendTextMsg(vxSendTextMsgReqDTO);
                    }
                    break;
                }
            }
        } else if (res.getUserType() == 2) {
            for (UserResDTO userResDTO : users) {
                if ("部长".equals(userResDTO.getUserName())) {
                    Integer result = dangerMapper.examineDanger(res.getId(), opinion, status, TokenUtil.getCurrentPersonNo(), dangerId, res.getUserType() + 1, userResDTO.getId());
                    if (result < 0) {
                        throw new CommonException(ErrorCode.UPDATE_ERROR);
                    } else if (userResDTO.getId() != null && !"".equals(userResDTO.getId())) {
                        VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
                        vxSendTextMsgReqDTO.setTouser(userResDTO.getId());
                        vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的隐患排查审批通知，请前往小程序查看处理。"));
                        msgService.sendTextMsg(vxSendTextMsgReqDTO);
                    }
                    break;
                }
            }
        } else if (res.getUserType() == 3) {
            DangerResDTO dangerResDTO = getDangerDetail(dangerId);
            if (Objects.isNull(dangerResDTO)) {
                throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
            }
            String checkUserId = dangerMapper.selectCheckUserId(dangerId);
            if (Objects.isNull(checkUserId)) {
                throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
            }
            Integer result = dangerMapper.examineDanger(res.getId(), opinion, status, TokenUtil.getCurrentPersonNo(), dangerId, res.getUserType() + 1, checkUserId);
            if (result < 0) {
                throw new CommonException(ErrorCode.UPDATE_ERROR);
            } else if (!"".equals(checkUserId)) {
                VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
                vxSendTextMsgReqDTO.setTouser(checkUserId);
                vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的隐患排查审批通知，请前往小程序查看处理。"));
                msgService.sendTextMsg(vxSendTextMsgReqDTO);
            }
        }
    }

    @Override
    public void issueDanger(String dangerId, String deptId, String rectifyTerm, String opinion) {
        Integer result = dangerMapper.issueDanger(dangerId, deptId, rectifyTerm, opinion, TokenUtil.getCurrentPersonNo());
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

}
