package com.security.info.manage.config.aspect;

import com.alibaba.fastjson.JSONObject;
import com.security.info.manage.annotation.LogMaker;
import com.security.info.manage.dto.DataResponse;
import com.security.info.manage.dto.res.OperationLogResDTO;
import com.security.info.manage.mapper.SysMapper;
import com.security.info.manage.utils.IpUtils;
import com.security.info.manage.utils.TokenUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Aspect
@Component
public class LogMakerAspect {

    @Resource
    private SysMapper sysMapper;

    /**
     * 注解Pointcut切入点
     * 定义出一个或一组方法，当执行这些方法时可产生通知
     * 指向你的切面类方法
     * 由于这里使用了自定义注解所以指向你的自定义注解
     */
    @Pointcut("@annotation(com.security.info.manage.annotation.LogMaker)")
    public void logPointCut() {
    }

    /**
     * 使用环绕通知
     */
    @AfterReturning(value = "logPointCut()", returning = "result")
    public void afterReturning(JoinPoint point, DataResponse result) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行时长(毫秒)
        Long time = System.currentTimeMillis() - beginTime;
        //异步保存日志
        saveLog(point, time, result);
    }

    void saveLog(JoinPoint joinPoint, Long time, DataResponse result) {
        final List<Object> params = new ArrayList<>();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperationLogResDTO operationLog = new OperationLogResDTO();
        //获取request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        Object[] args = joinPoint.getArgs();
        for (Object object : args) {
            if (object instanceof HttpServletResponse) {
                continue;
            }
            if (object instanceof HttpServletRequest) {
                continue;
            }
            params.add(object);
        }
        //设置IP地址
        operationLog.setHostIp(IpUtils.getIpAddr(request));
        //获取方法上的自定义注解
        LogMaker syslog = method.getAnnotation(LogMaker.class);
        if (syslog != null) {
            // 注解上的描述
            operationLog.setOperationType(syslog.value());
            //请求的参数
            if (result.getCode().equals(0)){
                operationLog.setParams(syslog.value() + "成功");
            }else {
                operationLog.setParams(syslog.value() + "失败," + "参数=" + JSONObject.toJSONString(params));
            }
        }
        //用户名
        if (null != TokenUtil.getCurrentUserName()) {
            operationLog.setUserName(TokenUtil.getCurrentUserName());
        }
        //系统当前时间
        operationLog.setOperationTime(new Timestamp(System.currentTimeMillis()));
        //用时
        operationLog.setUseTime(time);
        //保存系统日志
        sysMapper.addOperationLog(operationLog);
    }

}
