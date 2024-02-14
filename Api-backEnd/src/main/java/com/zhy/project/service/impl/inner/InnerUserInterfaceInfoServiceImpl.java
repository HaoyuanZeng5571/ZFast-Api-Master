package com.zhy.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhy.apicommon.model.entity.UserInterfaceInfo;
import com.zhy.apicommon.service.InnerUserInterfaceInfoService;
import com.zhy.project.common.ErrorCode;
import com.zhy.project.exception.BusinessException;
import com.zhy.project.mapper.UserInterfaceInfoMapper;
import com.zhy.project.service.InterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo> implements InnerUserInterfaceInfoService {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        // 查询当前是否有对应用户的调用记录
        LambdaQueryWrapper<UserInterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId);
        queryWrapper.eq(UserInterfaceInfo::getUserId, userId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(queryWrapper);
        // 如果没有则创建一条记录
        boolean invokeResult;
        if (userInterfaceInfo == null) {
            UserInterfaceInfo createUser = new UserInterfaceInfo();
            createUser.setUserId(userId);
            createUser.setInterfaceInfoId(interfaceInfoId);
            createUser.setTotalInvokes(1);
            invokeResult = this.save(createUser);
        } else {
            LambdaUpdateWrapper<UserInterfaceInfo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId);
            updateWrapper.eq(UserInterfaceInfo::getUserId, userId);
            updateWrapper.setSql("totalInvokes = totalInvokes + 1");
            invokeResult = this.update(updateWrapper);
        }
        // 更新接口总调用次数
        boolean interfaceUpdateInvokeSave = interfaceInfoService.invokeCount(interfaceInfoId);
        boolean updateResult = invokeResult && interfaceUpdateInvokeSave;
        if (!updateResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "调用失败");
        }
        return true;
    }
}
