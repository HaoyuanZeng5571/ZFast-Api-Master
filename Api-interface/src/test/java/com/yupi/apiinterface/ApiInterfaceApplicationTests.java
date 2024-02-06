package com.yupi.apiinterface;

import com.yupi.apiclientsdk.client.ApiClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ApiInterfaceApplicationTests {

	//注入ApiClient
	@Resource
	private ApiClient apiClient;

//	@Test
//	public void ApiClientTest() {
//
//		//String result1 = apiClient.getNameByGet("zhy");
//		User user = new User();
//		user.setUsername("zzz");
//		String result3 = apiClient.getUserNameByPost(user);
//		//System.out.println(result1);
//		System.out.println(result3);
//	}



}
