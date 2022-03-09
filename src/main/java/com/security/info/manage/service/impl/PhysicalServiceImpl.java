package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.NewUserReqDTO;
import com.security.info.manage.dto.req.PhysicalReqDTO;
import com.security.info.manage.dto.res.PhysicalResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.PhysicalMapper;
import com.security.info.manage.service.PhysicalService;
import com.security.info.manage.utils.FileUtils;
import com.security.info.manage.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class PhysicalServiceImpl implements PhysicalService {

    @Autowired
    private PhysicalMapper physicalMapper;

    @Override
    public Page<PhysicalResDTO> listPhysical(String sStartTime, String sEndTime, String eStartTime, String eEndTime, Integer type, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return physicalMapper.listPhysical(pageReqDTO.of(), sStartTime, sEndTime, eStartTime, eEndTime, type);
    }

    /**
     * todo 体检流程船舰修改时，新消息通知
      */
    @Override
    public void addPhysical(PhysicalReqDTO physicalReqDTO) {
        if (Objects.isNull(physicalReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        physicalReqDTO.setId(TokenUtil.getUuId());
        // todo 流水号生成
        physicalReqDTO.setNo(TokenUtil.getUuId());
        physicalReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = physicalMapper.addPhysical(physicalReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
        result = physicalMapper.modifyPhysicalUser(physicalReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void modifyPhysical(PhysicalReqDTO physicalReqDTO) {
        if (Objects.isNull(physicalReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        physicalReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = physicalMapper.modifyPhysical(physicalReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
        result = physicalMapper.modifyPhysicalUser(physicalReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void deletePhysical(PhysicalReqDTO physicalReqDTO) {
        if (Objects.isNull(physicalReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        physicalReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = physicalMapper.deletePhysical(physicalReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<NewUserReqDTO> addNewUser(MultipartFile file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(FileUtils.transferToFile(file));
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            List<NewUserReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                NewUserReqDTO reqDTO = new NewUserReqDTO();
                cells.getCell(0).setCellType(CellType.STRING);
                reqDTO.setName(cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(CellType.STRING);
                reqDTO.setAge(Integer.valueOf(cells.getCell(1).getStringCellValue()));
                cells.getCell(2).setCellType(CellType.STRING);
                reqDTO.setSex(Integer.valueOf(cells.getCell(2).getStringCellValue()));
                cells.getCell(3).setCellType(CellType.STRING);
                reqDTO.setPhone(cells.getCell(3).getStringCellValue());
                reqDTO.setId(TokenUtil.getUuId());
                reqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
                temp.add(reqDTO);
            }
            fileInputStream.close();
            Integer result = physicalMapper.addNewUser(temp);
            if (result < 0) {
                throw new CommonException(ErrorCode.IMPORT_DATA_EXIST);
            }
            return temp;
        } catch (IOException e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }
}
