package com.yupi.project.model.dto.interfaceInfo;

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

    /**
     * 主键
     */
    private Long id;

    /**
     * 请求参数
     */
    private List<Field> userRequestParams;

    public static final long serialVersionUId = 1L;

    @Data
    public static class Field {
        private String fieldName;
        private String value;
    }

}