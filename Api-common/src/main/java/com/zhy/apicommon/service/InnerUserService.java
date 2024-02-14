package com.zhy.apicommon.service;


import com.zhy.apicommon.model.entity.User;

/**
 * 用户服务
 *
 */
public interface InnerUserService {

    /**
     * 从数据库中查是否已经分配给用户密钥（ak）
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);

}
