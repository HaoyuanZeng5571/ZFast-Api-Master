package com.zhy.apiclientsdk.model.params;

import lombok.Data;

import java.io.Serializable;

/**
 * 获取天气请求参数
 */
@Data
public class WeatherParams implements Serializable {

    public static final long serialVersionUID = 3815188540434269370L;

    private String ip;

    private String city;

    private String type;

}
