package com.security.info.manage.controller;

import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.res.Ztt4GEquipmentResDTO;
import com.security.info.manage.dto.res.Ztt4GPlaybackResDTO;
import com.security.info.manage.dto.res.Ztt4GTalkResDTO;
import com.security.info.manage.dto.res.Ztt4GVideoResDTO;
import com.security.info.manage.service.UserService;
import com.security.info.manage.service.Ztt4gService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/4g")
@Api(tags = "4G监控管理")
@Validated
public class Ztt4gController {

    @Resource
    private Ztt4gService ztt4gService;

    @GetMapping("/getToken")
    @ApiOperation(value = "获取token")
    public DataResponse<Map<String, Object>> getToken() {
        return DataResponse.of(ztt4gService.getToken());
    }

    @GetMapping("/listEquipment")
    @ApiOperation(value = "获取设备列表")
    public DataResponse<List<Ztt4GEquipmentResDTO>> listEquipment() {
        return DataResponse.of(ztt4gService.listEquipment());
    }

    @GetMapping("/listVideo")
    @ApiOperation(value = "获取单个设备视频列表")
    public DataResponse<List<Ztt4GVideoResDTO>> listVideo(@RequestParam String puid) {
        return DataResponse.of(ztt4gService.listVideo(puid));
    }

    @GetMapping("/liveUrl")
    @ApiOperation(value = "获取视频链接")
    public DataResponse<Map<String, Object>> getVideoUrl(@RequestParam String puid,
                                                         @RequestParam(required = false, defaultValue = "0") String idx,
                                                         @RequestParam(required = false, defaultValue = "IV") String resType,
                                                         @RequestParam(required = false, defaultValue = "0") String stream) {
        return DataResponse.of(ztt4gService.getVideoUrl(puid, idx, resType, stream));
    }

    @GetMapping("/playback")
    @ApiOperation(value = "查询云录像云抓拍")
    public DataResponse<List<Ztt4GPlaybackResDTO>> getPlaybackUrl(@RequestParam String puid,
                                                                  @RequestParam(required = false, defaultValue = "0") String idx,
                                                                  @RequestParam(required = false, defaultValue = "0") String type,
                                                                  @RequestParam(required = false, defaultValue = "0") String offset,
                                                                  @RequestParam(required = false, defaultValue = "200") String count,
                                                                  @RequestParam(required = false) String domainRoadId,
                                                                  @RequestParam String begin,
                                                                  @RequestParam String end,
                                                                  @RequestParam(required = false, defaultValue = "IV") String resType,
                                                                  @RequestParam(required = false, defaultValue = "0") String stream) {
        return DataResponse.of(ztt4gService.getPlaybackUrl(puid, idx, type, offset, count, domainRoadId, begin, end, resType, stream));
    }

    @GetMapping("/startTalk")
    @ApiOperation(value = "对讲,喊话 - 开启语音对讲(推模式)")
    public DataResponse<Ztt4GTalkResDTO> startTalk(@RequestParam String puid,
                                                   @RequestParam(required = false, defaultValue = "0") String idx) {
        return DataResponse.of(ztt4gService.startTalk(puid, idx));
    }

    @GetMapping("/stopTalk")
    @ApiOperation(value = "对讲,喊话 - 停止语音对讲")
    public DataResponse<Ztt4GTalkResDTO> stopTalk(@RequestParam String puid,
                                                  @RequestParam(required = false, defaultValue = "0") String idx) {
        return DataResponse.of(ztt4gService.stopTalk(puid, idx));
    }
}
