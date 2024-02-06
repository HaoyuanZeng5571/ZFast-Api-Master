package com.yupi.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.apicommon.model.entity.InterfaceInfo;

/**
* @author zenghaoyuan
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-09-02 16:47:41
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @return
     */
    boolean invokeCount(long interfaceInfoId);
}
