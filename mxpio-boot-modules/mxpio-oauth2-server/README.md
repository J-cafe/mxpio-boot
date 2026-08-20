# ERP系统启用oauth2 server步骤（Spring Authorization Server 7.0 / Spring Boot 4）

### 1.在mxpio-erp-parent中引入此模块

```xml
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-oauth2-server</artifactId>
</dependency>
```

> 模块基于 Spring Authorization Server 7.0（Spring Boot 4）实现，仅启用 **授权码（authorization_code）** 与
> **刷新令牌（refresh_token）** 两种授权类型。Spring Authorization Server 7.0 已移除 password 授权类型。
> 模块颁发的 access_token 即为 mxpio 系统自身的 JWT 令牌，并写入在线用户缓存，因此可直接放入请求头
> `Access-Token` 访问 ERP，与系统原有登录逻辑完全一致（不改动 mxpio-security）。

### 2.启动项目后，项目会在数据库中建表 MB_OAUTH_CLIENT_DETAILS，在此表中维护接入端内容
主要字段说明：
- `CLIENT_ID_`：接入端标识
- `CLIENT_SECRET_`：接入端密钥，格式：
  - 明文密码：`{noop}123456`
  - bcrypt 加密格式：`{bcrypt}$2a$10$.c/TiWuSmvwitqxBZsF5guQ6qQn08E1b1Aldff/LEb.dkeR0b4RGq`
- `GRANT_TYPES_`：授权类型，逗号分隔，如 `authorization_code,refresh_token`
- `SCOPE_`：作用域，逗号分隔，如 `read,write`（注意：如需 OIDC id_token 才使用 `openid`，本模块默认未启用 OIDC）
- `REDIRECT_URI_`：回调地址，逗号分隔
- `ACCESS_TOKEN_VALIDITY_` / `REFRESH_TOKEN_VALIDITY_`：Token 有效期（秒）
- `AUTO_APPROVE_SCOPES_`：自动批准的作用域（非空时跳过授权确认页）

### 3.第三方平台引导用户浏览器到授权端点获取 code 码
比如后端地址为 http://127.0.0.1:9008，则授权地址为：
```
http://127.0.0.1:9008/oauth2/authorize?response_type=code&client_id=xxxxx&redirect_uri=http://xxx.xxx.xxx.xxx&scope=read&state=xxxxx
```
- 若用户尚未登录，系统会跳转到 OAuth2 专用登录页 `/oauth2/login`（复用 mxpio 的用户体系登录）。
- 登录成功并授权后，系统将获取的 code 码附加在 `redirect_uri` 提供的地址后面返回，此请求为 GET。

### 4.第三方平台获取到 code 码后，调用后端接口，用 code 换取 token
比如后端地址为 http://127.0.0.1:9008，则调用地址为：
```
http://127.0.0.1:9008/oauth2/token?grant_type=authorization_code&client_id=fangsong&client_secret=123456&redirect_uri=http://www.baidu.com&code=4amfMp
```
返回结果中的 `access_token` 即为 mxpio 系统的 Access-Token。可使用 `refresh_token` 通过
`grant_type=refresh_token` 刷新获取新的 token。

### 5.将获取的 token 放在请求头 Access-Token 中，访问 ERP 系统
