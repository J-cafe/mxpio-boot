该模块被项目引用时，BasicConfiguration使用到的配置参数：

```java
aad:
  clientId: 应用clientid
  authority: 授权地址
  secretKey: 应用密钥
  redirectUriSignin: 认证后重定向地址
  redirectUriGraph: https://localhost:8443/msal4jsample/graph/me
  msGraphEndpointHost: https://graph.microsoft.com/
```
需要在应用配置yml文件中，配置以上参数，否则启动会报错
