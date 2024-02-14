package com.zhy.project.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhy.apicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author zenghaoyuan
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2023-09-09 16:33:52
* @Entity com.zhy.project.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {


    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




