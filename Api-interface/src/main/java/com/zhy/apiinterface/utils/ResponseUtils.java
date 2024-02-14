package com.zhy.apiinterface.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.apiclientsdk.exception.ApiException;
import com.zhy.apiclientsdk.model.response.ResultResponse;
import com.zhy.apiinterface.exception.BusinessException;
import com.zhy.apiinterface.exception.ErrorCode;

import java.util.Map;

public class ResponseUtils {

    public static Map<String, Object> responseToMap(String response) {
        return new Gson().fromJson(response, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    public static <T> ResultResponse baseResponse(String baseUrl, T params) {
        String response = null;
        try {
            response = RequestUtils.get(baseUrl, params);
            Map<String, Object> fromResponse = responseToMap(response);
            boolean success = (boolean) fromResponse.get("success");
            ResultResponse baseResponse = new ResultResponse();
            if (!success) {
                baseResponse.setData(fromResponse);
                return baseResponse;
            }
            fromResponse.remove("success");
            baseResponse.setData(fromResponse);
            return baseResponse;
        } catch (ApiException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
    }
}
