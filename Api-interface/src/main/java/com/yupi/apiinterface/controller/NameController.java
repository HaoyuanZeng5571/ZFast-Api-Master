package com.yupi.apiinterface.controller;

import com.yupi.apiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 名称Api
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public String getNameByGet(String name) {
        return "GET 你的名字是" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "Post 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
//        String accessKey = request.getHeader("accessKey");
//        //String secretKey = request.getHeader("secretKey");
//        String nonce = request.getHeader("nonce");
//        String sign = request.getHeader("sign");
//        String body = request.getHeader("body");
////        if (!accessKey.equals("yupi") || !secretKey.equals("abcdefg")) {
////            throw new RuntimeException("无权限");
////        }
//        if (!accessKey.equals("yupi")) {
//            throw new RuntimeException("无权限");
//        }
//        if (Long.parseLong(nonce) > 10000) {
//            throw new RuntimeException("无权限");
//        }
//        String serverSign = genSign(body, "abcdefg");
//        if (!sign.equals(serverSign)) {
//            throw new RuntimeException("无权限");
//        }
        String result = "Post 你的名字是" + user.getUsername();
        return result;
    }


}
