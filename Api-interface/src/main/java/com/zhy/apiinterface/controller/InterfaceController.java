package com.zhy.apiinterface.controller;

import cn.hutool.json.JSONUtil;
import com.zhy.apiclientsdk.model.params.NameParams;
import com.zhy.apiclientsdk.model.params.WeatherParams;
import com.zhy.apiclientsdk.model.response.NameResponse;
import com.zhy.apiclientsdk.model.response.ResultResponse;
import com.zhy.apiinterface.utils.RequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.zhy.apiinterface.utils.ResponseUtils.baseResponse;

/**
 * Api
 */
@RestController
@RequestMapping("/")
public class InterfaceController {

    /**
     * 获取名字API
     * @param nameParams
     * @return
     */
    @GetMapping("/name")
    public NameResponse getName(NameParams nameParams) {
        return JSONUtil.toBean(JSONUtil.toJsonStr(nameParams), NameResponse.class);
    }

    /**
     * 获取随机情话API
     * @return
     */
    @GetMapping("/loveTalk")
    public String randomLoveTalk() {
        return RequestUtils.get("https://api.vvhan.com/api/love");
    }

    /**
     * 获取毒鸡汤API
     * @return
     */
    @GetMapping("/poisonousChickenSoup")
    public String getPoisonousChickenSoup() {
        return RequestUtils.get("https://api.btstu.cn/yan/api.php?charset=utf-8&encode=json");
    }

    /**
     * 获取天气API
     * @param weatherParams
     * @return
     */
    @GetMapping("/weather")
    public ResultResponse getWeatherInfo(WeatherParams weatherParams) {
        return baseResponse("https://api.vvhan.com/api/weather", weatherParams);
    }
}
