package com.security.info.manage.utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.regex.Pattern;

/**
 * @author frp
 * 阿里云短信发送方法
 */
@Slf4j
@Component
public class SendUtils {

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    private static String keyId;
    private static String keySecret;
    public static final String OK = "OK";
    public static final int ELEVEN = 11;

    @PostConstruct
    public void init() {
        keyId = accessKeyId;
        keySecret = accessKeySecret;
    }

    public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }

    private static Integer checkTel(String mobile) {
        if (mobile == null || "".equals(mobile)) {
            return -1;
        }
        if (mobile.length() != ELEVEN) {
            return 0;
        }
        Pattern tel = Pattern.compile("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");
        if (!tel.matcher(mobile).matches()) {
            return 0;
        }
        return 1;
    }
}
