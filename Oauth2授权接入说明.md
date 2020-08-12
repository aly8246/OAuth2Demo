
oauth2授权接入说明[开发环境]:

> 请注意，这个文档仅仅争对开发环境，如果要配置测试环境和生产环境
> 那么需要重新申请oauth2认证信息，和获取新的认证服务器地址

---

step1. 申请oauth2认证信息
clientId:      third-drugstore-dev
clientSecret:  client1

---

step2. 获得开发环境的认证服务器地址
uri: http://ata.natapp1.cc/

---

step3. 配置spring boot的配置文件
properties或者yml都可以
application或者bootstrap都可以

```properties
security.oauth2.resource.id=user-center
security.oauth2.resource.token-info-uri=http://ata.natapp1.cc/auth/oauth/check_token
security.oauth2.resource.prefer-token-info=true
security.oauth2.resource.user-info-uri=http://ata.natapp1.cc/auth/uaa/user
security.oauth2.resource.jwt.key-uri=http://ata.natapp1.cc/auth/oauth/token_key
security.oauth2.client.access-token-uri=http://ata.natapp1.cc/auth/oauth/token
security.oauth2.client.user-authorization-uri=http://ata.natapp1.cc/auth/oauth/authorize/
security.oauth2.client.client-id=third-drugstore-dev
security.oauth2.client.client-secret=client1
```

---

step4. 导入oauth2依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
    <version>2.2.0.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
    <version>2.2.0.RELEASE</version>
</dependency>
```

step5. 拷贝com.example.demo.oauth2下面的代码至你的公共开发包下面，或者你的项目任意位置，并且扫描这个包

```java
@RequiredArgsConstructor
@RestController
public class TestController {
    private final SsoLoginUser ssoLoginUser;

    @PreAuthorize("isAuthenticated()")//用户必须登陆
    @GetMapping("oauth2")
    public SsoLoginUser get() {
        SsoLoginUser.LoginUserDetails loginUser = ssoLoginUser.getLoginUser();
        System.out.println(loginUser);
        return ssoLoginUser;
    }
}
```

以上是一个示例controller
注入SsoLoginUser后就可以通过打印获得oauth2登陆用户
```java
2020-08-12 12:38:27.717  INFO 4396 --- [)-192.168.3.130] o.s.web.servlet.DispatcherServlet        : Completed initialization in 8 ms
com.example.demo.oauth2.SsoLoginUser$LoginUserDetails@1c565535
```
，里面包含了用户的基本信息，身份信息，机构信息，地址信息和授权信息

这个注解说明用户必须要登陆
```java
   @PreAuthorize("isAuthenticated()")
```
如果前端不传递token，则回会得到一些错误消息
```json
/*token为空*/
{"code":"600","msg":"Full authentication is required to access this resource"}

/*token无效*/
{"code":"600","msg":"Cannot convert access token to JSON"}
```

如果前端要使用，则可以通过
http://业务域名:业务端口/业务路径/?access_token=Oauth2Token

或者
http://业务域名:业务端口/业务路径/
Header: Authentication:bearer Oauth2Token