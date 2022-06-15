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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    @Value("${vx-business.appid}")
    private Integer appid;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void sendTextMsg(VxSendTextMsgReqDTO vxSendTextMsgReqDTO) {
        VxAccessToken accessToken = VxApiUtils.getAccessToken(corpid, appcorpsecret);
        if (accessToken == null) {
            throw new CommonException(ErrorCode.VX_ERROR, "accessToken返回为空!");
        }
        String url = Constants.VX_MESSAGE_SEND + "?access_token=" + accessToken.getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
        headers.add("charset","UTF-8");
        vxSendTextMsgReqDTO.setMsgtype("text");
        vxSendTextMsgReqDTO.setAgentid(appid);
        HttpEntity<String> strEntity = new HttpEntity<>(JSONObject.toJSONString(vxSendTextMsgReqDTO), headers);
        JSONObject res = restTemplate.postForObject(url, strEntity, JSONObject.class);
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
        headers.add("charset","UTF-8");
        vxSendMiniProgramMsgReqDTO.setMsgtype("miniprogram_notice");
        HttpEntity<String> strEntity = new HttpEntity<>(JSONObject.toJSONString(vxSendMiniProgramMsgReqDTO), headers);
        JSONObject res = restTemplate.postForObject(url, strEntity, JSONObject.class);
        if (!Constants.SUCCESS.equals(Objects.requireNonNull(res).getString(Constants.ERR_CODE))) {
            throw new CommonException(ErrorCode.VX_ERROR, String.valueOf(res.get(Constants.ERR_MSG)));
        }

    }

}
