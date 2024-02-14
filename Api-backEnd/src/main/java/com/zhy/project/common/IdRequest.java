package com.zhy.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装id
 */
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}