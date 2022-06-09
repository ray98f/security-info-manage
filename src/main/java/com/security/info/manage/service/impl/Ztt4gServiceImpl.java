package com.security.info.manage.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.security.info.manage.dto.res.Ztt4GEquipmentResDTO;
import com.security.info.manage.dto.res.Ztt4GPlaybackResDTO;
import com.security.info.manage.dto.res.Ztt4GTalkResDTO;
import com.security.info.manage.dto.res.Ztt4GVideoResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.service.Ztt4gService;
import com.security.info.manage.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author frp
 */
@Service
@Slf4j
public class Ztt4gServiceImpl implements Ztt4gService {

    public static final String MSG = "msg";
    public static final String OK = "OK";
    public static final String TOKEN = "token";
    @Value("${ztt-4g.url}")
    public String monitorUrl;

    @Value("${ztt-4g.port}")
    public String monitorPort;

    @Value("${ztt-4g.address}")
    public String monitorAddress;

    @Value("${ztt-4g.user}")
    public String monitorUser;

    @Value("${ztt-4g.password}")
    public String monitorPassword;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Map<String, Object> getToken() {
        Map<String, Object> data = new HashMap<>();
        data.put("token", login());
        return data;
    }

    @Override
    public List<Ztt4GEquipmentResDTO> listEquipment() {
        String url = "http://" + monitorUrl + ":" + monitorPort + Constants.ZTT_4G_LIST_EQUIPMENT + "?token=" + login();
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url)
                .build()
                .expand()
                .encode();
        URI uri = uriComponents.toUri();
        JSONArray res = restTemplate.getForEntity(uri, JSONArray.class).getBody();
        String str = Objects.requireNonNull(res).toJSONString().replaceAll("\\$", "puid");
        List<Ztt4GEquipmentResDTO> list = JSONArray.parseArray(str, Ztt4GEquipmentResDTO.class);
        list = list.stream().sorted(Comparator.comparing(Ztt4GEquipmentResDTO::getOnlineFlag).reversed().thenComparing(Ztt4GEquipmentResDTO::getPuid)).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<Ztt4GVideoResDTO> listVideo(String puid) {
        String url = "http://" + monitorUrl + ":" + monitorPort + Constants.ZTT_4G_LIST_VIDEO
                + "?token=" + login() + "&puid=" + puid;
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url)
                .build()
                .expand()
                .encode();
        URI uri = uriComponents.toUri();
        JSONObject res = restTemplate.getForEntity(uri, JSONObject.class).getBody();
        if (Objects.requireNonNull(res).getJSONArray("Res") == null) {
            return new ArrayList<>();
        }
        return JSONArray.parseArray(res.getJSONArray("Res").toJSONString(), Ztt4GVideoResDTO.class);
    }

    @Override
    public Map<String, Object> getVideoUrl(String puid, String idx, String resType, String stream) {
        Map<String, Object> data = new HashMap<>();
        data.put("url", "http://" + monitorUrl + ":" + monitorPort + Constants.ZTT_4G_LIVE_STREAM
                + "?puid=" + puid + "&idx=" + idx + "&resType=" + resType + "&token=" + login() + "&stream=" + stream);
        return data;
    }

    @Override
    public List<Ztt4GPlaybackResDTO> getPlaybackUrl(String puid, String idx, String type, String offset, String count,
                                                    String domainRoadId, String begin, String end, String resType, String stream) {
        String url = "http://" + monitorUrl + ":" + monitorPort + Constants.ZTT_4G_PLAYBACK_FILES
                + "?token=" + login() + "&puid=" + puid + "&idx=" + idx + "&type=" + type + "&offset=" + offset
                + "&count=" + count + "&domainRoadID=" + domainRoadId + "&begin=" + begin + "&end=" + end;
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url)
                .build()
                .expand()
                .encode();
        URI uri = uriComponents.toUri();
        JSONObject res = restTemplate.getForEntity(uri, JSONObject.class).getBody();
        if (Objects.requireNonNull(res).getJSONArray("File") == null) {
            return new ArrayList<>();
        }
        List<Ztt4GPlaybackResDTO> list = JSONArray.parseArray(res.getJSONArray("File").toJSONString(), Ztt4GPlaybackResDTO.class);
        if (list != null && !list.isEmpty()) {
            for (Ztt4GPlaybackResDTO resDTO : list) {
                resDTO.setUrl("http://" + monitorUrl + ":" + monitorPort + Constants.ZTT_4G_PLAYBACK_STREAM
                        + "?puid=" + resDTO.getPUID() + "&idx=" + resDTO.getResIdx()
                        + "&path=" + resDTO.getPath() + resDTO.getName() + "&token=" + login()
                        + "&stream=" + stream + "&resType=" + resType + "&id=" + resDTO.getID()
                        + "&start=0" + "&durationSecond=" + (Integer.parseInt(resDTO.getEnd()) - Integer.parseInt(resDTO.getBegin())));
            }
        }
        return list;
    }

    @Override
    public Ztt4GTalkResDTO startTalk(String puid, String idx) {
        String url = "http://" + monitorUrl + ":" + monitorPort + Constants.ZTT_4G_START_TALK
                + "?token=" + login() + "&puid=" + puid + "&idx=" + idx;
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url)
                .build()
                .expand()
                .encode();
        URI uri = uriComponents.toUri();
        JSONObject res = restTemplate.getForEntity(uri, JSONObject.class).getBody();
        if (Objects.requireNonNull(res).getString(Constants.ERR_CODE) != null) {
            throw new CommonException(ErrorCode.ZTT_4G_ERROR, "开启语音对讲失败" + res.getString(Constants.MSG));
        }
        return JSONObject.parseObject(res.toJSONString(), Ztt4GTalkResDTO.class);
    }

    @Override
    public Ztt4GTalkResDTO stopTalk(String puid, String idx) {
        String url = "http://" + monitorUrl + ":" + monitorPort + Constants.ZTT_4G_STOP_TALK
                + "?token=" + login() + "&puid=" + puid + "&idx=" + idx;
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url)
                .build()
                .expand()
                .encode();
        URI uri = uriComponents.toUri();
        JSONObject res = restTemplate.getForEntity(uri, JSONObject.class).getBody();
        if (Objects.requireNonNull(res).getString(Constants.ERR_CODE) != null) {
            throw new CommonException(ErrorCode.ZTT_4G_ERROR, "停止语音对讲失败" + res.getString(Constants.MSG));
        }
        return JSONObject.parseObject(res.toJSONString(), Ztt4GTalkResDTO.class);
    }

    public String login() {
        String url = "http://" + monitorUrl + ":" + monitorPort + Constants.ZTT_4G_LOGIN;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("address", monitorAddress);
        jsonObject.put("user", monitorUser);
        jsonObject.put("password", monitorPassword);
        JSONObject json = restTemplate.postForEntity(url, jsonObject, JSONObject.class).getBody();
        if (!OK.equals(Objects.requireNonNull(json).getString(MSG))) {
            throw new CommonException(ErrorCode.ZTT_4G_ERROR, String.valueOf(json.get(MSG)));
        }
        return json.getString(TOKEN);
    }

}
