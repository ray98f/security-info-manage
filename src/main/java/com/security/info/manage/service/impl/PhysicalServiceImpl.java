package com.security.info.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import com.security.info.manage.config.MinioConfig;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.NewUserReqDTO;
import com.security.info.manage.dto.req.PhysicalReqDTO;
import com.security.info.manage.dto.req.PhysicalResultImportReqDTO;
import com.security.info.manage.dto.req.VxSendTextMsgReqDTO;
import com.security.info.manage.dto.res.*;
import com.security.info.manage.entity.File;
import com.security.info.manage.entity.PhysicalFeedback;
import com.security.info.manage.entity.PhysicalResult;
import com.security.info.manage.entity.User;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.PhysicalMapper;
import com.security.info.manage.mapper.UserMapper;
import com.security.info.manage.service.FileService;
import com.security.info.manage.service.MsgService;
import com.security.info.manage.service.PhysicalService;
import com.security.info.manage.utils.*;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
public class PhysicalServiceImpl implements PhysicalService {

    public static final String SERIAL_NUMBER_GZ_PHYSICAL = ":serial:num:physical:gz";
    public static final String SERIAL_NUMBER_RZ_PHYSICAL = ":serial:num:physical:rz";
    public static final String SERIAL_NUMBER_PT_PHYSICAL = ":serial:num:physical:pt";
    public static final String SERIAL_NUMBER_LG_PHYSICAL = ":serial:num:physical:lg";
    public static final String GZ_PHYSICAL_NO = "ZTT-GZ-";
    public static final String RZ_PHYSICAL_NO = "ZTT-RZ-";
    public static final String PT_PHYSICAL_NO = "ZTT-PT-";
    public static final String LG_PHYSICAL_NO = "ZTT-LG-";

    @Autowired
    private PhysicalMapper physicalMapper;

    @Autowired
    private MsgService msgService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient client;

    @Autowired
    private MinioUtils minioUtils;

    @Value("${pro.name}")
    private String proName;

    @Value("${spring.redis.key-prefix}")
    private String keyPrefix;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void exportPhysical(String sStartTime, String sEndTime, String eStartTime, String eEndTime, Integer type, HttpServletResponse response) {
        List<String> listName = Arrays.asList("序号", "流水号", "医院流水号", "体检类型", "体检起始时间", "应检人数", "受检人数", "检查结果");
        List<PhysicalResDTO> physicals = physicalMapper.listAllPhysical(sStartTime, sEndTime, eStartTime, eEndTime, type);
        ArrayList<Map<String, String>> list = new ArrayList<>();
        if (physicals != null && !physicals.isEmpty()) {
            for (PhysicalResDTO resDTO : physicals) {
                resDTO.setResult(physicalMapper.countPhysicalUser(resDTO.getId()));
                Map<String, String> map = new HashMap<>();
                map.put("序号", resDTO.getId());
                map.put("流水号", resDTO.getNo());
                map.put("医院流水号", resDTO.getHospitalNo());
                map.put("体检类型", resDTO.getType() == 1 ? "岗中体检" : resDTO.getType() == 2 ? "新人体检" : resDTO.getType() == 3 ? "普通体检" : "离岗体检");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                map.put("体检起始时间", sdf.format(resDTO.getStartTime()) + " 至 " + sdf.format(resDTO.getEndTime()));
                map.put("应检人数", resDTO.getEstimateNum().toString());
                map.put("受检人数", resDTO.getActualNum().toString());
                map.put("检查结果", "正常: " + resDTO.getResult().getNormalNum() + "人；复查: " + resDTO.getResult().getReviewNum() + "人；职业禁忌证: " + resDTO.getResult().getTabooNum() + "人");
                list.add(map);
            }
        }
        if (list.size() > 0) {
            ExcelPortUtil.excelPort("企业体检汇总档案", listName, list, null, response);
        }
    }

    @Override
    public Page<PhysicalResDTO> listPhysical(String sStartTime, String sEndTime, String eStartTime, String eEndTime, Integer type, Integer isBusiness, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<PhysicalResDTO> page = physicalMapper.listPhysical(pageReqDTO.of(), sStartTime, sEndTime, eStartTime, eEndTime, type, isBusiness);
        List<PhysicalResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
            for (PhysicalResDTO res : list) {
                res.setResult(physicalMapper.countPhysicalUser(res.getId()));
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public void addPhysical(PhysicalReqDTO physicalReqDTO) {
        if (Objects.isNull(physicalReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        physicalReqDTO.setId(TokenUtil.getUuId());
        //流水号生成
        String no;
        String typeName = "";
        if (physicalReqDTO.getType() == 1) {
            no = proName + keyPrefix + SERIAL_NUMBER_GZ_PHYSICAL;
            typeName = "岗中体检";
        } else if (physicalReqDTO.getType() == 2) {
            no = proName + keyPrefix + SERIAL_NUMBER_RZ_PHYSICAL;
        } else if (physicalReqDTO.getType() == 3) {
            no = proName + keyPrefix + SERIAL_NUMBER_PT_PHYSICAL;
            typeName = "普通体检";
        } else {
            no = proName + keyPrefix + SERIAL_NUMBER_LG_PHYSICAL;
            typeName = "离岗体检";
        }
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
        if (physicalReqDTO.getType() == 1) {
            physicalReqDTO.setNo(GZ_PHYSICAL_NO + today + "-" + str);
        } else if (physicalReqDTO.getType() == 2) {
            physicalReqDTO.setNo(RZ_PHYSICAL_NO + today + "-" + str);
        } else if (physicalReqDTO.getType() == 3) {
            physicalReqDTO.setNo(PT_PHYSICAL_NO + today + "-" + str);
        } else {
            physicalReqDTO.setNo(LG_PHYSICAL_NO + today + "-" + str);
        }
        stringRedisTemplate.opsForValue().set(no, String.valueOf(num + 1), 0);
        physicalReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = physicalMapper.addPhysical(physicalReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
        result = physicalMapper.modifyPhysicalUser(physicalReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
        if (physicalReqDTO.getType() != 2) {
            VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
            List<String> userIds = physicalReqDTO.getUsers().stream().map(User::getId).collect(Collectors.toList());
            if (!userIds.isEmpty()) {
                vxSendTextMsgReqDTO.setTouser(Joiner.on("|").join(userIds));
                vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条新" + typeName + "，请前往小程序查看处理。"));
                msgService.sendTextMsg(vxSendTextMsgReqDTO);
            }
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
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public PhysicalResDTO getPhysicalDetail(String id) {
        return physicalMapper.getPhysicalDetail(id);
    }

    @Override
    public void vxConfirmPhysicalUser(String id) {
        Integer result = physicalMapper.vxConfirmPhysicalUser(id, TokenUtil.getCurrentPersonNo());
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
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
        physicalUserResDTO.setPhysicalDetail(physicalMapper.getPhysicalDetail(physicalUserResDTO.getPhysicalId()));
        physicalUserResDTO.setIfFeedback(physicalMapper.ifPhysicalHadFeedback(id, physicalUserResDTO.getPhysicalId()));
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
        VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
        List<String> userIds = list.stream().map(PhysicalUserResDTO::getUserId).collect(Collectors.toList());
        if (!userIds.isEmpty()) {
            vxSendTextMsgReqDTO.setTouser(Joiner.on("|").join(userIds));
            vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一条体检发起复检，请前往小程序查看处理。"));
            msgService.sendTextMsg(vxSendTextMsgReqDTO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadWord(MultipartFile file, String bizCode, String id) throws Exception {
        File fileRes = uploadFile(file, bizCode);
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
            } catch (Exception e) {
                throw new CommonException(ErrorCode.FILE_UPLOAD_FAIL);
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
            result = physicalMapper.uploadFilePhysical(fileRes.getId(), id, TokenUtil.getCurrentPersonNo(), 1);
        } else if ("physical-user-word".equals(bizCode)) {
            physicalResultImportReqDTO.setId(physicalMapper.selectPhysicalIdByPhysicalUserId(id));
            physicalMapper.physicalResultImport(physicalResultImportReqDTO);
            result = physicalMapper.uploadFilePhysicalUser(fileRes.getId(), id, TokenUtil.getCurrentPersonNo(), 1);
        }
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        } else {
            if (physicalMapper.getPhysicalDetail(id).getType() != 3) {
                VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
                List<String> userIds = physicalMapper.selectUserIdByPhysicalUserId(physicalMapper.selectPhysicalUserWhenImportResult(physicalResultImportReqDTO));
                if (!userIds.isEmpty()) {
                    vxSendTextMsgReqDTO.setTouser(Joiner.on("|").join(userIds));
                    vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您的体检结果已录入，请前往小程序确认。"));
                    msgService.sendTextMsg(vxSendTextMsgReqDTO);
                }
            }
        }
    }

    @Override
    public void uploadPdf(MultipartFile file, String bizCode, String id) throws Exception {
        File fileRes = uploadFile(file, bizCode);
        Integer result = 0;
        if ("physical-pdf".equals(bizCode)) {
            result = physicalMapper.uploadFilePhysical(fileRes.getId(), id, TokenUtil.getCurrentPersonNo(), 2);
        } else if ("physical-user-pdf".equals(bizCode)) {
            result = physicalMapper.uploadFilePhysicalUser(fileRes.getId(), id, TokenUtil.getCurrentPersonNo(), 2);
        }
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public Page<PhysicalFeedbackResDTO> listFeedback(String name, String startTime, String endTime, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return physicalMapper.listFeedback(pageReqDTO.of(), name, startTime, endTime);
    }

    @Override
    public PhysicalFeedbackResDTO getFeedbackDetailByPhysicalId(String id, String physicalId) {
        return physicalMapper.getFeedbackDetailByPhysicalId(id, physicalId);
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
        if (id == null) {
            id = TokenUtil.getCurrentPersonNo();
        }
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

    @Override
    public Page<PhysicalUserResDTO> vxUserArchives(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<PhysicalUserResDTO> page = physicalMapper.selectVxPhysicalUserByUserId(pageReqDTO.of(), TokenUtil.getCurrentPersonNo());
        List<PhysicalUserResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
            for (PhysicalUserResDTO resDTO : list) {
                resDTO.setPhysicalResult(physicalMapper.getPhysicalResult(resDTO.getId()));
                resDTO.setPhysicalDetail(physicalMapper.getPhysicalDetail(resDTO.getPhysicalId()));
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public Page<PhysicalUserResDTO> vxMinePhysical(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<PhysicalUserResDTO> page = physicalMapper.selectVxMinePhysical(pageReqDTO.of(), TokenUtil.getCurrentPersonNo());
        List<PhysicalUserResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
            for (PhysicalUserResDTO resDTO : list) {
                resDTO.setPhysicalResult(physicalMapper.getPhysicalResult(resDTO.getId()));
                resDTO.setPhysicalDetail(physicalMapper.getPhysicalDetail(resDTO.getPhysicalId()));
                resDTO.setIfFeedback(physicalMapper.ifPhysicalHadFeedback(resDTO.getId(), resDTO.getPhysicalId()));
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public Page<PhysicalWarnResDTO> listPhysicalWarn(PageReqDTO pageReqDTO, Integer type) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return physicalMapper.listPhysicalWarn(pageReqDTO.of(), type);
    }

    @Override
    public void handlePhysicalWarn(String id) {
        if (Objects.isNull(id)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = physicalMapper.handlePhysicalWarn(id);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    public File uploadFile(MultipartFile file, String bizCode) throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {
        if (!minioUtils.bucketExists(bizCode)) {
            minioUtils.makeBucket(bizCode);
        }
        String oldName = file.getOriginalFilename();
        String fileName = FileUploadUtils.extractFilename(file);
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bizCode)
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        client.putObject(args);
        String url = minioConfig.getImgPath() + "/" + bizCode + "/" + fileName;
        return fileService.insertFile(url, bizCode, oldName);
    }
}
