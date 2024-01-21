package com.yupi.apiclientsdk.service;

import com.yupi.apiclientsdk.client.ApiClient;
import com.yupi.apiclientsdk.exception.ApiException;
import com.yupi.apiclientsdk.model.request.BaseRequest;
import com.yupi.apiclientsdk.model.response.ResultResponse;

public interface ApiService {


    <O, T extends ResultResponse> T request(BaseRequest<O, T> request) throws ApiException;

    <O, T extends ResultResponse> T request(ApiClient apiClient, BaseRequest<O, T> request) throws ApiException;

}
