package com.security.info.manage.utils.task;

import com.security.info.manage.dto.res.ApplianceConfigResDTO;
import com.security.info.manage.dto.res.PhysicalUserResDTO;
import com.security.info.manage.enums.ErrorCode;
import com.security.info.manage.exception.CommonException;
import com.security.info.manage.mapper.ApplianceMapper;
import com.security.info.manage.mapper.PhysicalMapper;
import com.security.info.manage.service.DeptService;
import com.security.info.manage.service.UserService;
import com.security.info.manage.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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

    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    public void applianceData() {
        applianceMapper.taskStatus();
        List<ApplianceConfigResDTO> list = applianceMapper.listExpiredAppliance();
        if (list != null && !list.isEmpty()) {
            for (ApplianceConfigResDTO applianceConfigResDTO : list) {
                applianceConfigResDTO.setUserName(applianceConfigResDTO.getUserName() + "的劳保用品即将到期预警");
            }
        }
        applianceMapper.addApplianceWarning(list, TokenUtil.getCurrentPersonNo());
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    public void physicalUserData() {
        List<PhysicalUserResDTO> list = physicalMapper.listExpiredPhysicalUser();
        if (list != null && !list.isEmpty()) {
            for (PhysicalUserResDTO physicalUserResDTO : list) {
                physicalUserResDTO.setUserName(physicalUserResDTO.getUserName() + "未参加体检");
            }
            physicalMapper.addPhysicalWarning(list);
        }
    }
}
