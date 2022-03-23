package com.security.info.manage.utils.task;

import com.security.info.manage.mapper.ApplianceMapper;
import com.security.info.manage.service.DeptService;
import com.security.info.manage.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author frp
 */
@Component
@Slf4j
public class DataChangeTask {

    @Resource
    private ApplianceMapper applianceMapper;

    @Scheduled(cron = "0 0 1 * * ?")
    @Async
    public void applianceData() {
        applianceMapper.taskStatus();
    }
}
