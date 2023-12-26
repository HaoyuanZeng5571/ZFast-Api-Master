package com.yupi.apicommon.service;


/**
* @author zenghaoyuan
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-09-09 16:33:52
*/
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 验证剩余调用次数
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    int leftInvokeNum(long interfaceInfoId, long userId);

}
