package com.yupi.apiclientsdk.model.request;

import com.yupi.apiclientsdk.model.response.ResultResponse;

public class CurrencyRequest extends BaseRequest<Object, ResultResponse> {
    private String method;
    private String path;

    /**
     * get方法
     */
    @Override
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * 获取路径
     */
    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取响应类
     */
    @Override
    public Class<ResultResponse> getResponseClass() {
        return ResultResponse.class;
    }
}
