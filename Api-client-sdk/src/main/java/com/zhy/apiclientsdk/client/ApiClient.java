package com.zhy.apiclientsdk.client;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装调用第三方接口的方法
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiClient {

    private String accessKey;

    private String secretKey;

}
