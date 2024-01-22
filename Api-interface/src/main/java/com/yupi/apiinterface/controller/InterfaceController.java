package com.yupi.apiinterface.controller;

import cn.hutool.json.JSONUtil;
import com.yupi.apiclientsdk.model.params.NameParams;
import com.yupi.apiclientsdk.model.response.NameResponse;
import com.yupi.apiinterface.utils.RequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
