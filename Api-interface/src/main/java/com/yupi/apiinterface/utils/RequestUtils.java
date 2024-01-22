package com.yupi.apiinterface.utils;

import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestUtils {

    public static String get(String url) {
        String body = HttpRequest.get(url).execute().body();
        log.info("【interface】：请求地址：{}，响应数据：{}", url, body);
        return body;
    }
}

