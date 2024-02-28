package com.zhy.apiclientsdk.service.impl;

import com.zhy.apiclientsdk.client.ApiClient;
import com.zhy.apiclientsdk.exception.ApiException;
import com.zhy.apiclientsdk.model.request.NameRequest;
import com.zhy.apiclientsdk.model.request.PoisonousChickenSoupRequest;
import com.zhy.apiclientsdk.model.request.WeatherRequest;
import com.zhy.apiclientsdk.model.response.NameResponse;
import com.zhy.apiclientsdk.model.response.PoisonousChickenSoupResponse;
import com.zhy.apiclientsdk.model.response.ResultResponse;
import com.zhy.apiclientsdk.service.ApiService;
import com.zhy.apiclientsdk.service.BaseService;

public class ApiServiceImpl extends BaseService implements ApiService {
    @Override
    public PoisonousChickenSoupResponse getPoisonousChickenSoup(ApiClient apiClient) throws ApiException {
        PoisonousChickenSoupRequest request = new PoisonousChickenSoupRequest();
        return request(apiClient,request);
    }

    @Override
    public NameResponse getName(ApiClient apiClient, NameRequest request) throws ApiException {
        return request(apiClient,request);
    }

    @Override
    public ResultResponse getWeatherInfo(ApiClient apiClient,WeatherRequest request) throws ApiException {
        return request(apiClient,request);
    }
}
