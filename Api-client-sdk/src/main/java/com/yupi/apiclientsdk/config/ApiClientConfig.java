package com.yupi.apiclientsdk.config;

import com.yupi.apiclientsdk.client.ApiClient;
import com.yupi.apiclientsdk.service.ApiService;
import com.yupi.apiclientsdk.service.impl.ApiServiceImpl;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("qi.api.client")
@ComponentScan
public class ApiClientConfig {
    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 秘密密钥
     */
    private String secretKey;


    @Bean
    public ApiClient apiClient() {
        return new ApiClient(accessKey, secretKey);
    }

    @Bean
    public ApiService apiService() {
        ApiServiceImpl apiService = new ApiServiceImpl();
        apiService.setApiClient(new ApiClient(accessKey, secretKey));
        return apiService;
    }
}
