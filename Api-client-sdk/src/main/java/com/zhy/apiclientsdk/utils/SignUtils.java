package com.zhy.apiclientsdk.utils;


import cn.hutool.crypto.digest.MD5;
import cn.hutool.json.JSONUtil;

public class SignUtils {

    /**
     * 生成签名
     * @param body
     * @param secretKey
     * @return
     */
    public static String genSign(String body, String secretKey) {

        return MD5.create().digestHex(JSONUtil.toJsonStr(body) + '.' + secretKey);
    }
}
