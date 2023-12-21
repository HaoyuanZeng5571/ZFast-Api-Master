package com.yupi.apiclientsdk.client;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.apiclientsdk.model.User;

import java.util.HashMap;
import java.util.Map;

import static com.yupi.apiclientsdk.utils.SignUtils.genSign;

/**
 * 封装调用第三方接口的方法
 */
public class ApiClient {

    public static final String GATEWAY_HOST = "http://localhost:8090";

    private String accessKey;
    private String secretKey;

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    //使用GET方法从服务器获取名称信息
    public String getNameByGet(String name) {
        //可以单独传入HTTP参数，这样参数会自动做URL转码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        //将name参数添加到映射中
        paramMap.put("name",name);
        //使用HttpUtil工具发起GET请求，并获取服务器返回的结果
        String result = HttpUtil.get(GATEWAY_HOST + "/api/name/", paramMap);
        //打印服务器返回的结果
        System.out.println(result);
        //返回服务器返回的结果
        return result;
    }


    public String getNameByPost(String name) {
        //可以单独传入HTTP参数，这样参数会自动做URL转码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        //将name参数添加到映射中
        paramMap.put("name",name);
        //使用HttpUtil工具发起POST请求，并获取服务器返回的结果
        String result = HttpUtil.post(GATEWAY_HOST+"/api/name/post", paramMap);
        //打印服务器返回的结果
        System.out.println(result);
        //返回服务器返回的结果
        return result;
    }

    public Map<String,String> getHeaderMap(String body) {
        //  创建一个心的HashMap对象
        HashMap<String,String> hashMap = new HashMap<>();
        //将accessKey和对应的值放入map
        hashMap.put("accessKey",accessKey);
        //将secretKey和对应的值放入map
        //不能直接发送密钥
        //hashMap.put("secretKey",secretKey);
        //生成随机数（生成一个包含4个随机数字的字符串）
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        //请求提内容
        hashMap.put("body",body);
        // 当前时间戳
        // System.currentTimeMillis()返回当前时间的毫秒数，通过除以1000，转换为秒数
        // String.valueOf()用于将数值转换为字符串
        hashMap.put("timestamp",String.valueOf(System.currentTimeMillis() / 1000));
        // 利用SignUtils包中genSign方法生成签名
        hashMap.put("sign",genSign(body,secretKey));
        return hashMap;
    }

    public String getUserNameByPost(User user) {
        //将user对象转换为JSON字符串
        String jsonStr = JSONUtil.toJsonStr(user);
        //使用HttpUtil工具发起POST请求，并获取服务器返回的结果
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .addHeaders(getHeaderMap(jsonStr))
                .body(jsonStr)
                .execute();
        //打印服务器返回的状态码
        System.out.println(httpResponse.getStatus());
        //获取服务器返回的结果
        String result = httpResponse.body();
        //打印服务器返回的结果
        System.out.println(result);
        //返回服务器返回的结果
        return result;
    }

}
