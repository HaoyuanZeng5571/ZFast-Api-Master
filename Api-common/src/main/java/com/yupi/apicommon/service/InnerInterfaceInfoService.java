package com.yupi.apicommon.service;


import com.yupi.apicommon.model.entity.InterfaceInfo;

/**
* @author zenghaoyuan
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-09-02 16:47:41
*/
public interface InnerInterfaceInfoService {

    /**
     * 从数据库中查询模拟接口是否存在（请求路径，请求方法，请求参数）
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path, String method);

}
