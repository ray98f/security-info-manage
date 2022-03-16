package com.security.info.manage.utils;

import com.alibaba.fastjson.JSONObject;
import com.security.info.manage.dto.VxAccessToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 企业微信
 */
public class VxApiUtils {

    /**
     * 获取凭证信息
     *
     * @param corpid
     * @param corpsecret
     * @return
     */
    public static VxAccessToken getAccessToken(String corpid, String corpsecret) {
        VxAccessToken accessToken = new VxAccessToken();
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet getToken = new HttpGet(Constants.VX_GET_ACCESS_TOKEN + "?corpid=" + corpid + "&corpsecret=" + corpsecret);
        HttpResponse response;
        JSONObject tokenJson = new JSONObject();
        try {
            response = client.execute(getToken);
            HttpEntity result = response.getEntity();
            String tokenMessage = EntityUtils.toString(result);
            tokenJson = JSONObject.parseObject(tokenMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (tokenJson != null) {
            try {
                accessToken.setToken(tokenJson.getString("access_token"));
                accessToken.setExpiresIn(tokenJson.getInteger("expires_in"));
            } catch (Exception e) {
                accessToken = null;
            }
        }
        return accessToken;
    }
}

