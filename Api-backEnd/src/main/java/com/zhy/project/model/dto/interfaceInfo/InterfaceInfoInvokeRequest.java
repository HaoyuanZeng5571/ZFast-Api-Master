package com.zhy.project.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 接口调用请求
 *
 * @TableName product
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    private List<Field> requestParams;

    /**
     * 请求参数
     */
    private String userRequestParams;

    public static final long serialVersionUId = 1L;

    @Data
    public static class Field {
        private String fieldName;
        private String value;
    }

}