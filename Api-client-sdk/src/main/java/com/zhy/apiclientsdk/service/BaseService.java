package com.zhy.apiclientsdk.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zhy.apiclientsdk.client.ApiClient;
import com.zhy.apiclientsdk.exception.ApiException;
import com.zhy.apiclientsdk.exception.ErrorCode;
import com.zhy.apiclientsdk.exception.ErrorResponse;
import com.zhy.apiclientsdk.model.request.BaseRequest;
import com.zhy.apiclientsdk.model.response.ResultResponse;
import com.zhy.apiclientsdk.utils.SignUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class BaseService implements ApiService{

    private ApiClient apiClient;

    public static final String GATEWAY_HOST = "http://localhost:8090";

    @Override
    public <O, T extends ResultResponse> T request(ApiClient apiClient, BaseRequest<O, T> request) throws ApiException {
        // 校验apiClient
        checkConfig(apiClient);
        return request(request);
    }

    @Override
    public <O, T extends ResultResponse> T request(BaseRequest<O, T> request) throws ApiException {
        try {
            return res(request);

        }
        catch (Exception e) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 获取响应数据
     * @param request
     * @return
     * @param <O>
     * @param <T>
     * @throws ApiException
     */
    public <O, T extends ResultResponse> T res(BaseRequest<O, T> request) throws ApiException {

        T rsp;
        // 获取响应类型
        try {
            Class<T> tClass = request.getResponseClass();
            rsp = tClass.newInstance();
        } catch (Exception e) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
        HttpResponse httpResponse = doRequest(request);
        String body = httpResponse.body();
        Map<String, Object> data = new HashMap<>();
        if (httpResponse.getStatus() != 200) {
            ErrorResponse errorResponse = JSONUtil.toBean(body, ErrorResponse.class);
            data.put("errorMessage", errorResponse.getMessage());
            data.put("code", errorResponse.getCode());
        } else {
            try {
                // 尝试解析为JSON对象
                data = new Gson().fromJson(body, new TypeToken<Map<String, Object>>() {
                }.getType());
            } catch (JsonSyntaxException e) {
                // 解析失败，将body作为普通字符串处理
                data.put("value", body);
            }
        }
        rsp.setData(data);
        return rsp;
    }


    /**
     * 检查用户配置
     * @param apiClient
     * @throws ApiException
     */
    public void checkConfig(ApiClient apiClient) throws ApiException {
        // 如果当前用户未配置sdk
        if (apiClient == null && this.getApiClient() == null) {
            throw new ApiException(ErrorCode.NO_AUTH_ERROR, "请先配置密钥AccessKey/SecretKey");
        }
        if (apiClient != null && !StringUtils.isAnyBlank(apiClient.getAccessKey(), apiClient.getSecretKey())) {
            this.setApiClient(apiClient);
        }
    }

    /**
     * 执行请求
     * @param request
     * @return
     * @param <O>
     * @param <T>
     * @throws ApiException
     */
    private <O, T extends ResultResponse> HttpResponse doRequest(BaseRequest<O, T> request) throws ApiException {
        try (HttpResponse httpResponse = getHttpRequestByRequestMethod(request).execute()) {
            return httpResponse;
        }
        catch (Exception e) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }

    }

    /**
     * 拼接GET请求
     * @param request
     * @param path
     * @return
     * @param <O>
     * @param <T>
     */
    private <O, T extends ResultResponse> String splicingGetRequest(BaseRequest<O, T> request, String path) {
        StringBuilder urlBuilder = new StringBuilder(GATEWAY_HOST);
        // urlBuilder最后是/结尾且path以/开头的情况下，去掉urlBuilder结尾的/
        if (urlBuilder.toString().endsWith("/") && path.startsWith("/")) {
            urlBuilder.setLength(urlBuilder.length() - 1);
        }
        urlBuilder.append(path);
        if (!request.getRequestParams().isEmpty()) {
            urlBuilder.append("?");
            for (Map.Entry<String, Object> entry : request.getRequestParams().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                urlBuilder.append(key).append("=").append(value).append("&");
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        log.info("GET请求路径：{}", urlBuilder);
        return urlBuilder.toString();
    }

    /**
     * 获取请求头
     * @param body
     * @param apiClient
     * @return
     */
    private Map<String, String> getHeaders(String body, ApiClient apiClient) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", apiClient.getAccessKey());
        String encodeBody = SecureUtil.md5(body);
        hashMap.put("body", encodeBody);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", SignUtils.genSign(encodeBody, apiClient.getSecretKey()));
        return hashMap;
    }

    /**
     * 通过请求方法获取http响应
     * @param request
     * @return
     * @param <O>
     * @param <T>
     * @throws ApiException
     */
    private <O, T extends ResultResponse> HttpRequest getHttpRequestByRequestMethod(BaseRequest<O, T> request) throws ApiException, UnsupportedEncodingException {
        if (ObjectUtils.isEmpty(request)) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, "请求参数错误");
        }
        // 获取请求路径
        String path = request.getPath().trim();
        // 获取请求方法
        String method = request.getMethod().trim().toUpperCase();

        if (ObjectUtils.isEmpty(method)) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, "请求方法不存在");
        }
        if (StringUtils.isBlank(path)) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, "请求路径不存在");
        }

        if (path.startsWith(GATEWAY_HOST)) {
            path = path.substring(GATEWAY_HOST.length());
        }
        log.info("请求方法：{}，请求路径：{}，请求参数：{}", method, path, request.getRequestParams());
        HttpRequest httpRequest;
        switch (method) {
            case "GET": {
                httpRequest = HttpRequest.get(splicingGetRequest(request, path));
                break;
            }
            case "POST": {
                httpRequest = HttpRequest.post(GATEWAY_HOST + path);
                break;
            }
            default: {
                throw new ApiException(ErrorCode.OPERATION_ERROR, "不支持该请求");
            }
        }
        return httpRequest.addHeaders(getHeaders(JSONUtil.toJsonStr(request), apiClient)).body(JSONUtil.toJsonStr(request.getRequestParams()));
    }




}
