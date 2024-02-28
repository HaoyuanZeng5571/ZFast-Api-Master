package com.zhy.apiclientsdk.service;

import com.zhy.apiclientsdk.client.ApiClient;
import com.zhy.apiclientsdk.exception.ApiException;
import com.zhy.apiclientsdk.model.request.BaseRequest;
import com.zhy.apiclientsdk.model.request.NameRequest;
import com.zhy.apiclientsdk.model.request.WeatherRequest;
import com.zhy.apiclientsdk.model.response.NameResponse;
import com.zhy.apiclientsdk.model.response.PoisonousChickenSoupResponse;
import com.zhy.apiclientsdk.model.response.ResultResponse;

public interface ApiService {

    /**
     * 通用请求
     * @param request
     * @return
     * @param <O>
     * @param <T>
     * @throws ApiException
     */
    <O, T extends ResultResponse> T request(BaseRequest<O, T> request) throws ApiException;

    /**
     * 通用请求
     * @param apiClient
     * @param request
     * @return
     * @param <O>
     * @param <T>
     * @throws ApiException
     */
    <O, T extends ResultResponse> T request(ApiClient apiClient, BaseRequest<O, T> request) throws ApiException;

    NameResponse getName(ApiClient apiClient, NameRequest request) throws ApiException;

    PoisonousChickenSoupResponse getPoisonousChickenSoup(ApiClient apiClient) throws ApiException;

    ResultResponse getWeatherInfo(ApiClient apiClient, WeatherRequest request) throws ApiException;

}
