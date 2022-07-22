package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.DangerReqDTO;
import com.security.info.manage.dto.req.VxSendTextMsgReqDTO;
import com.security.info.manage.dto.res.*;
import com.security.info.manage.entity.EntryPlate;
import com.security.info.manage.entity.User;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.DeptMapper;
import com.security.info.manage.mapper.FileMapper;
import com.security.info.manage.mapper.DangerMapper;
import com.security.info.manage.mapper.SysMapper;
import com.security.info.manage.service.DangerService;
import com.security.info.manage.service.DeptService;
import com.security.info.manage.service.MsgService;
import com.security.info.manage.utils.ExcelPortUtil;
import com.security.info.manage.utils.ObjectUtils;
import com.security.info.manage.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
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
    private DeptMapper deptMapper;

    @Autowired
    private MsgService msgService;

    @Value("${vx-business.jumppage}")
    private String jumppage;

    @Override
    public List<DeptTreeResDTO> listDept(Integer type) {
        return dangerMapper.listDept(type);
    }

    @Override
    public List<EntryPlate> listPlate(Integer type, String deptId, String deptName) {
        return dangerMapper.listPlate(type, deptId, deptName);
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
                if (resDTO.getStatus() == 3) {
                    resDTO.setUserStatus(dangerMapper.selectExamineUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo(), 1) == 0 ? 1 : 0);
                } else if (resDTO.getStatus() == 4) {
                    resDTO.setUserStatus(dangerMapper.selectExamineUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo(), 2) == 0 ? 1 : 0);
                } else if (resDTO.getStatus() == 5) {
                    resDTO.setUserStatus(dangerMapper.selectCheckUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
                } else if (resDTO.getStatus() == 0) {
                    resDTO.setUserStatus(dangerMapper.selectCreateUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
                } else {
                    resDTO.setUserStatus(dangerMapper.selectUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
                }
                DangerRectifyResDTO dangerRectify = dangerMapper.getDangerRectify(resDTO.getId());
                if (!Objects.isNull(dangerRectify)) {
                    if (dangerRectify.getAfterPic() != null && !"".equals(dangerRectify.getAfterPic())) {
                        dangerRectify.setAfterPicFile(fileMapper.selectFileInfo(Arrays.asList(dangerRectify.getAfterPic().split(","))));
                    }
                }
                resDTO.setDangerRectify(dangerRectify);
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
                if (resDTO.getStatus() == 3) {
                    resDTO.setUserStatus(dangerMapper.selectExamineUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo(), 1) == 0 ? 1 : 0);
                } else if (resDTO.getStatus() == 4) {
                    resDTO.setUserStatus(dangerMapper.selectExamineUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo(), 2) == 0 ? 1 : 0);
                } else if (resDTO.getStatus() == 5) {
                    resDTO.setUserStatus(dangerMapper.selectCheckUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
                } else if (resDTO.getStatus() == 0) {
                    resDTO.setUserStatus(dangerMapper.selectCreateUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
                } else {
                    resDTO.setUserStatus(dangerMapper.selectUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
                }
                DangerRectifyResDTO dangerRectify = dangerMapper.getDangerRectify(resDTO.getId());
                if (!Objects.isNull(dangerRectify)) {
                    if (dangerRectify.getAfterPic() != null && !"".equals(dangerRectify.getAfterPic())) {
                        dangerRectify.setAfterPicFile(fileMapper.selectFileInfo(Arrays.asList(dangerRectify.getAfterPic().split(","))));
                    }
                }
                resDTO.setDangerRectify(dangerRectify);
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
//                resDTO.setDangerExamines(dangerMapper.listDangerExamine(resDTO.getId()));
//                if (resDTO.getStatus() == 3) {
//                    resDTO.setUserStatus(dangerMapper.selectExamineUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo(), 1) == 0 ? 1 : 0);
//                } else if (resDTO.getStatus() == 4) {
//                    resDTO.setUserStatus(dangerMapper.selectExamineUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo(), 2) == 0 ? 1 : 0);
//                } else if (resDTO.getStatus() == 5) {
//                    resDTO.setUserStatus(dangerMapper.selectCheckUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
//                } else if (resDTO.getStatus() == 0) {
//                    resDTO.setUserStatus(dangerMapper.selectCreateUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
//                } else {
//                    resDTO.setUserStatus(dangerMapper.selectUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
//                }
//                DangerRectifyResDTO dangerRectify = dangerMapper.getDangerRectify(resDTO.getId());
//                if (!Objects.isNull(dangerRectify)) {
//                    if (dangerRectify.getAfterPic() != null && !"".equals(dangerRectify.getAfterPic())) {
//                        dangerRectify.setAfterPicFile(fileMapper.selectFileInfo(Arrays.asList(dangerRectify.getAfterPic().split(","))));
//                    }
//                }
//                resDTO.setDangerRectify(dangerRectify);
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
        if (res.getStatus() == 3) {
            res.setUserStatus(dangerMapper.selectExamineUserStatus(res.getId(), TokenUtil.getCurrentPersonNo(), 1) == 0 ? 1 : 0);
        } else if (res.getStatus() == 4) {
            res.setUserStatus(dangerMapper.selectExamineUserStatus(res.getId(), TokenUtil.getCurrentPersonNo(), 2) == 0 ? 1 : 0);
        } else if (res.getStatus() == 5) {
            res.setUserStatus(dangerMapper.selectCheckUserStatus(res.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
        } else if (res.getStatus() == 0) {
            res.setUserStatus(dangerMapper.selectCreateUserStatus(res.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
        } else {
            res.setUserStatus(dangerMapper.selectUserStatus(res.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
        }
        if ("0".equals(res.getBuildDeptId())) {
            res.setBuildDeptName("公共板块");
        }
        DangerRectifyResDTO dangerRectify = dangerMapper.getDangerRectify(id);
        if (!Objects.isNull(dangerRectify)) {
            if (dangerRectify.getAfterPic() != null && !"".equals(dangerRectify.getAfterPic())) {
                dangerRectify.setAfterPicFile(fileMapper.selectFileInfo(Arrays.asList(dangerRectify.getAfterPic().split(","))));
            }
        }
        res.setDangerRectify(dangerRectify);
        return res;
    }

    @Override
    public void modifyDanger(DangerReqDTO dangerReqDTO) {
        if (Objects.isNull(dangerReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        if (dangerReqDTO.getId() != null) {
            Integer result = dangerMapper.selectIsDangerExamine(dangerReqDTO.getId());
            if (result != 0 && result != 4) {
                throw new CommonException(ErrorCode.RESOURCE_USE);
            }
        }
        dangerReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        if (dangerReqDTO.getCheckUserId() == null || "".equals(dangerReqDTO.getCheckUserId())) {
            dangerReqDTO.setCheckUserId(TokenUtil.getCurrentPersonNo());
            dangerReqDTO.setCheckDeptId(TokenUtil.getCurrentPersonDeptId());
            dangerReqDTO.setCheckTime(new Date(System.currentTimeMillis()));
        }
        Integer result = dangerMapper.modifyDanger(dangerReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        } else if (dangerReqDTO.getIsUse() == 1 && dangerReqDTO.getExamineUserId() != null) {
            VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
            vxSendTextMsgReqDTO.setTouser(dangerReqDTO.getExamineUserId());
            vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的隐患排查审批通知，请前往小程序查看处理。" +
                    "<a href=\"" + jumppage + "?page=pages/reportProblems/index&type=4&id=" + dangerReqDTO.getId() + "\">跳转小程序</a>"));
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
        if (dangerReqDTO.getCheckUserId() == null || "".equals(dangerReqDTO.getCheckUserId())) {
            dangerReqDTO.setCheckUserId(TokenUtil.getCurrentPersonNo());
            dangerReqDTO.setCheckDeptId(TokenUtil.getCurrentPersonDeptId());
            dangerReqDTO.setCheckTime(new Date(System.currentTimeMillis()));
        }
        Integer result = dangerMapper.addDanger(dangerReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        } else if (dangerReqDTO.getIsUse() == 1 && dangerReqDTO.getExamineUserId() != null) {
            VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
            vxSendTextMsgReqDTO.setTouser(dangerReqDTO.getExamineUserId());
            vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的隐患排查审批通知，请前往小程序查看处理。" +
                    "<a href=\"" + jumppage + "?page=pages/reportProblems/index&type=4&id=" + dangerReqDTO.getId() + "\">跳转小程序</a>"));
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
    public List<UserResDTO> examineUserList(String deptId, Integer userType) {
        String res = deptMapper.upRecursion(deptId);
        if (res != null) {
            List<String> ids = deptMapper.downRecursion(res);
            if (ids != null && !ids.isEmpty()) {
                List<UserResDTO> list = dangerMapper.examineUserList(ids, userType);
                if (!Objects.isNull(list) && !list.isEmpty()) {
                    return list;
                }
            }
        }
        throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
    }

    @Override
    public void examineDanger(String dangerId, String userId, String opinion, Integer status) {
        DangerExamineResDTO res = dangerMapper.selectUserType(dangerId);
        if (res.getUserType() == 3) {
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
            } else if (!"".equals(checkUserId) && status == 1) {
                VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
                vxSendTextMsgReqDTO.setTouser(checkUserId);
                vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的隐患排查待下发，请前往小程序查看处理。" +
                        "<a href=\"" + jumppage + "?page=pages/reportProblems/index&type=4&id=" + dangerId + "\">跳转小程序</a>"));
                msgService.sendTextMsg(vxSendTextMsgReqDTO);
            }
        } else {
            Integer result = dangerMapper.examineDanger(res.getId(), opinion, status, TokenUtil.getCurrentPersonNo(), dangerId, res.getUserType() + 1, userId);
            if (result < 0) {
                throw new CommonException(ErrorCode.UPDATE_ERROR);
            } else if (userId != null && !"".equals(userId) && status == 1) {
                VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
                vxSendTextMsgReqDTO.setTouser(userId);
                vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的隐患排查审批通知，请前往小程序查看处理。" +
                        "<a href=\"" + jumppage + "?page=pages/reportProblems/index&type=4&id=" + dangerId + "\">跳转小程序</a>"));
                msgService.sendTextMsg(vxSendTextMsgReqDTO);
            }
        }
    }

    @Override
    public void issueDanger(String dangerId, String deptId, String userId, String rectifyTerm, String opinion) {
        DeptTreeResDTO res = deptMapper.selectParent(deptId);
        if (!Objects.isNull(res) && !"1".equals(res.getId())) {
            while (!"1".equals(res.getParentId())) {
                res = deptMapper.selectParent(res.getId());
                if (Objects.isNull(res)) {
                    break;
                }
            }
            deptId = res.getId();
        }
        Integer result = dangerMapper.issueDanger(dangerId, deptId, Arrays.asList(userId.split(",")), rectifyTerm, opinion, TokenUtil.getCurrentPersonNo());
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
        VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
        vxSendTextMsgReqDTO.setTouser(userId.replaceAll(",", "|"));
        vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的隐患整改通知，请前往小程序查看处理。" +
                "<a href=\"" + jumppage + "?page=pages/reportProblems/index&type=5&id=" + dangerId + "\">跳转小程序</a>"));
        msgService.sendTextMsg(vxSendTextMsgReqDTO);
    }

    @Override
    public void rectifyDanger(String dangerId, String userId, String rectifyMeasure, String afterPic) {
        Integer result = dangerMapper.rectifyDanger(dangerId, userId, rectifyMeasure, afterPic, TokenUtil.getCurrentPersonNo());
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
        VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
        vxSendTextMsgReqDTO.setTouser(userId);
        vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的隐患整改审批通知，请前往小程序查看处理。" +
                "<a href=\"" + jumppage + "?page=pages/reportProblems/index&type=6&id=" + dangerId + "\">跳转小程序</a>"));
        msgService.sendTextMsg(vxSendTextMsgReqDTO);
    }

    @Override
    public void rectifyExamineDanger(String dangerId, Integer status) {
        Integer result = dangerMapper.rectifyExamineDanger(dangerId, status, TokenUtil.getCurrentPersonNo());
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void rectifyPassDanger(String dangerId, Integer status) {
        Integer result = dangerMapper.rectifyPassDanger(dangerId, status, TokenUtil.getCurrentPersonNo());
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void exportDanger(HttpServletResponse response) {
        List<String> listName = Arrays.asList("编号", "时间", "地点", "检查部门", "检查人", "问题", "标准化工区建设-版块",
                "标准化工区建设-标准化词条", "标准化工区建设-扣除分值", "标准化工区建设-换算分值", "安全生产标准化-版块",
                "安全生产标准化-类别", "安全生产标准化-词条", "安全生产标准化-考核分值", "安全隐患排查与治理-隐患类别",
                "安全隐患排查与治理-隐患等级", "图片", "整改时限", "是否销项", "整改/防护措施", "整改后图片", "责任部门",
                "责任工区", "整改责任人", "备注");
        List<DangerExportResDTO> exportDanger = dangerMapper.exportDanger();
        List<Map<String, String>> list = new ArrayList<>();
        if (exportDanger != null && !exportDanger.isEmpty()) {
            for (DangerExportResDTO resDTO : exportDanger) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Map<String, String> map = new HashMap<>();
                map.put("编号", resDTO.getNo());
                map.put("时间", sdf.format(resDTO.getCheckTime()));
                map.put("地点", resDTO.getRegionName() + "-" + resDTO.getAddress());
                map.put("检查部门", resDTO.getCheckDeptName());
                map.put("检查人", resDTO.getCheckUserName());
                map.put("问题", resDTO.getContent());
                map.put("标准化工区建设-版块", resDTO.getBuildPlateName());
                map.put("标准化工区建设-标准化词条", resDTO.getBuildEntry());
                map.put("标准化工区建设-扣除分值", String.valueOf(resDTO.getBuildScore()));
                map.put("标准化工区建设-换算分值", String.valueOf(resDTO.getBuildConversionScore()));
                map.put("安全生产标准化-版块", resDTO.getProdPlateName());
                map.put("安全生产标准化-类别", resDTO.getProdCategory());
                map.put("安全生产标准化-词条", resDTO.getProdEntry());
                map.put("安全生产标准化-考核分值", String.valueOf(resDTO.getProdEntryScore()));
                map.put("安全隐患排查与治理-隐患类别", resDTO.getDangerCategory() == 1 ? "安全装置" :
                        resDTO.getDangerCategory() == 2 ? "设备设施" :
                                resDTO.getDangerCategory() == 3 ? "管理" :
                                        resDTO.getDangerCategory() == 4 ? "作业环境" :
                                                resDTO.getDangerCategory() == 5 ? "作业行为" : "其他");
                map.put("安全隐患排查与治理-隐患等级", resDTO.getLevel() == 1 ? "一般" : "重大");
                map.put("图片", resDTO.getBeforePic());
                map.put("整改时限", sdf.format(resDTO.getRectifyTerm()));
                map.put("是否销项", resDTO.getIsEliminate() == 0 ? "否" : "是");
                map.put("整改/防护措施", resDTO.getRectifyMeasure());
                map.put("整改后图片", resDTO.getAfterPic());
                DeptTreeResDTO res = deptMapper.selectParent(resDTO.getResponsibilityDeptId());
                if (!Objects.isNull(res) && !"1".equals(res.getId())) {
                    while (!"1".equals(res.getParentId())) {
                        res = deptMapper.selectParent(res.getId());
                        if (Objects.isNull(res)) {
                            break;
                        }
                    }
                }
                map.put("责任部门", res.getOrgName());
                map.put("责任工区", resDTO.getResponsibilityDeptName());
                map.put("整改责任人", resDTO.getRectifyUserName());
                map.put("备注", "");
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("问题检查汇总表", listName, list, null, response);
    }

    @Override
    public List<DangerTypeStatisticsResDTO> dangerTypeStatistics(String date) {
        return dangerMapper.dangerTypeStatistics(date);
    }

    @Override
    public List<DangerDeptStatisticsResDTO> dangerDeptStatistics(String date) {
        List<DangerDeptStatisticsResDTO> dept = dangerMapper.selectAllDeptName();
        if (!Objects.isNull(dept) && !dept.isEmpty()) {
            for (DangerDeptStatisticsResDTO res : dept) {
                DangerDeptStatisticsResDTO.DeptStatistics deptStatistics = dangerMapper.dangerDeptStatistics(res.getDeptId(), date);
                if (Objects.isNull(deptStatistics)) {
                    deptStatistics = new DangerDeptStatisticsResDTO.DeptStatistics();
                    deptStatistics.setTotal(0);
                    deptStatistics.setLegacy(0);
                    deptStatistics.setNowAdd(0);
                    deptStatistics.setNowSolve(0);
                }
                res.setDeptStatistics(deptStatistics);
            }
        }
        return dept;
    }

    @Override
    public List<DangerRegionStatisticsResDTO> dangerRegionStatistics(String regionId, String date) {
        List<DangerRegionStatisticsResDTO> list = dangerMapper.selectRootRegion(regionId);
        if (!Objects.isNull(list) && !list.isEmpty()) {
            for (DangerRegionStatisticsResDTO res : list) {
                List<String> regions = dangerMapper.selectBodyRegion(res.getRegionId());
                regions.add(res.getRegionId());
                DangerRegionStatisticsResDTO.RegionStatistics regionStatistics = dangerMapper.dangerRegionStatistics(regions, date);
                if (Objects.isNull(regionStatistics)) {
                    regionStatistics = new DangerRegionStatisticsResDTO.RegionStatistics();
                    regionStatistics.setTotal(0);
                    regionStatistics.setLastLegacy(0);
                    regionStatistics.setNowAdd(0);
                    regionStatistics.setNowSolve(0);
                    regionStatistics.setNowLegacy(0);
                    regionStatistics.setCycleAddBroadcast(0);
                    regionStatistics.setCycleSolveBroadcast(0);
                }
                res.setRegionStatistics(regionStatistics);
            }
        }
        return list;
    }

    @Override
    public List<String> listUnitStatistics(String regionId) {
        return dangerMapper.listUnitStatistics(regionId);
    }

    @Override
    public List<String> listWorkAreaStatistics(String regionId) {
        return dangerMapper.listWorkAreaStatistics(regionId);
    }

    @Override
    public DangerChartStatisticsResDTO chartStatistics(String regionId, String unit, String workArea) {
        DangerChartStatisticsResDTO resDTO = new DangerChartStatisticsResDTO();
        List<DangerChartStatisticsResDTO.ChartStatistics> newAdd = dangerMapper.newAddStatistics(regionId, unit, workArea);
        List<DangerChartStatisticsResDTO.ChartStatistics> legacy = dangerMapper.legacyStatistics(regionId, unit, workArea);
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        for (int i = 1; i <= month; i++) {
            String finalI = String.valueOf(i);
            if (newAdd.stream().noneMatch(m -> m.getMonth().equals(finalI))) {
                DangerChartStatisticsResDTO.ChartStatistics res = new DangerChartStatisticsResDTO.ChartStatistics();
                res.setMonth(finalI);
                res.setNum(0);
                newAdd.add(Integer.parseInt(finalI) - 1, res);
            }
            if (legacy.stream().noneMatch(m -> m.getMonth().equals(finalI))) {
                DangerChartStatisticsResDTO.ChartStatistics res = new DangerChartStatisticsResDTO.ChartStatistics();
                res.setMonth(finalI);
                res.setNum(0);
                legacy.add(Integer.parseInt(finalI) - 1, res);
            }
        }
        resDTO.setNewAddStatistics(newAdd);
        resDTO.setLegacyStatistics(legacy);
        return resDTO;
    }

    @Override
    public List<DangerMonthStatisticsResDTO> dangerMonthStatistics(String month) {
        if (Objects.isNull(month)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        List<DangerMonthStatisticsResDTO> list = new ArrayList<>();
        String[] s = new String[]{"安全装置", "设备设施", "管理", "作业环境", "作业行为", "其他"};
        for (int i = 0; i < s.length; i++) {
            DangerMonthStatisticsResDTO resDTO = dangerMapper.dangerMonthStatistics(i + 1, month);
            resDTO.setName(s[i]);
            list.add(resDTO);
        }
        return list;
    }

}
