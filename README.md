## ZFast Api Open Platform

### Project description

**ZFastApi** is an efficient, reliable and secure interface open platform to provide the interface service for both common users and developers, in which to help them easily achieve a variety of functions and data interaction, improve work efficiency and using experience .

🙋As a **user**, you can register to log in to the account, obtain the interface call permission, and browse and select the appropriate interface according to your own needs. You can debug the interface online to quickly verify the function of the interface.

💻 As **developers** we provide  [Api-client-sdk](https://github.com/HaoyuanZeng5571/ZFast-Api-Master/tree/master/Api-client-sdk), through the pair of the Access Key and Secret Key can easily integrated the interface into your project. Enable more efficient development and invocation.

---

#### Project module

This project is a front-end and back-end separated project. The back-end utilizes **Spring Cloud** and **Spring Boot** as the service framework. **Spring Cloud Gateway** is used as the global gateway to achieve traffic control, route forward, unified authentication, access control and unified log. **MyBatis-Plus** is used as the persistence layer technology. **Apache Dubbo** is employed for high-performance remote service invocation. **Redis** is used as a cache and stored the user login sessions.


| Directory                                                    | Description                       |
| ------------------------------------------------------------ | --------------------------------- |
| [Api-backEnd](https://github.com/HaoyuanZeng5571/ZFast-Api-Master/tree/master/Api-backEnd) | ZFast Api back-end service module |
| [Api-common](https://github.com/HaoyuanZeng5571/ZFast-Api-Master/tree/master/Api-common) | Common service module             |
| [Api-gateway](https://github.com/HaoyuanZeng5571/ZFast-Api-Master/tree/master/Api-gateway) | Gateway module                    |
| [Api-interface](https://github.com/HaoyuanZeng5571/ZFast-Api-Master/tree/master/Api-interface) | Interface module                  |
| [Api-client-sdk](https://github.com/HaoyuanZeng5571/ZFast-Api-Master/tree/master/Api-client-sdk) | Developer invoke SDK              |

---

#### Flow chart of the ZFast API

![flow chart](/Users/zenghaoyuan/Desktop/flow chart.png)



---

#### Api-client-sdk introduction

Api-client-sdk is the developer toolkit for ZFast api open platform. Providing a simplified toolkit that allows users to invoke interface without manually writing and encapsulating HTTP requests, simplifying the complex process of the interface invocation.

##### Quick start of the Api-client-sdk🚀

1. **Introduce the dependency**  

```java
<dependency>
    <groupId>com.zhy</groupId>
    <artifactId>Api-client-sdk</artifactId>
    <version>0.0.1</version>
</dependency>
```

2. **Get the Access key and Secret key from [Zfast api open platform](www.zfastapi.com)**
3. **Initialize the Apiclient**

```java
String accessKey = "Your Access key";
String secretKey = "Your Secret key";
ApiClient apiClient = new ApiClient(accessKey, secretKey);
```

4. **Inject the Apiservice in your project**

```java
@Resource
private ApiService apiService;
```

5. **Send the request**

  Example (Get input name): 

```java
try {
     NameRequest nameRequest = new NameRequest();
     nameRequest.setMethod("Interface method");
     nameRequest.setPath("Interface URL");
     nameRequest.setRequestParams("Interface params");
     NameResponse name = apiService.getName(apiClient, nameRequest);
     System.out.println("Name = " + name);
}catch (Exception e) {
     log.error(e.getMessage());
}
```

Response:

```JSON
{
  "name": "name"
}
```

---

#### Function display

##### Login page

![image-20240229011427608](/Users/zenghaoyuan/Library/Application Support/typora-user-images/image-20240229011427608.png)

##### Registry page

![image-20240229011450952](/Users/zenghaoyuan/Library/Application Support/typora-user-images/image-20240229011450952.png)

##### Home page

![image-20240229010622245](/Users/zenghaoyuan/Library/Application Support/typora-user-images/image-20240229010622245.png)

##### Interface description

1. Online interface

![image-20240229010857418](/Users/zenghaoyuan/Library/Application Support/typora-user-images/image-20240229010857418.png)

2. Online invoke

![image-20240229010939760](/Users/zenghaoyuan/Library/Application Support/typora-user-images/image-20240229010939760.png)

3. Error code reference

![image-20240229011007412](/Users/zenghaoyuan/Library/Application Support/typora-user-images/image-20240229011007412.png)

##### Admin page

1. Interface management

![image-20240229011235297](/Users/zenghaoyuan/Library/Application Support/typora-user-images/image-20240229011235297.png)

2. Interface analysis

![image-20240229011313709](/Users/zenghaoyuan/Library/Application Support/typora-user-images/image-20240229011313709.png)

##### User information

![image-20240229011358023](/Users/zenghaoyuan/Library/Application Support/typora-user-images/image-20240229011358023.png)
