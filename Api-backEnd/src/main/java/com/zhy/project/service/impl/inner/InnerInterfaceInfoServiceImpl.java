package com.zhy.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhy.apicommon.model.entity.InterfaceInfo;
import com.zhy.apicommon.service.InnerInterfaceInfoService;
import com.zhy.project.common.ErrorCode;
import com.zhy.project.exception.BusinessException;
import com.zhy.project.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    /**
     * 实现接口的 getInterfaceInfo 方法，根据url和method获取内部接口信息
     * @param url
     * @param method
     * @return
     */
    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        // 参数校验
        if (StringUtils.isAnyBlank(url,method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 如果带参数，去除第一个？和之后的参数
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        // 创建查询条件包装器
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url",url);
        queryWrapper.eq("method",method);
        // 使用 interfaceInfoMapper 的 selectOne 方法查询接口信息
        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(queryWrapper);
        return interfaceInfo;
    }
}
