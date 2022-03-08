package com.security.info.manage.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.security.info.manage.dto.res.DeptTreeResDTO;
import com.security.info.manage.dto.VxAccessToken;
import com.security.info.manage.dto.res.VxDeptResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.OrganizationMapper;
import com.security.info.manage.service.OrganizationService;
import com.security.info.manage.utils.CompanyStructureTreeToolUtils;
import com.security.info.manage.utils.Constants;
import com.security.info.manage.utils.TokenUtil;
import com.security.info.manage.utils.VxApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationMapper organizationMapper;

    @Value("${vx-business.corpid}")
    private String corpid;

    @Value("${vx-business.corpsecret}")
    private String corpsecret;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void syncOrg(String orgId) {
        VxAccessToken accessToken = VxApiUtils.getAccessToken(corpid, corpsecret);
        if (accessToken == null) {
            throw new CommonException(ErrorCode.VX_ERROR, "accessToken返回为空!");
        }
        String url = Constants.VX_GET_ORG_LIST + "?access_token=" + accessToken.getToken();
        if (orgId != null) {
            url = url + "&id=" + orgId;
        }
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url)
                .build()
                .expand()
                .encode();
        URI uri = uriComponents.toUri();
        JSONObject res = restTemplate.getForEntity(uri, JSONObject.class).getBody();
        if (!Constants.SUCCESS.equals(Objects.requireNonNull(res).getString(Constants.ERR_CODE))) {
            throw new CommonException(ErrorCode.VX_ERROR, String.valueOf(res.get(Constants.ERR_MSG)));
        }
        if (res.getJSONArray("department") == null) {
            return;
        }
        List<VxDeptResDTO> list = JSONArray.parseArray(res.getJSONArray("department").toJSONString(), VxDeptResDTO.class);
        if (list != null && !list.isEmpty()) {
            organizationMapper.syncOrg(list, TokenUtil.getCurrentPersonNo());
        }
    }

    @Override
    public List<DeptTreeResDTO> listTree() {
        List<DeptTreeResDTO> extraRootList = organizationMapper.getRoot();
        if (Objects.isNull(extraRootList)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        List<DeptTreeResDTO> extraBodyList = organizationMapper.getBody();
        CompanyStructureTreeToolUtils extraTree = new CompanyStructureTreeToolUtils(extraRootList, extraBodyList);
        return extraTree.getTree();
    }

    @Override
    public List<DeptTreeResDTO> listFirst() {
        return organizationMapper.listCompanyList();
    }
}
