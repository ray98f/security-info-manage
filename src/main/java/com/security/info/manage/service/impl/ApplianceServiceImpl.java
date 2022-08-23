package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.*;
import com.security.info.manage.dto.res.*;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.ApplianceMapper;
import com.security.info.manage.service.ApplianceService;
import com.security.info.manage.service.MsgService;
import com.security.info.manage.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.security.info.manage.utils.Constants.XLS;
import static com.security.info.manage.utils.Constants.XLSX;

/**
 * @author frp
 */
@Service
@Slf4j
public class ApplianceServiceImpl implements ApplianceService {

    @Autowired
    private ApplianceMapper applianceMapper;

    @Autowired
    private MsgService msgService;

    @Override
    public Page<ApplianceResDTO> listAppliance(String name, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return applianceMapper.listAppliance(pageReqDTO.of(), name);
    }

    @Override
    public ApplianceResDTO getApplianceDetail(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        return applianceMapper.getApplianceDetail(id);
    }

    @Override
    public void insertAppliance(ApplianceReqDTO applianceReqDTO) {
        if (Objects.isNull(applianceReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = applianceMapper.selectApplianceIsExist(applianceReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        applianceReqDTO.setId(TokenUtil.getUuId());
        applianceReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = applianceMapper.insertAppliance(applianceReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void modifyAppliance(ApplianceReqDTO applianceReqDTO) {
        if (Objects.isNull(applianceReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = applianceMapper.selectApplianceIsExist(applianceReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        applianceReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        result = applianceMapper.modifyAppliance(applianceReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void deleteAppliance(ApplianceReqDTO applianceReqDTO) {
        if (Objects.isNull(applianceReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        applianceReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        Integer result = applianceMapper.deleteAppliance(applianceReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importApplianceConfig(MultipartFile file) throws Exception {
        try {
            Workbook workbook;
            String fileName = file.getOriginalFilename();
            FileInputStream fileInputStream = new FileInputStream(FileUtils.transferToFile(file));
            if (Objects.requireNonNull(fileName).endsWith(XLS)) {
                workbook = new HSSFWorkbook(fileInputStream);
            } else if (fileName.endsWith(XLSX)) {
                workbook = new XSSFWorkbook(fileInputStream);
            } else {
                throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
            }
            Sheet sheet = workbook.getSheetAt(0);
            List<ApplianceConfigReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                ApplianceConfigReqDTO reqDTO = new ApplianceConfigReqDTO();
                cells.getCell(1).setCellType(1);
                reqDTO.setYear(cells.getCell(1) == null ? null : Integer.valueOf(cells.getCell(1).getStringCellValue()));
                cells.getCell(2).setCellType(1);
                reqDTO.setOrgName(cells.getCell(2) == null ? null : cells.getCell(2).getStringCellValue());
                cells.getCell(3).setCellType(1);
                reqDTO.setDeptName(cells.getCell(3) == null ? null : cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(1);
                reqDTO.setWorkAreaName(cells.getCell(4) == null ? null : cells.getCell(4).getStringCellValue());
                cells.getCell(6).setCellType(1);
                reqDTO.setUserName(cells.getCell(6) == null ? null : cells.getCell(6).getStringCellValue());
                cells.getCell(7).setCellType(1);
                reqDTO.setWorkType(cells.getCell(7) == null ? null : cells.getCell(7).getStringCellValue());
                cells.getCell(8).setCellType(1);
                reqDTO.setHazardFactors(cells.getCell(8) == null ? null : cells.getCell(8).getStringCellValue());
                cells.getCell(9).setCellType(1);
                reqDTO.setApplianceName(cells.getCell(9) == null ? null : cells.getCell(9).getStringCellValue());
                cells.getCell(10).setCellType(1);
                reqDTO.setApplianceCode(cells.getCell(10) == null ? null : cells.getCell(10).getStringCellValue());
                cells.getCell(11).setCellType(1);
                reqDTO.setNum(cells.getCell(11) == null ? null : Integer.valueOf(cells.getCell(11).getStringCellValue()));
                reqDTO.setReceivingTime(cells.getCell(13) == null ? null : cells.getCell(13).getDateCellValue());
                cells.getCell(14).setCellType(1);
                String str = cells.getCell(14) == null ? null : cells.getCell(14).getStringCellValue();
                Matcher m = Pattern.compile("[^0-9]").matcher(Objects.requireNonNull(str));
                reqDTO.setEffectiveTime(DateUtils.getMonthNewDate(reqDTO.getReceivingTime(), Integer.valueOf(m.replaceAll("").trim())));
                cells.getCell(17).setCellType(1);
                reqDTO.setChangeReason(cells.getCell(17) == null ? null : cells.getCell(17).getStringCellValue());
                cells.getCell(18).setCellType(1);
                reqDTO.setRemark(cells.getCell(18) == null ? null : cells.getCell(18).getStringCellValue());
                reqDTO.setId(TokenUtil.getUuId());
                reqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String now = df.format(new Date());
                reqDTO.setStatus(reqDTO.getEffectiveTime().compareTo(df.parse(now)) <= 0 ? 1 : 0);
                temp.add(reqDTO);
            }
            fileInputStream.close();
            if (temp.size() > 0) {
                applianceMapper.importApplianceConfig(temp);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public void exportApplianceConfig(HttpServletResponse response) {
        List<String> listName = Arrays.asList("年度", "部门名称", "科室", "工区名称", "物资类别", "领用人", "涉及相关作业类型",
                "接触职业病危害因素", "防护用品名称", "规格型号", "领取数量", "领取时间", "有效期", "生产厂家", "生产厂家", "合格证",
                "备注", "是否已确认", "状态");
        List<ApplianceConfigResDTO> applianceConfigList = applianceMapper.listApplianceConfig();
        List<Map<String, String>> list = new ArrayList<>();
        if (applianceConfigList != null && !applianceConfigList.isEmpty()) {
            for (ApplianceConfigResDTO applianceConfigResDTO : applianceConfigList) {
                Map<String, String> map = new HashMap<>();
                map.put("年度", applianceConfigResDTO.getYear().toString());
                map.put("部门名称", applianceConfigResDTO.getOrgName());
                map.put("科室", applianceConfigResDTO.getDeptName());
                map.put("工区名称", applianceConfigResDTO.getWorkAreaName());
                map.put("物资类别", applianceConfigResDTO.getApplianceType() == 1 ? "公共用品" : "个人用品");
                map.put("领用人", applianceConfigResDTO.getUserName());
                map.put("涉及相关作业类型", applianceConfigResDTO.getWorkType());
                map.put("接触职业病危害因素", applianceConfigResDTO.getHazardFactors());
                map.put("防护用品名称", applianceConfigResDTO.getApplianceName());
                map.put("规格型号", applianceConfigResDTO.getApplianceCode());
                map.put("领取数量", applianceConfigResDTO.getNum().toString());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                map.put("领取时间", sdf.format(applianceConfigResDTO.getReceivingTime()));
                map.put("有效期", sdf.format(applianceConfigResDTO.getEffectiveTime()));
                map.put("生产厂家", applianceConfigResDTO.getManufacturer());
                map.put("合格证", applianceConfigResDTO.getCertificate());
                map.put("更换原因", applianceConfigResDTO.getChangeReason());
                map.put("备注", applianceConfigResDTO.getRemark());
                map.put("是否已确认", applianceConfigResDTO.getIsConfirm() == 0 ? "否" : "是");
                map.put("状态", applianceConfigResDTO.getStatus() == 0 ? "未到期" : (applianceConfigResDTO.getStatus() == 1 ? "已到期" : "已更换"));
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("劳保用品配备信息", listName, list, null, response);
    }

    @Override
    public Page<ApplianceConfigResDTO> listApplianceConfig(PageReqDTO pageReqDTO, String name, Integer status, String startTime, String endTime) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return applianceMapper.pageApplianceConfig(pageReqDTO.of(), name, status, startTime, endTime);
    }

    @Override
    public Page<ApplianceConfigResDTO> vxListApplianceConfig(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return applianceMapper.vxListApplianceConfig(pageReqDTO.of(), TokenUtil.getCurrentPersonNo());
    }

    @Override
    public void vxConfirmApplianceConfig(String id) {
        Integer result = applianceMapper.vxConfirmApplianceConfig(id, TokenUtil.getCurrentPersonNo());
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public ApplianceConfigResDTO getApplianceConfigDetail(String id) {
        return applianceMapper.getApplianceConfigDetail(id);
    }

    @Override
    public void deleteApplianceConfig(String id) {
        applianceMapper.deleteApplianceConfig(id, TokenUtil.getCurrentPersonNo());
    }

    @Override
    public void changeAppliance(ApplianceConfigReqDTO applianceConfigReqDTO) {
        if (Objects.isNull(applianceConfigReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        ApplianceConfigResDTO applianceConfigResDTO = applianceMapper.getApplianceConfigDetail(applianceConfigReqDTO.getId());
        if (!Objects.isNull(applianceConfigResDTO)) {
            applianceConfigResDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
            applianceConfigResDTO.setReceivingTime(applianceConfigReqDTO.getReceivingTime());
            applianceConfigResDTO.setEffectiveTime(applianceConfigReqDTO.getEffectiveTime());
            applianceConfigResDTO.setChangeReason(applianceConfigReqDTO.getChangeReason());
            applianceConfigResDTO.setNum(applianceConfigReqDTO.getNum());
            Integer result = applianceMapper.changeAppliance(applianceConfigResDTO);
            if (result < 0) {
                throw new CommonException(ErrorCode.UPDATE_ERROR);
            } else if (applianceConfigResDTO.getUserId() != null && !"".equals(applianceConfigResDTO.getUserId())) {
                VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
                vxSendTextMsgReqDTO.setTouser(applianceConfigResDTO.getUserId());
                vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新的劳保用品配备，请前往小程序确认。"));
                msgService.sendTextMsg(vxSendTextMsgReqDTO);
            }
        }
    }

    @Override
    public Page<ApplianceWarnResDTO> listApplianceWarn(String deptId, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return applianceMapper.listApplianceWarn(pageReqDTO.of(), deptId);
    }

    @Override
    public void handleApplianceWarn(List<String> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = applianceMapper.modifyApplianceWarn(ids);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public List<ApplianceConfigResDTO> userArchives(String id) {
        return applianceMapper.userArchives(id);
    }

}
