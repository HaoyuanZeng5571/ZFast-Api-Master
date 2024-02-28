package com.zhy.project.service;


import com.zhy.apiclientsdk.service.ApiService;
import com.zhy.apicommon.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 用户服务测试
 *
 * @author zhy
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Resource
    private ApiService apiService;

//    @Test
//    String getWeather() {
//
//        ApiClient apiClient = new ApiClient();
//        WeatherRequest weatherRequest = new WeatherRequest();
//        weatherRequest.setRequestParams();
//        apiService.getWeatherInfo(apiClient,)
//    }

    @Test
    void testAddUser() {
        User user = new User();
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        boolean result = userService.updateById(user);
        Assertions.assertTrue(result);
    }

    @Test
    void testDeleteUser() {
        boolean result = userService.removeById(1L);
        Assertions.assertTrue(result);
    }

    @Test
    void testGetUser() {
        User user = userService.getById(1L);
        Assertions.assertNotNull(user);
    }

}