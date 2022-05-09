package com.security.info.manage.service;

import com.security.info.manage.dto.res.Ztt4GEquipmentResDTO;
import com.security.info.manage.dto.res.Ztt4GPlaybackResDTO;
import com.security.info.manage.dto.res.Ztt4GTalkResDTO;
import com.security.info.manage.dto.res.Ztt4GVideoResDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author frp
 */
public interface Ztt4gService {

    Map<String, Object> getToken();

    List<Ztt4GEquipmentResDTO> listEquipment();

    List<Ztt4GVideoResDTO> listVideo(String puid);

    Map<String, Object> getVideoUrl(String puid, String idx, String resType, String stream);

    List<Ztt4GPlaybackResDTO> getPlaybackUrl(String puid, String idx, String type, String offset, String count,
                                             String domainRoadId, String begin, String end, String resType, String stream);

    Ztt4GTalkResDTO startTalk(String puid, String idx);

    Ztt4GTalkResDTO stopTalk(String puid, String idx);

}
