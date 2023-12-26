package com.yupi.project.model.vo;

import com.yupi.apicommon.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 接口信息封装视图
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoVo extends InterfaceInfo {

    /**
     * 总调用次数
     */
    private Integer totalNum;

    public static final long serialVersionUID = 1L;
}