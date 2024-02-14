package com.zhy.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhy.apicommon.model.entity.InterfaceInfo;
import com.zhy.project.common.ErrorCode;
import com.zhy.project.exception.BusinessException;
import com.zhy.project.mapper.InterfaceInfoMapper;
import com.zhy.project.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author zenghaoyuan
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2023-09-02 16:47:41
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        // 第一次创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

    @Override
    public boolean invokeCount(long interfaceInfoId) {
        LambdaUpdateWrapper<InterfaceInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(InterfaceInfo::getId, interfaceInfoId);
        updateWrapper.setSql("totalInvokes = totalInvokes + 1");
        boolean update = this.update(updateWrapper);
        return update;
    }

}




