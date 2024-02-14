package com.zhy.apiclientsdk.service;

import com.zhy.apiclientsdk.client.ApiClient;
import com.zhy.apiclientsdk.exception.ApiException;
import com.zhy.apiclientsdk.model.request.BaseRequest;
import com.zhy.apiclientsdk.model.response.ResultResponse;

public interface ApiService {


    <O, T extends ResultResponse> T request(BaseRequest<O, T> request) throws ApiException;

    <O, T extends ResultResponse> T request(ApiClient apiClient, BaseRequest<O, T> request) throws ApiException;

}
