package com.yupi.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserEmailRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String emailAccount;

    private String captcha;

    private String userName;

}
