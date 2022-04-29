package com.security.info.manage.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.security.info.manage.dto.res.DeptTreeResDTO;
import com.security.info.manage.dto.VxAccessToken;
import com.security.info.manage.dto.res.UserResDTO;
import com.security.info.manage.dto.res.VxDeptResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.DeptMapper;
import com.security.info.manage.service.DeptService;
import com.security.info.manage.utils.treeTool.DeptTreeToolUtils;
import com.security.info.manage.utils.Constants;
import com.security.info.manage.utils.TokenUtil;
import com.security.info.manage.utils.VxApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Value("${vx-business.corpid}")
    private String corpid;

    @Value("${vx-business.corpsecret}")
    private String corpsecret;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncDept(String orgId) {
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
            for (VxDeptResDTO vxDeptResDTO : list) {
                vxDeptResDTO.setDepartment_leaders(String.join(",", vxDeptResDTO.getDepartment_leader()));
            }
            deptMapper.syncOrg(list, TokenUtil.getCurrentPersonNo());
        }
    }

    @Override
    public List<DeptTreeResDTO> listSyncDept() {
        return deptMapper.listSyncDept();
    }

    @Override
    public List<DeptTreeResDTO> listTree() {
        List<DeptTreeResDTO> extraRootList = deptMapper.getRoot();
        if (Objects.isNull(extraRootList)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        List<DeptTreeResDTO> extraBodyList = deptMapper.getBody();
        DeptTreeToolUtils extraTree = new DeptTreeToolUtils(extraRootList, extraBodyList);
        return extraTree.getTree();
    }

    @Override
    public List<DeptTreeResDTO> listFirst() {
        return deptMapper.listCompanyList();
    }

    @Override
    public List<UserResDTO> getDeptUser(String deptId, String dangerId) {
        DeptTreeResDTO res = deptMapper.selectParent(deptId);
        if (!Objects.isNull(res) && !"1".equals(res.getId())) {
            while (!"1".equals(res.getParentId())) {
                res = deptMapper.selectParent(res.getId());
                if (Objects.isNull(res)) {
                    break;
                }
            }
            return deptMapper.selectDepartmentUser(res.getId(), dangerId);
        } else if ("1".equals(res.getId())) {
            return deptMapper.selectDepartmentUser(deptId, dangerId);
        } else {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
    }
}
