package com.security.info.manage.utils.task;

import com.google.common.base.Joiner;
import com.security.info.manage.dto.req.VxSendTextMsgReqDTO;
import com.security.info.manage.dto.res.ApplianceConfigResDTO;
import com.security.info.manage.dto.res.PhysicalUserResDTO;
import com.security.info.manage.entity.PhysicalResult;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.ApplianceMapper;
import com.security.info.manage.mapper.PhysicalMapper;
import com.security.info.manage.mapper.SafeExpectMapper;
import com.security.info.manage.service.DeptService;
import com.security.info.manage.service.MsgService;
import com.security.info.manage.service.UserService;
import com.security.info.manage.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author frp
 */
@Component
@Slf4j
public class DataChangeTask {

    @Resource
    private ApplianceMapper applianceMapper;

    @Resource
    private PhysicalMapper physicalMapper;

    @Resource
    private SafeExpectMapper safeExpectMapper;

    @Resource
    private MsgService msgService;

    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    public void applianceData() {
        applianceMapper.taskStatus();
        List<ApplianceConfigResDTO> list = applianceMapper.listExpiredAppliance();
        if (list != null && !list.isEmpty()) {
            for (ApplianceConfigResDTO applianceConfigResDTO : list) {
                applianceConfigResDTO.setUserName(applianceConfigResDTO.getUserName() + "的劳保用品即将到期预警");
            }
            if (list.size() > 0) {
                applianceMapper.addApplianceWarning(list, TokenUtil.getCurrentPersonNo());
            }
        }
    }

    @Scheduled(cron = "0 0 7 * * ?")
    @Async
    public void physicalUserData() {
        List<PhysicalUserResDTO> list = physicalMapper.listExpiredPhysicalUser();
        if (list != null && !list.isEmpty()) {
            for (PhysicalUserResDTO physicalUserResDTO : list) {
                physicalUserResDTO.setUserName(physicalUserResDTO.getUserName() + "未参加体检");
            }
            physicalMapper.addPhysicalWarning(list);
            VxSendTextMsgReqDTO vxSendTextMsgReqDTO = new VxSendTextMsgReqDTO();
            List<String> userIds = list.stream().map(PhysicalUserResDTO::getUserId).collect(Collectors.toList());
            if (!userIds.isEmpty()) {
                vxSendTextMsgReqDTO.setTouser(Joiner.on("|").join(userIds));
                vxSendTextMsgReqDTO.setText(new VxSendTextMsgReqDTO.Content("您有一次体检未参加，请前往小程序查看。"));
                msgService.sendTextMsg(vxSendTextMsgReqDTO);
            }
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    @Async
    public void modifySafeExpectStatus() {
        safeExpectMapper.modifySafeExpectStatus();
    }
}
