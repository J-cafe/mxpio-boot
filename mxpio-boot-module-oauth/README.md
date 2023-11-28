**应用模块的yml配置中需要引入以下**

```java
spring:
  security:
    oauth2:
      client:
        registration:
          authing:
            client-id: {替换为你的App ID如：App Secret5e72d72e3798fb03e1d57b13}
            client-secret: {替换为你的App Secret如：931f19ce2161e5560c072f586c706ee6}
            redirect-uri: {替换为登录的回调地址 如http://localhost:8080/callback}
            client-authentication-method: post
            scope:
              - openid
              - profile
        provider:
          authing:
            issuer-uri: {替换为你的Issuer，如：https://authing-net-sdk-demo.authing.cn/oidc}
            user-name-attribute: preferred_username  
```
实际值为oauth中台中对应应用的值，如果配置文件中缺少该属性，会影响应用启动。
