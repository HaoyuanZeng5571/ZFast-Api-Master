package com.yupi.project.model.dto.interfaceInfo;

import lombok.Data;

/**
 * 请求参数包装类
 */
@Data
public class RequestParamsField {
    private String id;
    private String fieldName;
    private String type;
    private String desc;
    private String required;
}