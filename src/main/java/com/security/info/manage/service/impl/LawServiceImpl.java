package com.security.info.manage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.security.info.manage.config.MinioConfig;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.LawCatalogReqDTO;
import com.security.info.manage.dto.req.LawCatalogUserRoleReqDTO;
import com.security.info.manage.dto.req.LawReqDTO;
import com.security.info.manage.dto.res.LawCatalogResDTO;
import com.security.info.manage.dto.res.LawResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.FileMapper;
import com.security.info.manage.mapper.LawMapper;
import com.security.info.manage.service.LawService;
import com.security.info.manage.utils.DocUtils;
import com.security.info.manage.utils.MinioUtils;
import com.security.info.manage.utils.TokenUtil;
import com.security.info.manage.utils.treeTool.LawCatalogTreeToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class LawServiceImpl implements LawService {

    public static final String DOC = ".doc";
    public static final String DOCX = ".docx";
    public static final String LAW = "law";
    public static final String LAW_PATH = "/law";
    @Autowired
    private LawMapper lawMapper;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioUtils minioUtils;

    @Override
    public Page<LawCatalogResDTO> listLawCatalog(PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<LawCatalogResDTO> page = lawMapper.listLawCatalog(pageReqDTO.of());
        List<LawCatalogResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
            for (LawCatalogResDTO res : list) {
                res.setRouteName(res.getRouteName().replace("root", "").replaceAll(",", "/") + "/" + res.getName());
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public List<LawCatalogResDTO> listAllLawCatalog(String deptId) {
        List<LawCatalogResDTO> extraRootList = lawMapper.getRoot(deptId);
        if (Objects.isNull(extraRootList)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        List<LawCatalogResDTO> extraBodyList = lawMapper.getBody(deptId);
        LawCatalogTreeToolUtils extraTree = new LawCatalogTreeToolUtils(extraRootList, extraBodyList);
        return extraTree.getTree();
    }

    @Override
    public List<LawCatalogResDTO> vxListAllLawCatalog() {
        List<LawCatalogResDTO> extraRootList = lawMapper.getVxRoot(TokenUtil.getCurrentPersonNo());
        if (Objects.isNull(extraRootList)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        List<LawCatalogResDTO> extraBodyList = lawMapper.getVxBody(TokenUtil.getCurrentPersonNo());
        LawCatalogTreeToolUtils extraTree = new LawCatalogTreeToolUtils(extraRootList, extraBodyList);
        return extraTree.getTree();
    }

    @Override
    public void modifyLawCatalog(LawCatalogReqDTO lawCatalogReqDTO) {
        if (Objects.isNull(lawCatalogReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        if (lawCatalogReqDTO.getStatus() == 1) {
            Integer result = lawMapper.selectIfLawCatalogHadChild(lawCatalogReqDTO.getRoute());
            if (result > 0) {
                throw new CommonException(ErrorCode.CANT_UPDATE_HAD_CHILD);
            }
        }
        lawCatalogReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        Integer result = lawMapper.modifyLawCatalog(lawCatalogReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public void addLawCatalog(LawCatalogReqDTO lawCatalogReqDTO) {
        if (Objects.isNull(lawCatalogReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        lawCatalogReqDTO.setId(TokenUtil.getUuId());
        lawCatalogReqDTO.setUserId(TokenUtil.getCurrentPersonNo());
        Integer result = lawMapper.addLawCatalog(lawCatalogReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public void addLawCatalogRole(LawCatalogUserRoleReqDTO lawCatalogUserRoleReqDTO) {
        if (Objects.isNull(lawCatalogUserRoleReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        Integer result = lawMapper.addLawCatalogRole(lawCatalogUserRoleReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.INSERT_ERROR);
        }
    }

    @Override
    public Page<LawResDTO> listLaw(String catalogId, String name, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return lawMapper.listLaw(pageReqDTO.of(), catalogId, name);
    }

    @Override
    public void addLaw(LawReqDTO lawReqDTO) {
        if (Objects.isNull(lawReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        if (lawReqDTO.getFileIds().size() > 0) {
            lawReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
            Integer result = lawMapper.addLaw(lawReqDTO);
            if (result < 0) {
                throw new CommonException(ErrorCode.FILE_UPLOAD_FAIL);
            }
        }
    }

    @Override
    public void deleteLaw(LawReqDTO lawReqDTO) {
        if (Objects.isNull(lawReqDTO)) {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
        lawReqDTO.setCreateBy(TokenUtil.getCurrentPersonNo());
        Integer result = lawMapper.deleteLaw(lawReqDTO);
        if (result < 0) {
            throw new CommonException(ErrorCode.DELETE_ERROR);
        }
        minioUtils.removeObject(LAW, lawReqDTO.getFileUrl().replaceFirst(minioConfig.getUrl() + LAW_PATH, ""));
    }

    @Override
    public String previewLaw(String url) {
        try {
            if (url.contains(DOCX)) {
                String docxHtml = DocUtils.docx2Html(url, "");
                docxHtml = DocUtils.formatHtml(docxHtml);
                return docxHtml.replace("___", "22");
            }
            if (url.contains(DOC)) {
                String docHtml = DocUtils.doc2Html(url, "");
                return DocUtils.formatHtml(docHtml);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.PREVIEW_ERROR);
        }
        throw new CommonException(ErrorCode.PREVIEW_ERROR);
    }
}
