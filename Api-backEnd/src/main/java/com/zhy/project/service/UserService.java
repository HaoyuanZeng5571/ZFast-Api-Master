package com.zhy.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zhy.apicommon.model.entity.User;
import com.zhy.project.model.dto.user.UserEmailLoginRequest;
import com.zhy.project.model.dto.user.UserEmailRegisterRequest;
import com.zhy.project.model.dto.user.UserLoginRequest;
import com.zhy.project.model.dto.user.UserRegisterRequest;
import com.zhy.project.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 */
public interface UserService extends IService<User> {

    /**
     * 账号密码注册
     * @param userRegisterRequest
     * @return
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 邮箱登录
     * @param userEmailLoginRequest
     * @param request
     * @return
     */
    User userEmailLogin(UserEmailLoginRequest userEmailLoginRequest, HttpServletRequest request);

    /**
     * 邮箱注册
     * @param userEmailRegisterRequest
     * @return
     */
    long userEmailRegister(UserEmailRegisterRequest userEmailRegisterRequest);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 更新ak/sk
     * @param loginUser
     * @return
     */
    UserVO updateVoucher(User loginUser);
}
