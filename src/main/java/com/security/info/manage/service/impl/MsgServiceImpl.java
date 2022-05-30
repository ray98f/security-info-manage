package com.security.info.manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.security.info.manage.dto.VxAccessToken;
import com.security.info.manage.dto.req.VxSendMiniProgramMsgReqDTO;
import com.security.info.manage.dto.req.VxSendTextMsgReqDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.service.MsgService;
import com.security.info.manage.utils.Constants;
import com.security.info.manage.utils.VxApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class MsgServiceImpl implements MsgService {

    @Value("${vx-business.corpid}")
    private String corpid;

    @Value("${vx-business.appcorpsecret}")
    private String appcorpsecret;

    @Value("${vx-business.agentid}")
    private Integer agentid;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void sendTextMsg(VxSendTextMsgReqDTO vxSendTextMsgReqDTO) {
        VxAccessToken accessToken = VxApiUtils.getAccessToken(corpid, appcorpsecret);
        if (accessToken == null) {
            throw new CommonException(ErrorCode.VX_ERROR, "accessToken返回为空!");
        }
        String url = Constants.VX_MESSAGE_SEND + "?access_token=" + accessToken.getToken();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("touser", vxSendTextMsgReqDTO.getTouser());
//        jsonObject.put("toparty", vxSendTextMsgReqDTO.getToparty());
//        jsonObject.put("totag", vxSendTextMsgReqDTO.getTotag());
//        jsonObject.put("msgtype", vxSendTextMsgReqDTO.getMsgtype());
//        jsonObject.put("agentid", vxSendTextMsgReqDTO.getAgentid());
//        jsonObject.put("content", vxSendTextMsgReqDTO.getContent());
//        jsonObject.put("safe", vxSendTextMsgReqDTO.getSafe());
//        jsonObject.put("enable_id_trans", vxSendTextMsgReqDTO.getEnable_id_trans());
//        jsonObject.put("enable_duplicate_check", vxSendTextMsgReqDTO.getEnable_duplicate_check());
//        jsonObject.put("duplicate_check_interval", vxSendTextMsgReqDTO.getDuplicate_check_interval());
//        JSONObject res = restTemplate.postForEntity(url, jsonObject, JSONObject.class).getBody();
        vxSendTextMsgReqDTO.setMsgtype("text");
        vxSendTextMsgReqDTO.setAgentid(agentid);
        JSONObject res = restTemplate.postForEntity(url, JSONObject.toJSONString(vxSendTextMsgReqDTO), JSONObject.class).getBody();
        if (!Constants.SUCCESS.equals(Objects.requireNonNull(res).getString(Constants.ERR_CODE))) {
            throw new CommonException(ErrorCode.VX_ERROR, String.valueOf(res.get(Constants.ERR_MSG)));
        }
    }

    @Override
    public void sendMiniProgramMsg(VxSendMiniProgramMsgReqDTO vxSendMiniProgramMsgReqDTO) {
        VxAccessToken accessToken = VxApiUtils.getAccessToken(corpid, appcorpsecret);
        if (accessToken == null) {
            throw new CommonException(ErrorCode.VX_ERROR, "accessToken返回为空!");
        }
        String url = Constants.VX_MESSAGE_SEND + "?access_token=" + accessToken.getToken();
        vxSendMiniProgramMsgReqDTO.setMsgtype("miniprogram_notice");
        JSONObject res = restTemplate.postForEntity(url, JSONObject.toJSONString(vxSendMiniProgramMsgReqDTO), JSONObject.class).getBody();
        if (!Constants.SUCCESS.equals(Objects.requireNonNull(res).getString(Constants.ERR_CODE))) {
            throw new CommonException(ErrorCode.VX_ERROR, String.valueOf(res.get(Constants.ERR_MSG)));
        }

    }

}
