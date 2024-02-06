package com.yupi.apicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.apicommon.model.entity.UserInterfaceInfo;

/**
* @author zenghaoyuan
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-09-09 16:33:52
*/
public interface InnerUserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);


}
