package com.yupi.project.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.apicommon.model.entity.User;
import com.yupi.project.common.ErrorCode;
import com.yupi.project.exception.BusinessException;
import com.yupi.project.mapper.UserMapper;
import com.yupi.project.model.dto.user.UserEmailLoginRequest;
import com.yupi.project.model.dto.user.UserEmailRegisterRequest;
import com.yupi.project.model.dto.user.UserLoginRequest;
import com.yupi.project.model.dto.user.UserRegisterRequest;
import com.yupi.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

import static com.yupi.project.constant.EmailConstant.CAPTCHA_CACHE_KEY;
import static com.yupi.project.constant.UserConstant.ADMIN_ROLE;
import static com.yupi.project.constant.UserConstant.USER_LOGIN_STATE;


/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "api";

    /**
     * 用户账号密码注册
     * @param userRegisterRequest
     * @return
     */
    @Override
    public long userRegister(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String userName = userRegisterRequest.getUserName();
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        // 验证username的长度
        if (userName.length() > 40) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名过长！");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = userMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 3. 分配accessKey，secretKey
            // 使用DigestUtil.md5Hex将盐值，用户账户和5-8位随机数进行md5加密
            String accessKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(5));
            String secretKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(8));
            // 4. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserName(userName);
            user.setUserPassword(encryptPassword);
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    /**
     * 用户邮箱注册
     * @param userEmailRegisterRequest
     * @return
     */
    @Override
    public long userEmailRegister(UserEmailRegisterRequest userEmailRegisterRequest) {
        String emailAccount = userEmailRegisterRequest.getEmailAccount();
        String captcha = userEmailRegisterRequest.getCaptcha();
        String userName = userEmailRegisterRequest.getUserName();

        // 验证传入的邮箱，验证码是否为空
        if (StringUtils.isAnyBlank(emailAccount,captcha)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "邮箱，验证码为空！");
        }
        // 验证username的长度
        if (userName.length() > 40) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名过长！");
        }
        // 验证邮箱格式
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(emailPattern, emailAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式不正确！");
        }
        // 验证验证码
        String cacheCaptcha = redisTemplate.opsForValue().get(CAPTCHA_CACHE_KEY + emailAccount);
        if (StringUtils.isBlank(cacheCaptcha)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码已过期，请重新获取！");
        }
        captcha = captcha.trim();
        if (!captcha.equals(cacheCaptcha)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误，请输入正确的验证码！");
        }
        synchronized (emailAccount.intern()) {

            // 执行注册流程，存入用户信息
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", emailAccount);
            Long count = userMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "账号已存在！");
            }
            // 使用DigestUtil.md5Hex将盐值，用户账户和5-8位随机数进行md5加密
            String accessKey = DigestUtil.md5Hex(SALT + emailAccount + RandomUtil.randomNumbers(5));
            String secretKey = DigestUtil.md5Hex(SALT + emailAccount + RandomUtil.randomNumbers(8));
            // 插入数据
            User user = new User();
            user.setUserAccount(emailAccount);
            user.setUserName(userName);
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);

            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "注册失败！");
            }
            return user.getId();
        }
    }

    /**
     * 用户账号密码登陆
     * @param userLoginRequest
     * @param request
     * @return
     */
    @Override
    public User userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return user;
    }

    /**
     * 用户邮箱注册
     * @param userEmailLoginRequest
     * @param request
     * @return
     */
    @Override
    public User userEmailLogin(UserEmailLoginRequest userEmailLoginRequest, HttpServletRequest request) {
        String emailAccount = userEmailLoginRequest.getEmailAccount();
        String captcha = userEmailLoginRequest.getCaptcha();

        // 验证传入的邮箱，验证码是否为空
        if (StringUtils.isAnyBlank(emailAccount, captcha)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱，验证码为空！");
        }
        // 验证邮箱格式
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(emailPattern, emailAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式不正确！");
        }
        // 验证验证码
        String cacheCaptcha = redisTemplate.opsForValue().get(CAPTCHA_CACHE_KEY + emailAccount);
        if (StringUtils.isBlank(cacheCaptcha)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码已过期，请重新获取！");
        }
        captcha = captcha.trim();
        if (!captcha.equals(cacheCaptcha)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误，请输入正确的验证码！");
        }
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", emailAccount);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return user;
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && ADMIN_ROLE.equals(user.getUserRole());
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

}




