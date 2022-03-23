package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.config.MinioConfig;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.NewUserReqDTO;
import com.security.info.manage.dto.req.PhysicalReqDTO;
import com.security.info.manage.dto.req.PhysicalResultImportReqDTO;
import com.security.info.manage.dto.res.PhysicalFeedbackResDTO;
import com.security.info.manage.dto.res.PhysicalResDTO;
import com.security.info.manage.dto.res.PhysicalUserResDTO;
import com.security.info.manage.dto.res.UserArchivesResDTO;
import com.security.info.manage.entity.PhysicalFeedback;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.PhysicalMapper;
import com.security.info.manage.mapper.UserMapper;
import com.security.info.manage.service.PhysicalService;
import com.security.info.manage.utils.*;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author frp
 */
@Service
@Slf4j
public class PhysicalServiceImpl implements PhysicalService {

    @Autowired
    private PhysicalMapper physicalMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient client;

    @Autowired
    private MinioUtils minioUtils;

    @Override
    public Page<PhysicalResDTO> listPhysical(String sStartTime, String sEndTime, String eStartTime, String eEndTime, Integer type, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<PhysicalResDTO> page = physicalMapper.listPhysical(pageReqDTO.of(), sStartTime, sEndTime, eStartTime, eEndTime, type);
        List<PhysicalResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
            for (PhysicalResDTO res : list) {
                res.setResult(physicalMapper.countPhysicalUser(res.getId()));
            }
        }
        page.setRecords(list);
        return page;
    }

    /**
     * todo 体检流程创建修改时，新消息通知
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
                cells.getCell(0).setCellType(1);
                reqDTO.setName(cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(1);
                reqDTO.setAge(Integer.valueOf(cells.getCell(1).getStringCellValue()));
                cells.getCell(2).setCellType(1);
                reqDTO.setSex("".equals(cells.getCell(2).getStringCellValue()) ? 0 : ("男".equals(cells.getCell(2).getStringCellValue()) ? 1 : 2));
                cells.getCell(3).setCellType(1);
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

    @Override
    public PhysicalResDTO getPhysicalDetail(String id) {
        return physicalMapper.getPhysicalDetail(id);
    }

    @Override
    public Page<PhysicalUserResDTO> listPhysicalUser(String id, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return physicalMapper.listPhysicalUser(pageReqDTO.of(), id);
    }

    @Override
    public PhysicalUserResDTO getPhysicalUserDetail(String id) {
        PhysicalUserResDTO physicalUserResDTO = physicalMapper.selectPhysicalUserById(id);
        if (Objects.isNull(physicalUserResDTO)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        physicalUserResDTO.setPhysicalResult(physicalMapper.getPhysicalResult(id));
        return physicalUserResDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userReview(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        List<PhysicalUserResDTO> list = physicalMapper.selectPhysicalUserByPhysicalId(id);
        if (list == null || list.isEmpty()) {
            throw new CommonException(ErrorCode.USER_REVIEW_EXIST);
        }
        for (PhysicalUserResDTO physicalUserResDTO : list) {
            physicalUserResDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
            Integer result = physicalMapper.userReview(physicalUserResDTO);
            if (result < 0) {
                throw new CommonException(ErrorCode.UPDATE_ERROR);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadWord(MultipartFile file, String bizCode, String id) throws Exception {
        String url = uploadFile(file, bizCode);
        PhysicalResultImportReqDTO physicalResultImportReqDTO = new PhysicalResultImportReqDTO();
        physicalResultImportReqDTO.setId(id);
        physicalResultImportReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        FileInputStream fileInputStream = new FileInputStream(FileUtils.transferToFile(file));
        String buffer;
        String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if ("doc".equals(suffix)) {
            Document doc = Jsoup.parse(Objects.requireNonNull(FileUtils.docToHtml(fileInputStream)));
            Elements tables = doc.select("table");
            ReadWordTable readWordTable = new ReadWordTable();
            physicalResultImportReqDTO.setUsers(readWordTable.readTable(tables));
            buffer = HtmlUtil.removeHtmlTag(doc.toString());
        } else if ("docx".equals(suffix)) {
            XWPFDocument document = new XWPFDocument(Objects.requireNonNull(fileInputStream));
            physicalResultImportReqDTO.setHospitalNo(document.getHeaderFooterPolicy().getHeader(1).getListParagraph().get(2).getText().split(" ")[0]);
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            buffer = extractor.getText();
            ReadWordTable readWordTable = new ReadWordTable();
            try {
                List<XWPFTable> tables = document.getTables();
                physicalResultImportReqDTO.setUsers(readWordTable.readTable(tables));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new CommonException(ErrorCode.FILE_FORMAT_ERROR);
        }
        Pattern zj = Pattern.compile("应检人数：(.*?)人");
        Pattern sd = Pattern.compile("受检人数：(.*?)人");
        Pattern fw = Pattern.compile("批准日期：(.*?)日");
        Matcher matcher = zj.matcher(buffer);
        if (matcher.find()) {
            String result = matcher.group(1).trim();
            physicalResultImportReqDTO.setEstimateNum(Integer.valueOf(result));
        }
        matcher = sd.matcher(buffer);
        if (matcher.find()) {
            String result = matcher.group(1).trim();
            physicalResultImportReqDTO.setActualNum(Integer.valueOf(result));
        }
        matcher = fw.matcher(buffer);
        if (matcher.find()) {
            String result = matcher.group(1).trim();
            physicalResultImportReqDTO.setPhysicalTime(result.replace("年", "-").replace("月", "-"));
        }
        Integer result = 0;
        if ("physical-word".equals(bizCode)) {
            physicalMapper.editPhysical(physicalResultImportReqDTO);
            physicalMapper.physicalResultImport(physicalResultImportReqDTO);
            result = physicalMapper.uploadFilePhysical(url, id, TokenUtil.getCurrentPersonNo(), 1);
        } else if ("physical-user-word".equals(bizCode)) {
            physicalMapper.physicalResultImport(physicalResultImportReqDTO);
            result = physicalMapper.uploadFilePhysicalUser(url, id, TokenUtil.getCurrentPersonNo(), 1);
        }
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void uploadPdf(MultipartFile file, String bizCode, String id) throws Exception {
        String url = uploadFile(file, bizCode);
        Integer result = 0;
        if ("physical-pdf".equals(bizCode)) {
            result = physicalMapper.uploadFilePhysical(url, id, TokenUtil.getCurrentPersonNo(), 2);
        } else if ("physical-user-pdf".equals(bizCode)) {
            result = physicalMapper.uploadFilePhysicalUser(url, id, TokenUtil.getCurrentPersonNo(), 2);
        }
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public Page<PhysicalFeedbackResDTO> listFeedback(String name, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return physicalMapper.listFeedback(pageReqDTO.of(), name);
    }

    @Override
    public void addFeedback(PhysicalFeedback physicalFeedback) {
        if (Objects.isNull(physicalFeedback)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        physicalFeedback.setId(TokenUtil.getUuId());
        physicalFeedback.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = physicalMapper.insertPhysicalFeedback(physicalFeedback);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void modifyFeedback(PhysicalFeedback physicalFeedback) {
        if (Objects.isNull(physicalFeedback)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        physicalFeedback.setUpdateBy(TokenUtil.getCurrentPersonNo());
        Integer result = physicalMapper.modifyPhysicalFeedback(physicalFeedback);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public UserArchivesResDTO userArchives(String id) {
        UserArchivesResDTO userArchivesResDTO = new UserArchivesResDTO();
        userArchivesResDTO.setUserInfo(userMapper.selectUser(id));
        PhysicalUserResDTO physicalUserResDTO = physicalMapper.selectLatestPhysicalUserByUserId(id);
        if (physicalUserResDTO != null && physicalUserResDTO.getId() != null) {
            physicalUserResDTO.setPhysicalResult(physicalMapper.getPhysicalResult(physicalUserResDTO.getId()));
        }
        userArchivesResDTO.setLatestUserPhysical(physicalUserResDTO);
        List<PhysicalUserResDTO> list = physicalMapper.selectPhysicalUserByUserId(id);
        if (list != null && !list.isEmpty()) {
            for (PhysicalUserResDTO resDTO : list) {
                resDTO.setPhysicalResult(physicalMapper.getPhysicalResult(resDTO.getId()));
            }
        }
        userArchivesResDTO.setUserPhysicalList(list);
        return userArchivesResDTO;
    }

    public String uploadFile(MultipartFile file, String bizCode) throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {
        if (!minioUtils.bucketExists(bizCode)) {
            minioUtils.makeBucket(bizCode);
        }
        String fileName = FileUploadUtils.extractFilename(file);
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bizCode)
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        client.putObject(args);
        return minioConfig.getUrl() + "/" + bizCode + "/" + fileName;
    }
}
