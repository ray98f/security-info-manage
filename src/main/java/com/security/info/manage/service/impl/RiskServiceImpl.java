package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.RiskInfoReqDTO;
import com.security.info.manage.dto.res.RiskInfoResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.RiskMapper;
import com.security.info.manage.service.RiskService;
import com.security.info.manage.utils.ExcelPortUtil;
import com.security.info.manage.utils.FileUtils;
import com.security.info.manage.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.util.*;

import static com.security.info.manage.utils.Constants.XLS;
import static com.security.info.manage.utils.Constants.XLSX;

/**
 * @author frp
 */
@Service
@Slf4j
public class RiskServiceImpl implements RiskService {

    @Autowired
    private RiskMapper riskMapper;

    private final Map<String, Integer> LEVEL = new HashMap<String, Integer>() {{
        put("重大", 1);
        put("较大", 2);
        put("一般", 3);
        put("较小", 4);
    }};

    @Override
    public Page<RiskInfoResDTO> listRisk(Integer level, Integer type, String module, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return riskMapper.listRisk(pageReqDTO.of(), level, type, module);
    }

    @Override
    public RiskInfoResDTO getRiskDetail(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        return riskMapper.getRiskDetail(id);
    }

    @Override
    public void modifyRisk(RiskInfoReqDTO riskInfoReqDTO) {
        if (Objects.isNull(riskInfoReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = riskMapper.selectRiskIsExist(riskInfoReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        riskInfoReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = riskMapper.modifyRisk(riskInfoReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void addRisk(RiskInfoReqDTO riskInfoReqDTO) {
        if (Objects.isNull(riskInfoReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = riskMapper.selectRiskIsExist(riskInfoReqDTO);
        if (result > 0) {
            throw new CommonException(ErrorCode.DATA_EXIST);
        }
        riskInfoReqDTO.setId(TokenUtil.getUuId());
        riskInfoReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        result = riskMapper.addRisk(riskInfoReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void deleteRisk(List<String> ids) {
        Integer result = riskMapper.deleteRisk(ids, TokenUtil.getCurrentPersonNo());
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importRisk(MultipartFile file, Integer type) {
        try {
            List<RiskInfoReqDTO> temp = new ArrayList<>();
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
            Sheet sheet = Objects.requireNonNull(workbook).getSheetAt(1);
            int rows = sheet.getLastRowNum();
            if (rows == 0) {
                throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
            }
            if (type == 1) {
                for (Row cells : sheet) {
                    if (cells.getRowNum() < 3) {
                        continue;
                    }
                    RiskInfoReqDTO reqDTO = new RiskInfoReqDTO();
                    cells.getCell(1).setCellType(1);
                    reqDTO.setModules(cells.getCell(1) == null ? null : cells.getCell(1).getStringCellValue());
                    cells.getCell(2).setCellType(1);
                    reqDTO.setRiskPoints(cells.getCell(2) == null ? null : cells.getCell(2).getStringCellValue());
                    cells.getCell(3).setCellType(1);
                    reqDTO.setMainRiskPoints(cells.getCell(3) == null ? null : cells.getCell(3).getStringCellValue());
                    cells.getCell(4).setCellType(1);
                    reqDTO.setDescribeResult(cells.getCell(4) == null ? null : cells.getCell(4).getStringCellValue());
                    cells.getCell(5).setCellType(1);
                    reqDTO.setAccidentType(cells.getCell(5) == null ? null : cells.getCell(5).getStringCellValue());
                    cells.getCell(6).setCellType(1);
                    reqDTO.setRiskEvaluationL(cells.getCell(6) == null ? null : cells.getCell(6).getStringCellValue());
                    cells.getCell(7).setCellType(1);
                    reqDTO.setRiskEvaluationC(cells.getCell(7) == null ? null : cells.getCell(7).getStringCellValue());
                    cells.getCell(8).setCellType(1);
                    reqDTO.setRiskEvaluationD(cells.getCell(8) == null ? null : cells.getCell(8).getStringCellValue());
                    cells.getCell(9).setCellType(1);
                    reqDTO.setLevel(cells.getCell(9) == null ? null : LEVEL.get(cells.getCell(9).getStringCellValue()));
                    cells.getCell(10).setCellType(1);
                    reqDTO.setTechnicalMeasures(cells.getCell(10) == null ? null : cells.getCell(10).getStringCellValue());
                    cells.getCell(11).setCellType(1);
                    reqDTO.setManageMeasures(cells.getCell(11) == null ? null : cells.getCell(11).getStringCellValue());
                    cells.getCell(12).setCellType(1);
                    reqDTO.setEducationMeasures(cells.getCell(12) == null ? null : cells.getCell(12).getStringCellValue());
                    cells.getCell(13).setCellType(1);
                    reqDTO.setIndividualProtection(cells.getCell(13) == null ? null : cells.getCell(13).getStringCellValue());
                    cells.getCell(14).setCellType(1);
                    reqDTO.setEmergencyMeasure(cells.getCell(14) == null ? null : cells.getCell(14).getStringCellValue());
                    cells.getCell(15).setCellType(1);
                    reqDTO.setBasis(cells.getCell(15) == null ? null : cells.getCell(15).getStringCellValue());
                    cells.getCell(16).setCellType(1);
                    reqDTO.setResponsibilityDept(cells.getCell(16) == null ? null : cells.getCell(16).getStringCellValue());
                    cells.getCell(17).setCellType(1);
                    reqDTO.setResponsibilityCenter(cells.getCell(17) == null ? null : cells.getCell(17).getStringCellValue());
                    cells.getCell(18).setCellType(1);
                    reqDTO.setResponsibilityPost(cells.getCell(18) == null ? null : cells.getCell(18).getStringCellValue());
                    cells.getCell(19).setCellType(1);
                    reqDTO.setResponsibilityUser(cells.getCell(19) == null ? null : cells.getCell(19).getStringCellValue());
                    reqDTO.setType(type);
                    reqDTO.setId(TokenUtil.getUuId());
                    reqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
                    temp.add(reqDTO);
                }
                fileInputStream.close();
            } else if (type == 2) {
                for (Row cells : sheet) {
                    if (cells.getRowNum() < 3) {
                        continue;
                    }
                    RiskInfoReqDTO reqDTO = new RiskInfoReqDTO();
                    cells.getCell(1).setCellType(1);
                    reqDTO.setModules(cells.getCell(1) == null ? null : cells.getCell(1).getStringCellValue());
                    cells.getCell(2).setCellType(1);
                    reqDTO.setRiskPoints(cells.getCell(2) == null ? null : cells.getCell(2).getStringCellValue());
                    cells.getCell(3).setCellType(1);
                    reqDTO.setMainRiskPoints(cells.getCell(3) == null ? null : cells.getCell(3).getStringCellValue());
                    cells.getCell(4).setCellType(1);
                    reqDTO.setDescribePersonFactor(cells.getCell(4) == null ? null : cells.getCell(4).getStringCellValue());
                    cells.getCell(5).setCellType(1);
                    reqDTO.setDescribeObjectFactor(cells.getCell(5) == null ? null : cells.getCell(5).getStringCellValue());
                    cells.getCell(6).setCellType(1);
                    reqDTO.setDescribeEnvironmentFactor(cells.getCell(6) == null ? null : cells.getCell(6).getStringCellValue());
                    cells.getCell(7).setCellType(1);
                    reqDTO.setDescribeManageFactor(cells.getCell(7) == null ? null : cells.getCell(7).getStringCellValue());
                    cells.getCell(8).setCellType(1);
                    reqDTO.setDescribeMayOccur(cells.getCell(8) == null ? null : cells.getCell(8).getStringCellValue());
                    cells.getCell(9).setCellType(1);
                    reqDTO.setDescribeOccupationalHazards(cells.getCell(9) == null ? null : cells.getCell(9).getStringCellValue());
                    cells.getCell(10).setCellType(1);
                    reqDTO.setRiskMatrixL(cells.getCell(10) == null ? null : cells.getCell(10).getStringCellValue());
                    cells.getCell(11).setCellType(1);
                    reqDTO.setRiskMatrixS(cells.getCell(11) == null ? null : cells.getCell(11).getStringCellValue());
                    cells.getCell(12).setCellType(1);
                    reqDTO.setRiskMatrixR(cells.getCell(12) == null ? null : cells.getCell(12).getStringCellValue());
                    cells.getCell(13).setCellType(1);
                    reqDTO.setLevel(cells.getCell(13) == null ? null : LEVEL.get(cells.getCell(13).getStringCellValue()));
                    cells.getCell(14).setCellType(1);
                    reqDTO.setTechnicalMeasures(cells.getCell(14) == null ? null : cells.getCell(14).getStringCellValue());
                    cells.getCell(15).setCellType(1);
                    reqDTO.setManageMeasures(cells.getCell(15) == null ? null : cells.getCell(15).getStringCellValue());
                    cells.getCell(16).setCellType(1);
                    reqDTO.setEducationMeasures(cells.getCell(16) == null ? null : cells.getCell(16).getStringCellValue());
                    cells.getCell(17).setCellType(1);
                    reqDTO.setIndividualProtection(cells.getCell(17) == null ? null : cells.getCell(17).getStringCellValue());
                    cells.getCell(18).setCellType(1);
                    reqDTO.setHealthProtectMeasures(cells.getCell(18) == null ? null : cells.getCell(18).getStringCellValue());
                    cells.getCell(19).setCellType(1);
                    reqDTO.setEmergencyMeasure(cells.getCell(19) == null ? null : cells.getCell(19).getStringCellValue());
                    cells.getCell(20).setCellType(1);
                    reqDTO.setBasis(cells.getCell(20) == null ? null : cells.getCell(20).getStringCellValue());
                    cells.getCell(21).setCellType(1);
                    reqDTO.setResponsibilityDept(cells.getCell(21) == null ? null : cells.getCell(21).getStringCellValue());
                    cells.getCell(22).setCellType(1);
                    reqDTO.setResponsibilityCenter(cells.getCell(22) == null ? null : cells.getCell(22).getStringCellValue());
                    cells.getCell(23).setCellType(1);
                    reqDTO.setResponsibilityPost(cells.getCell(23) == null ? null : cells.getCell(23).getStringCellValue());
                    cells.getCell(24).setCellType(1);
                    reqDTO.setResponsibilityUser(cells.getCell(24) == null ? null : cells.getCell(24).getStringCellValue());
                    reqDTO.setType(type);
                    reqDTO.setId(TokenUtil.getUuId());
                    reqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
                    temp.add(reqDTO);
                }
                fileInputStream.close();
            }
            if (temp.size() > 0) {
                riskMapper.importRisk(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportRisk(HttpServletResponse response, Integer type) {
        List<RiskInfoResDTO> resDTOList = riskMapper.exportRisk(type);
        List<String> listName = new ArrayList<>();
        List<Map<String, String>> list = new ArrayList<>();
        if (type == 1) {
            if (resDTOList != null && !resDTOList.isEmpty()) {
                for (RiskInfoResDTO resDTO : resDTOList) {
                    Map<String, String> map = new HashMap<>();
                    map.put("风险项类型", "1".equals(resDTO.getType()) ? "运营类" : "生产类");
                    map.put("所属专业模块", resDTO.getModules());
                    map.put("主要风险点", resDTO.getRiskPoints());
                    map.put("主风险点分项", resDTO.getMainRiskPoints());
                    map.put("风险描述-危险源及其可能造成的后果(运营类)", resDTO.getDescribeResult());
                    map.put("风险定量评价-L(运营类)", resDTO.getRiskEvaluationL());
                    map.put("风险定量评价-C(运营类)", resDTO.getRiskEvaluationC());
                    map.put("风险定量评价-D(运营类)", resDTO.getRiskEvaluationD());
                    map.put("风险等级", resDTO.getLevel() == 1 ? "重大风险" : resDTO.getLevel() == 2 ? "较大风险" : resDTO.getLevel() == 3 ? "一般风险" : "较小风险");
                    map.put("风险管控措施-技术措施", resDTO.getTechnicalMeasures());
                    map.put("风险管控措施-管理措施", resDTO.getManageMeasures());
                    map.put("风险管控措施-教育措施", resDTO.getEducationMeasures());
                    map.put("风险管控措施-个体防护", resDTO.getIndividualProtection());
                    map.put("风险管控措施-应急措施", resDTO.getEmergencyMeasure());
                    map.put("措施指定依据(关联法律、法规、规章、制度文档)", resDTO.getBasis());
                    map.put("责任部门", resDTO.getResponsibilityDept());
                    map.put("责任中心", resDTO.getResponsibilityCenter());
                    map.put("责任岗位", resDTO.getResponsibilityPost());
                    map.put("责任人", resDTO.getResponsibilityUser());
                    list.add(map);
                }
            }
            listName = Arrays.asList("风险项类型", "所属专业模块", "主要风险点", "主风险点分项", "风险描述-危险源及其可能造成的后果(运营类)",
                    "风险定量评价-L(运营类)", "风险定量评价-C(运营类)", "风险定量评价-D(运营类)", "风险等级", "风险管控措施-技术措施",
                    "风险管控措施-管理措施", "风险管控措施-教育措施", "风险管控措施-个体防护", "风险管控措施-应急措施",
                    "措施指定依据(关联法律、法规、规章、制度文档)", "责任部门", "责任中心", "责任岗位", "责任人");
        } else if (type == 2) {
            listName = Arrays.asList("年度", "工号", "姓名", "部门名称", "涉及相关作业类型", "接触职业病危害因素", "用品编号", "用品名称", "领取数量", "领取时间", "有效期", "更换原因", "备注", "是否已确认", "状态");
        }
        ExcelPortUtil.excelPort("风险分级管控数据库", listName, list, null, response);
    }

}
