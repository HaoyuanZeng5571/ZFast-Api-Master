package com.yupi.apiclientsdk;


import com.yupi.apiclientsdk.client.ApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// 声明配置类
@Configuration
// 能够读取application.yml中的配置，读取到配置之后设置到属性之中
// 这里给所有的配置加上前缀为"api.client"
@ConfigurationProperties("api.client")
@Data
// 自动扫描组建，使得spring能够自动注册响应的Bean
@ComponentScan
public class ApiClientSdkConfig {

    private String accessKey;

    private String secretKey;

    //创建对象
    @Bean
    public ApiClient apiClient() {
        // 使用ak和sk创建Apiclient实例
        return new ApiClient(accessKey,secretKey);
    }
}
