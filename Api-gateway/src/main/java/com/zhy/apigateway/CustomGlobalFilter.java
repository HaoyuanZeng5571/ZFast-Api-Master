package com.zhy.apigateway;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.apiclientsdk.utils.SignUtils;
import com.zhy.apicommon.model.dto.RequestParamsField;
import com.zhy.apicommon.model.entity.InterfaceInfo;
import com.zhy.apicommon.model.entity.User;
import com.zhy.apicommon.service.InnerInterfaceInfoService;
import com.zhy.apicommon.service.InnerUserInterfaceInfoService;
import com.zhy.apicommon.service.InnerUserService;
import com.zhy.utils.RedissonLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @Resource
    private RedissonLockUtil redissonLockUtil;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @DubboReference
    private InnerUserService innerUserService;

//    public static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final long FIVE_MINUTES = 60 * 5L;

    /**
     *
     * @param exchange 所有的请求信息、响应信息，响应体，请求体都能从这里拿到
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 1. 请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + request.getPath());
        log.info("请求方法：" + request.getMethod());
        log.info("请求参数：" + request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址：" + sourceAddress);
        log.info("请求来源地址：" + request.getRemoteAddress());
        log.info("url: "+ request.getURI());



        // 拿到响应对象
        ServerHttpResponse response = exchange.getResponse();
        // 3.访问控制，黑白名单
//        if (!IP_WHITE_LIST.contains(sourceAddress)) {
//            // 若访问地址不在白名单中，设置响应状态码为403 FORBIDDEN
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            // 返回处理完成的响应
//            return response.setComplete();
//        }

        // 4. 用户鉴权（ak和sk 的合法性）

        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");


        // 时间和当前时间不能超过5分钟
        long currentTime = System.currentTimeMillis() / 1000;
        if ((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
            return handleNoAuth(response);
        }

        // 验证身份合法性
        User invokeUser = null;
        try {
            // 调用内部服务，根据访问密钥获取用户信息
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            // 捕获异常，记录日志
            log.error("getInvokeUser error", e);
        }
        if (invokeUser == null) {
            // 如果用户信息为空，处理未授权情况并返回
            return handleNoAuth(response);
        }
        if (Long.parseLong(nonce) > 10000L) {
            return handleNoAuth(response);
        }

        // 从数据库中查询sk
        // 从获取到的ak对请求体中获取用户的sk
        String secretKey = invokeUser.getSecretKey();
        // 使用获取到的sk对请求体进行签名
        try {
            String serverSign = SignUtils.genSign(body, secretKey);
            // 检查请求中的签名是否于服务器生成的不一致
            if (sign == null || !sign.equals(serverSign)) {
                // 如果签名为空或不一致，返回处理未授权的响应
                return handleNoAuth(response);
            }
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }

        // 5. 请求的模拟接口是否存在
        // 初始化一个 InterfaceInfo 对象，用于存储查询结果
        InterfaceInfo interfaceInfo = null;
        String method = Objects.requireNonNull(request.getMethod()).toString();
        String uri = request.getURI().toString().trim();
        try {
            // 从内部接口信息服务获取指定路径和方法的接口信息
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(uri,method);
        }
        catch (Exception e) {
            // 如果获取接口信息时出现异常，记录错误日志
            log.error("getInterfaceInfo error", e);
        }
        // 检查是否成功获取到接口信息，
        if (interfaceInfo == null) {
            // 如果未获取到接口信息，返回处理未授权的响应
            return handleNoAuth(response);
        }

//        // 6.验证用户是否还有请求次数
//        int leftInvokeNum = innerUserInterfaceInfoService.leftInvokeNum(interfaceInfo.getId(), invokeUser.getId());
//        if (leftInvokeNum <= 0) {
//            return handleNoAuth(response);
//        }
        // 进行参数校验
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        String requestParams = interfaceInfo.getRequestParams();
        if (StringUtils.isNotBlank(requestParams)) {
            List<RequestParamsField> list = new Gson().fromJson(requestParams, new TypeToken<List<RequestParamsField>>() {
            }.getType());
            if ("GET".equals(method)) {
                log.info("GET 请求参数是：" + queryParams);
                for (RequestParamsField requestParamsField : list) {
                    if ("是".equals(requestParamsField.getRequired())) {
                        if (StringUtils.isBlank(queryParams.getFirst(requestParamsField.getFieldName())) || !queryParams.containsKey(requestParamsField.getFieldName())) {
                            return handleNoAuth(response);
                        }
                    }
                }
            }

        }
        // 7.请求转发，调用模拟接口 + 响应日志
         return handleResponse(exchange,  chain, invokeUser, interfaceInfo.getId());
    }


    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, User user, long interfaceInfoId) {
        try {
            // 获取原始的响应对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 7. 调用成功，接口调用次数 + 1 invokeCount
                                        try {
                                            redissonLockUtil.redissonDistributedLocks(("gateway" + user.getUserAccount()).intern(), ()->{
                                                innerUserInterfaceInfoService.invokeCount(interfaceInfoId, user.getId());
                                            }, "接口调用次数统计失败");
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }





    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {

        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {

        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}


