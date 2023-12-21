package com.yupi.apiclientsdk.utils;


import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

public class SignUtils {

    /**
     * 生成签名
     * @param body
     * @param secretKey
     * @return
     */
    public static String genSign(String body, String secretKey) {

        //使用sha256算法的Digester
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        //  构建签名内容，将hash映射转换为字符串并拼接密钥
        String content = body + "." + secretKey;
        //计算签名的摘要并返回摘要的十六进制表示形式
        return md5.digestHex(content);
    }
}
