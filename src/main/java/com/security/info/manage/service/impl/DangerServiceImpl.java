package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.DangerReqDTO;
import com.security.info.manage.dto.req.NewEReqDTO;
import com.security.info.manage.dto.req.NewUserReqDTO;
import com.security.info.manage.dto.req.VxSendTextMsgReqDTO;
import com.security.info.manage.dto.res.*;
import com.security.info.manage.entity.EntryPlate;
import com.security.info.manage.entity.User;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.*;
import com.security.info.manage.service.DangerService;
import com.security.info.manage.service.DeptService;
import com.security.info.manage.service.MsgService;
import com.security.info.manage.utils.*;
import jdk.nashorn.internal.parser.Token;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.security.info.manage.utils.Constants.XLS;
import static com.security.info.manage.utils.Constants.XLSX;

/**
 * @author frp
 */
@Service
@Slf4j
public class DangerServiceImpl implements DangerService {

    public static final String SERIAL_NUMBER_WT_DANGER = ":serial:num:danger:wt";
    public static final String WT_DANGER_NO = "ZTT-WT-";

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

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

    @Autowired
    private UserMapper userMapper;

    @Value("${vx-business.jumppage}")
    private String jumppage;

    @Value("${pro.name}")
    private String proName;

    @Value("${spring.redis.key-prefix}")
    private String keyPrefix;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
    public void syncEntry() {
        //TODO 
        File ff = new File("C:\\import.xlsx");
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
        try{
            Workbook workbook;
            FileInputStream fileInputStream = new FileInputStream(ff);
            workbook = new XSSFWorkbook(fileInputStream);

            Sheet sheet = workbook.getSheetAt(0);
            List<EntryPlate.Entry> temp = new ArrayList<>();

            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                EntryPlate.Entry reqDTO = new EntryPlate.Entry();
                cells.getCell(0).setCellType(1);
                reqDTO.setId(cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(1);
                reqDTO.setPlateId(cells.getCell(1).getStringCellValue());
                cells.getCell(2).setCellType(1);
                reqDTO.setContent(cells.getCell(2).getStringCellValue());

                temp.add(reqDTO);
            }
            fileInputStream.close();
            DangerMapper dangerMapper = sqlSession.getMapper(DangerMapper.class);
            for (EntryPlate.Entry s : temp) {
                dangerMapper.addEntry(s);
            }


            sqlSession.commit();
            sqlSession.flushStatements();
            sqlSession.clearCache();
            sqlSession.close();
        }catch (Exception e){

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
    public Page<DangerResDTO> listDanger(Integer type,Integer status, String name, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<DangerResDTO> page;
        if (sysMapper.selectIfAdmin(TokenUtil.getCurrentPersonNo()) == 1) {
            page = dangerMapper.listDanger(pageReqDTO.of(), type, status, name, null);
        } else {
            page = dangerMapper.listDanger(pageReqDTO.of(), type, status, name, TokenUtil.getCurrentPersonNo());
        }
        List<DangerResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
            List<String> userRoles = userMapper.selectUserRole(TokenUtil.getCurrentPersonNo());
            for (DangerResDTO resDTO : list) {
//                if (resDTO.getBeforePic() != null && !resDTO.getBeforePic().isEmpty() && StringUtils.isNotBlank(resDTO.getBeforePic())) {
//                    List<String> beforePicList = Arrays.asList(resDTO.getBeforePic().split(","));
//                    if (!beforePicList.isEmpty()) {
//                        resDTO.setBeforePicFile(fileMapper.selectFileInfo(beforePicList));
//                    }
//                }
                resDTO.setDangerExamines(dangerMapper.listDangerExamine(resDTO.getId()));
                DangerRectifyResDTO dangerRectify = dangerMapper.getDangerRectify(resDTO.getId());
//                if (!Objects.isNull(dangerRectify)) {
//                    if (dangerRectify.getAfterPic() != null && !dangerRectify.getAfterPic().isEmpty() && StringUtils.isNotBlank(dangerRectify.getAfterPic())) {
//                        List<String> afterPicList = Arrays.asList(dangerRectify.getAfterPic().split(","));
//                        if (!afterPicList.isEmpty()) {
//                            dangerRectify.setAfterPicFile(fileMapper.selectFileInfo(afterPicList));
//                        }
//                    }
//                }
                resDTO.setDangerRectify(dangerRectify);
                if (userRoles != null && !userRoles.isEmpty()) {
                    if (userRoles.contains("系统管理员")) {
                        resDTO.setUserRole(1);
                    } else {
                        resDTO.setUserRole(0);
                    }
                }
                switch (resDTO.getStatus()) {
                    case 3:
                        resDTO.setUserStatus(dangerMapper.selectExamineUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo(), 1) == 0 ? 1 : 0);
                        break;
                    case 4:
                        resDTO.setUserStatus(dangerMapper.selectExamineUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo(), 2) == 0 ? 1 : 0);
                        break;
                    case 5:
                        resDTO.setUserStatus(dangerMapper.selectCheckUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
                        break;
                    case 0:
                        resDTO.setUserStatus(dangerMapper.selectCreateUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
                        break;
                    default:
                        resDTO.setUserStatus(dangerMapper.selectUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
                        break;
                }
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
//                if (resDTO.getBeforePic() != null && !resDTO.getBeforePic().isEmpty() && StringUtils.isNotBlank(resDTO.getBeforePic())) {
//                    List<String> beforePicList = Arrays.asList(resDTO.getBeforePic().split(","));
//                    if (!beforePicList.isEmpty()) {
//                        resDTO.setBeforePicFile(fileMapper.selectFileInfo(beforePicList));
//                    }
//                }
                resDTO.setDangerExamines(dangerMapper.listDangerExamine(resDTO.getId()));
                DangerRectifyResDTO dangerRectify = dangerMapper.getDangerRectify(resDTO.getId());
//                if (!Objects.isNull(dangerRectify)) {
//                    if (dangerRectify.getAfterPic() != null && !dangerRectify.getAfterPic().isEmpty() && StringUtils.isNotBlank(dangerRectify.getAfterPic())) {
//                        List<String> afterPicList = Arrays.asList(dangerRectify.getAfterPic().split(","));
//                        if (!afterPicList.isEmpty()) {
//                            dangerRectify.setAfterPicFile(fileMapper.selectFileInfo(afterPicList));
//                        }
//                    }
//                }
                resDTO.setDangerRectify(dangerRectify);
                switch (resDTO.getStatus()) {
                    case 3:
                        resDTO.setUserStatus(dangerMapper.selectExamineUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo(), 1) == 0 ? 1 : 0);
                        break;
                    case 4:
                        resDTO.setUserStatus(dangerMapper.selectExamineUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo(), 2) == 0 ? 1 : 0);
                        break;
                    case 5:
                        resDTO.setUserStatus(dangerMapper.selectCheckUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
                        break;
                    case 0:
                        resDTO.setUserStatus(dangerMapper.selectCreateUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
                        break;
                    default:
                        resDTO.setUserStatus(dangerMapper.selectUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
                        break;
                }
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
                if (resDTO.getBeforePic() != null && !resDTO.getBeforePic().isEmpty() && StringUtils.isNotBlank(resDTO.getBeforePic())) {
                    List<String> beforePicList = Arrays.asList(resDTO.getBeforePic().split(","));
                    if (!beforePicList.isEmpty()) {
                        resDTO.setBeforePicFile(fileMapper.selectFileInfo(beforePicList));
                    }
                }
//                resDTO.setDangerExamines(dangerMapper.listDangerExamine(resDTO.getId()));
//                DangerRectifyResDTO dangerRectify = dangerMapper.getDangerRectify(resDTO.getId());
//                if (!Objects.isNull(dangerRectify)) {
//                    if (dangerRectify.getAfterPic() != null && !"".equals(dangerRectify.getAfterPic())) {
//                        dangerRectify.setAfterPicFile(fileMapper.selectFileInfo(Arrays.asList(dangerRectify.getAfterPic().split(","))));
//                    }
//                }
//                resDTO.setDangerRectify(dangerRectify);
//                switch (resDTO.getStatus()) {
//                    case 3:
//                        resDTO.setUserStatus(dangerMapper.selectExamineUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo(), 1) == 0 ? 1 : 0);
//                        break;
//                    case 4:
//                        resDTO.setUserStatus(dangerMapper.selectExamineUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo(), 2) == 0 ? 1 : 0);
//                        break;
//                    case 5:
//                        resDTO.setUserStatus(dangerMapper.selectCheckUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
//                        break;
//                    case 0:
//                        resDTO.setUserStatus(dangerMapper.selectCreateUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
//                        break;
//                    default:
//                        resDTO.setUserStatus(dangerMapper.selectUserStatus(resDTO.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
//                        break;
//                }
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
        if (res.getBeforePic() != null && !res.getBeforePic().isEmpty() && StringUtils.isNotBlank(res.getBeforePic())) {
            List<String> beforePicList = Arrays.asList(res.getBeforePic().split(","));
            if (!beforePicList.isEmpty()) {
                res.setBeforePicFile(fileMapper.selectFileInfo(beforePicList));
            }
        }
        res.setDangerExamines(dangerMapper.listDangerExamine(id));
        if ("0".equals(res.getBuildDeptId())) {
            res.setBuildDeptName("公共板块");
        }
        DangerRectifyResDTO dangerRectify = dangerMapper.getDangerRectify(id);
        if (!Objects.isNull(dangerRectify)) {
            if (dangerRectify.getAfterPic() != null && !dangerRectify.getAfterPic().isEmpty() && StringUtils.isNotBlank(dangerRectify.getAfterPic())) {
                List<String> afterPicList = Arrays.asList(dangerRectify.getAfterPic().split(","));
                if (!afterPicList.isEmpty()) {
                    dangerRectify.setAfterPicFile(fileMapper.selectFileInfo(afterPicList));
                }
            }
        }
        res.setDangerRectify(dangerRectify);
        switch (res.getStatus()) {
            case 3:
                res.setUserStatus(dangerMapper.selectExamineUserStatus(res.getId(), TokenUtil.getCurrentPersonNo(), 1) == 0 ? 1 : 0);
                break;
            case 4:
                res.setUserStatus(dangerMapper.selectExamineUserStatus(res.getId(), TokenUtil.getCurrentPersonNo(), 2) == 0 ? 1 : 0);
                break;
            case 5:
                res.setUserStatus(dangerMapper.selectCheckUserStatus(res.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
                break;
            case 0:
                res.setUserStatus(dangerMapper.selectCreateUserStatus(res.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
                break;
            default:
                res.setUserStatus(dangerMapper.selectUserStatus(res.getId(), TokenUtil.getCurrentPersonNo()) == 0 ? 1 : 0);
                break;
        }
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
        if (dangerReqDTO.getCheckUserId() == null || dangerReqDTO.getCheckUserId().isEmpty()) {
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
                    "<a href=\"" + jumppage + "?page=pages/problemRecord/myApproval/index&type=4&id=" + dangerReqDTO.getId() + "\">跳转小程序</a>"));
            msgService.sendTextMsg(vxSendTextMsgReqDTO);
        }
    }

    @Override
    public void addDanger(DangerReqDTO dangerReqDTO) {
        if (Objects.isNull(dangerReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        dangerReqDTO.setId(TokenUtil.getUuId());
        //流水号生成
        String no;
        no = proName + keyPrefix + SERIAL_NUMBER_WT_DANGER;
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(no))) {
            try {
                stringRedisTemplate.opsForValue().set(no, "1", 25, TimeUnit.HOURS);
            } catch (Exception e) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                e.printStackTrace(new PrintStream(out));
                throw new CommonException(ErrorCode.CACHE_ERROR, no, out.toString());
            }
        }
        int num = Integer.parseInt(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(no)));
        String numStr = String.valueOf(num);
        StringBuilder str = new StringBuilder(numStr);
        for (int i = 0; i < 4 - numStr.length(); i++) {
            str.insert(0, "0");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(new Date(System.currentTimeMillis()));
        dangerReqDTO.setNo(WT_DANGER_NO + today + "-" + str);
        stringRedisTemplate.opsForValue().set(no, String.valueOf(num + 1), 0);
        dangerReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        if (dangerReqDTO.getCheckUserId() == null || dangerReqDTO.getCheckUserId().isEmpty()) {
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
                    "<a href=\"" + jumppage + "?page=pages/problemRecord/myApproval/index&type=4&id=" + dangerReqDTO.getId() + "\">跳转小程序</a>"));
            msgService.sendTextMsg(vxSendTextMsgReqDTO);
        }
    }

    @Override
    public void deleteDanger(DangerReqDTO dangerReqDTO) {
        if (Objects.isNull(dangerReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        dangerReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = dangerMapper.deleteDanger(dangerReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public List<UserResDTO> examineUserList(String deptId, Integer userType) {
        while (!"1".equals(deptMapper.upRecursion(deptId)) && deptMapper.upRecursion(deptId) != null) {
            deptId = deptMapper.upRecursion(deptId);
        }
        if (deptId != null) {
            List<String> ids = deptMapper.downRecursion(deptId);
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
    @Transactional(rollbackFor = Exception.class)
    public void examineDanger(String dangerId, String userId, String opinion, Integer status) {
        String checkUserId = dangerMapper.selectCheckUserId(dangerId);
        if (Objects.isNull(checkUserId)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        DangerExamineResDTO res = dangerMapper.selectUserType(dangerId);
        if (res.getUserType() == 3) {
            DangerResDTO dangerResDTO = getDangerDetail(dangerId);
            if (Objects.isNull(dangerResDTO)) {
                throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
            }

            Integer result = dangerMapper.examineDanger(res.getId(), opinion, status, TokenUtil.getCurrentPersonNo(), dangerId, res.getUserType() + 1, checkUserId);
            if (result < 0) {
                throw new CommonException(ErrorCode.UPDATE_ERROR);
            } else if (!checkUserId.isEmpty() && status == 1) {
                VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
                vxSendTextMsgReqDTO.setTouser(checkUserId);
                vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的隐患排查待下发，请前往小程序查看处理。" +
                        "<a href=\"" + jumppage + "?page=pages/problemRecord/distribute/index&type=4&id=" + dangerId + "\">跳转小程序</a>"));
                msgService.sendTextMsg(vxSendTextMsgReqDTO);
            }
        } else {
            Integer result = dangerMapper.examineDanger(res.getId(), opinion, status, TokenUtil.getCurrentPersonNo(), dangerId, res.getUserType() + 1, userId);
            if (result < 0) {
                throw new CommonException(ErrorCode.UPDATE_ERROR);
            } else if (userId != null && !userId.isEmpty() && status == 1) {
                VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
                vxSendTextMsgReqDTO.setTouser(userId);
                vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的隐患排查审批通知，请前往小程序查看处理。" +
                        "<a href=\"" + jumppage + "?page=pages/problemRecord/myApproval/index&type=4&id=" + dangerId + "\">跳转小程序</a>"));
                msgService.sendTextMsg(vxSendTextMsgReqDTO);
            }
        }

        //增加回退通知
        if (status == 2) {
            VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
            vxSendTextMsgReqDTO.setTouser(checkUserId);
            vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您上报的隐患被退回，请前往小程序查看处理。"));
            msgService.sendTextMsg(vxSendTextMsgReqDTO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
                "<a href=\"" + jumppage + "?page=pages/problemRecord/myRectification/index&type=5&id=" + dangerId + "\">跳转小程序</a>"));
        msgService.sendTextMsg(vxSendTextMsgReqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rectifyDanger(String dangerId, String userId, String rectifyMeasure, String afterPic) {
        Integer result = dangerMapper.rectifyDanger(dangerId, userId, rectifyMeasure, afterPic, TokenUtil.getCurrentPersonNo());
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
        VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
        vxSendTextMsgReqDTO.setTouser(userId);
        vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的隐患整改审批通知，请前往小程序查看处理。" +
                "<a href=\"" + jumppage + "?page=pages/problemRecord/myApproval/index&type=6&id=" + dangerId + "\">跳转小程序</a>"));
        msgService.sendTextMsg(vxSendTextMsgReqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rectifyExamineDanger(String dangerId, Integer status) {
        Integer result = dangerMapper.rectifyExamineDanger(dangerId, status, TokenUtil.getCurrentPersonNo());
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
        //20220922 addBy zhangxin 部长驳回通知
        if (status == 2) {
            String rectifyUserId = dangerMapper.selectRectifyUserId(dangerId);
            VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
            vxSendTextMsgReqDTO.setTouser(rectifyUserId);
            vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的隐患整改被退回，请前往小程序查看处理。" +
                    "<a href=\"" + jumppage + "?page=pages/problemRecord/myRectification/index&type=5&id=" + dangerId + "\">跳转小程序</a>"));
            msgService.sendTextMsg(vxSendTextMsgReqDTO);
        } else if (status == 1) {
            //整改完成通知
            String checkUserId = dangerMapper.selectCheckUserId(dangerId);
            VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
            vxSendTextMsgReqDTO.setTouser(checkUserId);
            vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您上报的隐患已整改完成，请前往小程序审核。" +
                    "<a href=\"" + jumppage + "?page=pages/problemRecord/myApproval/index&type=4&id=" + dangerId + "\">跳转小程序</a>"));
            msgService.sendTextMsg(vxSendTextMsgReqDTO);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rectifyPassDanger(String dangerId, String condition, Integer status) {
        Integer result = dangerMapper.rectifyPassDanger(dangerId, condition, status, TokenUtil.getCurrentPersonNo());
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void exportDanger(Integer type, Integer status, String name, HttpServletResponse response) {
        List<String> listName = Arrays.asList("编号", "时间", "地点", "检查部门", "检查人", "问题", "标准化工区建设-版块",
                "标准化工区建设-标准化词条", "标准化工区建设-扣除分值", "标准化工区建设-换算分值", "安全生产标准化-版块", "安全生产标准化-类别",
                "安全生产标准化-词条", "安全生产标准化-考核分值", "安全隐患排查与治理-隐患类别", "安全隐患排查与治理-隐患等级", "整改时限",
                "是否销项", "整改/防护措施", "责任部门", "责任工区", "整改责任人", "状态", "备注");
        List<DangerExportResDTO> exportDanger;
        if (sysMapper.selectIfAdmin(TokenUtil.getCurrentPersonNo()) == 1) {
            exportDanger = dangerMapper.exportDanger(type, status, name, null);
        } else {
            exportDanger = dangerMapper.exportDanger(type, status, name, TokenUtil.getCurrentPersonNo());
        }
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
                map.put("标准化工区建设-扣除分值", resDTO.getBuildScore() == null ? "" : String.valueOf(resDTO.getBuildScore()));
                map.put("标准化工区建设-换算分值", resDTO.getBuildConversionScore() == null ? "" : String.valueOf(resDTO.getBuildConversionScore()));
                map.put("安全生产标准化-版块", resDTO.getProdPlateName());
                map.put("安全生产标准化-类别", resDTO.getProdCategory());
                map.put("安全生产标准化-词条", resDTO.getProdEntry());
                map.put("安全生产标准化-考核分值", resDTO.getProdEntryScore() == null ? "" : String.valueOf(resDTO.getProdEntryScore()));
                map.put("安全隐患排查与治理-隐患类别", resDTO.getDangerCategory() != null ? (resDTO.getDangerCategory() == 1 ? "安全装置" :
                        resDTO.getDangerCategory() == 2 ? "设备设施" :
                                resDTO.getDangerCategory() == 3 ? "管理" :
                                        resDTO.getDangerCategory() == 4 ? "作业环境" :
                                                resDTO.getDangerCategory() == 5 ? "作业行为" : "其他") : "");
                map.put("安全隐患排查与治理-隐患等级", resDTO.getLevel() != null ? (resDTO.getLevel() == 1 ? "一般" : "重大") : "");
                map.put("整改时限", resDTO.getRectifyTerm() == null ? "" : sdf.format(resDTO.getRectifyTerm()));
                map.put("是否销项", resDTO.getIsEliminate() == null ? "" : resDTO.getIsEliminate() == 0 ? "否" : "是");
                map.put("整改/防护措施", resDTO.getRectifyMeasure());
                map.put("责任部门", resDTO.getResponseUnit());
                map.put("责任工区", resDTO.getResponseWorkArea());
                map.put("整改责任人", resDTO.getRectifyUserName());
                map.put("状态", resDTO.getStatus() == 3 ? "整改中" : resDTO.getStatus() == 7 ? "归档" : resDTO.getStatus() == 6 ? "审核不通过" : "审核中");
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
        List<DangerRegionStatisticsResDTO> newList = new ArrayList<>();
        if (!Objects.isNull(list) && !list.isEmpty()) {
            for (DangerRegionStatisticsResDTO res : list) {
                List<String> regions = dangerMapper.selectBodyRegion(res.getRegionId());
                regions.add(res.getRegionId());
                RegionStatisticsResDTO regionStatistics = dangerMapper.dangerRegionStatistics(regions, date);
                if (Objects.isNull(regionStatistics)) {
                    regionStatistics = new RegionStatisticsResDTO();
                    regionStatistics.setTotal(0);
                    regionStatistics.setLastLegacy(0);
                    regionStatistics.setNowAdd(0);
                    regionStatistics.setNowSolve(0);
                    regionStatistics.setNowLegacy(0);
                    regionStatistics.setCycleAddBroadcast(0);
                    regionStatistics.setCycleSolveBroadcast(0);
                }
                res.setRegionStatistics(regionStatistics);
                newList.add(res);
            }
        }
        return newList;
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
