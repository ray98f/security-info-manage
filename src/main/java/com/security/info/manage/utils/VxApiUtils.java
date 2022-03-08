package com.security.info.manage.utils;

import com.alibaba.fastjson.JSONObject;
import com.security.info.manage.dto.VxAccessToken;

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
        JSONObject jsonObject = JSONObject.parseObject(HttpRequestUtil.sendGet(Constants.VX_GET_ACCESS_TOKEN + "?corpid=" + corpid + "&corpsecret=" + corpsecret, null));
        if (jsonObject != null) {
            try {
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInteger("expires_in"));
            } catch (Exception e) {
                accessToken = null;
            }
        }
        return accessToken;
    }
}

