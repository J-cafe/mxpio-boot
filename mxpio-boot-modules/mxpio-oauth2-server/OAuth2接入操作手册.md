# OAuth2 Server 接入操作手册（授权码模式 response_type=code）

> 适用模块：`mxpio-oauth2-server`（基于 Spring Authorization Server 7.0 / Spring Boot 4）
>
> 本手册面向**第三方系统接入方**，描述如何通过标准 OAuth2 授权码模式（`response_type=code`）接入 mxpio 平台并获取访问令牌。

---

## 1. 概述

- mxpio 平台作为 **OAuth2 授权服务器（Authorization Server）**，第三方系统作为 **客户端（Client）**。
- 仅支持以下授权类型（`grant_type`）：
  - `authorization_code` —— 授权码模式（本手册主要内容）
  - `refresh_token` —— 刷新令牌
  - > `password` 授权类型已被 Spring Authorization Server 7.0 移除，不再支持。
- 颁发的 `access_token` 即为 mxpio 系统自身的 JWT 令牌，并已写入在线用户缓存，直接放入请求头 `Access-Token` 即可访问 mxpio/ERP 各业务接口，与系统原生登录完全一致。

## 2. 地址约定（重要）

本手册所有示例中的地址均使用占位符，接入时请按实际部署环境替换，**不要在任何配置或代码中写死 IP 和端口**：

| 占位符 | 含义 | 示例格式 |
| --- | --- | --- |
| `{AUTH_SERVER_BASE_URL}` | mxpio 后端服务基础地址（协议 + 域名/IP + 端口 + [context-path]） | `https://erp.example.com`、`http://192.168.1.100:9008/mxpio` |
| `{CLIENT_BASE_URL}` | 第三方系统对外可访问的基础地址 | `https://portal.example.com` |
| `{CLIENT_ID}` / `{CLIENT_SECRET}` | 平台方分配的接入端标识与密钥 | — |

建议在第三方系统中通过配置项/环境变量维护这些地址，例如：

```
# 第三方系统配置示例
oauth.server-base-url=https://erp.example.com
oauth.client-id=my-client-id
oauth.client-secret=********
oauth.redirect-uri=https://portal.example.com/oauth/callback
```

> 注意：若 mxpio 后端配置了 `server.servlet.context-path`，则 `{AUTH_SERVER_BASE_URL}` 需包含该前缀。

## 3. 服务端启用（平台管理员操作）

### 3.1 引入模块

在项目 parent 的 pom 中引入：

```xml
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-oauth2-server</artifactId>
</dependency>
```

启动项目后自动建表 `MB_OAUTH_CLIENT_DETAILS`。

### 3.2 登记接入端

在该表中维护接入方信息，核心字段：

| 字段 | 说明 | 示例 |
| --- | --- | --- |
| `CLIENT_ID_` | 接入端标识（主键） | `third-party-portal` |
| `CLIENT_SECRET_` | 接入端密钥。支持 `{noop}明文` 与 `{bcrypt}密文` 两种格式 | `{noop}123456` 或 `{bcrypt}$2a$10$...` |
| `GRANT_TYPES_` | 授权类型，逗号分隔 | `authorization_code,refresh_token` |
| `SCOPE_` | 作用域，逗号分隔 | `read,write` |
| `REDIRECT_URI_` | 回调地址，逗号分隔，**必须与授权请求中的 `redirect_uri` 完全一致（含协议、端口、路径）** | `https://portal.example.com/oauth/callback` |
| `ACCESS_TOKEN_VALIDITY_` | access_token 有效期（秒） | `7200` |
| `REFRESH_TOKEN_VALIDITY_` | refresh_token 有效期（秒） | `604800` |
| `AUTO_APPROVE_SCOPES_` | 自动批准的作用域（非空时跳过授权确认页） | `read,write` |

参考 SQL：

```sql
INSERT INTO MB_OAUTH_CLIENT_DETAILS (
  ID_, CREATE_TIME_, CREATE_BY_, CLIENT_ID_, CLIENT_SECRET_,
  GRANT_TYPES_, SCOPE_, REDIRECT_URI_,
  ACCESS_TOKEN_VALIDITY_, REFRESH_TOKEN_VALIDITY_, AUTO_APPROVE_SCOPES_
) VALUES (
  REPLACE(UUID(), '-', ''), NOW(), 'admin', 'third-party-portal', '{noop}123456',
  'authorization_code,refresh_token', 'read,write', 'https://portal.example.com/oauth/callback',
  7200, 604800, 'read,write'
);
```

> 说明：
> - 机密客户端（配置了 `CLIENT_SECRET_`）无需 PKCE；公共客户端（`CLIENT_SECRET_` 留空）将强制要求 PKCE（RFC 7636）。
> - 生产环境建议使用 `{bcrypt}` 格式存放密钥，并保证 `redirect_uri` 使用 HTTPS。

## 4. 接入端点一览

| 端点 | 说明 |
| --- | --- |
| `GET {AUTH_SERVER_BASE_URL}/oauth2/authorize` | 授权端点，浏览器跳转获取授权码 |
| `POST {AUTH_SERVER_BASE_URL}/oauth2/token` | 令牌端点，用授权码换取 / 刷新令牌 |
| `GET/POST {AUTH_SERVER_BASE_URL}/oauth2/login` | OAuth2 专用登录页（未登录时自动跳转，无需第三方处理） |

## 5. 接入流程（授权码模式）

时序概览：

```
第三方系统(客户端)             用户浏览器              mxpio(OAuth2 Server)
      |                          |                        |
      |  1. 重定向到 /oauth2/authorize  ─────────────────> |
      |                          |   2. 未登录则展示 /oauth2/login
      |                          |      （已携带 mxpio Access-Token 的浏览器可免登录，SSO）
      |                          |   3. 登录成功，签发授权码 code
      | <──── 4. 302 回调 {CLIENT_BASE_URL}/callback?code=xxx&state=yyy
      |                          |                        |
      |  5. POST /oauth2/token（服务端对服务端，携带 code）> |
      | <──── 6. 返回 access_token / refresh_token ─────── |
      |                          |                        |
      |  7. 携带 Access-Token 请求头访问业务接口 ─────────> |
```

### 5.1 第一步：引导用户浏览器请求授权码

第三方系统将用户浏览器重定向到授权端点：

```
GET {AUTH_SERVER_BASE_URL}/oauth2/authorize?response_type=code
    &client_id={CLIENT_ID}
    &redirect_uri={CLIENT_BASE_URL}/oauth/callback
    &scope=read
    &state={随机字符串}
```

参数说明：

| 参数 | 必填 | 说明 |
| --- | --- | --- |
| `response_type` | 是 | 固定为 `code` |
| `client_id` | 是 | 平台分配的接入端标识 |
| `redirect_uri` | 是 | 回调地址，必须与 `REDIRECT_URI_` 中登记的**完全一致**（需 URL 编码） |
| `scope` | 否 | 请求的作用域，空格或逗号分隔，必须是已登记 scope 的子集 |
| `state` | 强烈建议 | 随机字符串，用于防 CSRF；平台会原样回传，回调时必须校验一致性 |

行为说明：

- 若用户尚未登录，系统自动跳转到 `/oauth2/login` 登录页（复用 mxpio 用户体系）；已在 mxpio 系统登录过的浏览器（携带 Access-Token Cookie/Header）可免登录直接授权（SSO）。
- 授权成功后，平台以 **302 重定向（GET）** 返回：

```
{redirect_uri}?code={授权码}&state={原样回传的state}
```

- 授权码**一次性使用、有效期较短（默认约 5 分钟）**，请立即用于换取令牌，切勿存储或复用。

前端跳转示例（从第三方系统页面发起）：

```javascript
// 推荐地址全部来自配置项，避免写死 IP/端口
const authUrl = `${OAUTH_CONFIG.serverBaseUrl}/oauth2/authorize` +
  `?response_type=code` +
  `&client_id=${encodeURIComponent(OAUTH_CONFIG.clientId)}` +
  `&redirect_uri=${encodeURIComponent(OAUTH_CONFIG.redirectUri)}` +
  `&scope=read` +
  `&state=${encodeURIComponent(crypto.randomUUID())}`;
window.location.href = authUrl;
```

### 5.2 第二步：接收回调并校验

第三方系统在 `redirect_uri` 对应的回调端点中：

1. 校验 `state` 与发起授权时保存的值一致（防 CSRF），不一致则拒绝；
2. 从查询参数中取出 `code`；
3. 若返回的是 `error` / `error_description`（如用户拒绝授权），按业务处理。

### 5.3 第三步：用授权码换取令牌

由**第三方后端服务**（而非浏览器）向令牌端点发起请求。支持两种客户端认证方式：

**方式一：client_secret_post（参数放请求体）**

```bash
curl -X POST "{AUTH_SERVER_BASE_URL}/oauth2/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=authorization_code" \
  -d "client_id={CLIENT_ID}" \
  -d "client_secret={CLIENT_SECRET}" \
  -d "code={第一步获取的授权码}" \
  -d "redirect_uri={与授权请求完全一致的回调地址}"
```

**方式二：client_secret_basic（HTTP Basic 认证，推荐）**

```bash
curl -X POST "{AUTH_SERVER_BASE_URL}/oauth2/token" \
  -u "{CLIENT_ID}:{CLIENT_SECRET}" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=authorization_code" \
  -d "code={第一步获取的授权码}" \
  -d "redirect_uri={与授权请求完全一致的回调地址}"
```

成功响应（JSON）：

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiJ9...",
  "refresh_token": "xxxxxx",
  "token_type": "Bearer",
  "expires_in": 7200,
  "scope": "read"
}
```

> 注意：
> - `redirect_uri` 参数必须与第一步授权请求中的值完全一致。
> - 授权码只能使用一次，重复使用会报 `invalid_grant`。

### 5.4 第四步：携带令牌访问业务接口

将 `access_token` 放入请求头 `Access-Token`：

```bash
curl -H "Access-Token: {access_token}" \
  "{AUTH_SERVER_BASE_URL}/xxx/业务接口"
```

前端可将 token 放入后续请求头中，效果与 mxpio 原生登录令牌一致。

### 5.5 第五步：刷新令牌（可选）

`access_token` 过期后，使用 `refresh_token` 换取新令牌，无需用户重新登录授权：

```bash
curl -X POST "{AUTH_SERVER_BASE_URL}/oauth2/token" \
  -u "{CLIENT_ID}:{CLIENT_SECRET}" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=refresh_token" \
  -d "refresh_token={之前获取的refresh_token}"
```

> 第三方系统应按 `expires_in` 提前规划刷新，避免业务请求中途失效。

## 6. 常见错误排查

| 错误（error） | 场景 | 原因与处理 |
| --- | --- | --- |
| `invalid_client` | 换取令牌 | `client_id`/`client_secret` 不正确，或 secret 前缀格式（`{noop}`/`{bcrypt}`）配置有误 |
| `invalid_grant` | 换取令牌 | code 已使用/已过期（默认约 5 分钟）；`redirect_uri` 与授权请求不一致；refresh_token 已失效 |
| `invalid_request` | 授权/换令牌 | 缺少必要参数，如 `response_type`、`code`、`redirect_uri` |
| `access_denied` | 回调 | 用户在授权页拒绝了授权 |
| `unauthorized_client` | 授权 | 接入端未在 `GRANT_TYPES_` 中登记 `authorization_code` |
| `invalid_scope` | 授权 | 请求的 `scope` 超出 `SCOPE_` 登记范围 |
| 跳转后 404 / 302 异常 | 授权 | `redirect_uri` 与 `REDIRECT_URI_` 登记值不一致（含大小写、末尾斜杠、端口） |
| 始终跳登录页 | 授权 | 未登录属正常流程；若期望 SSO 免登，请确认浏览器已持有有效的 mxpio Access-Token（Header/Cookie） |

## 7. 安全建议

1. **不写死地址**：`{AUTH_SERVER_BASE_URL}`、`redirect_uri` 等一律通过配置中心/环境变量维护，便于多环境（开发/测试/生产）切换。
2. `redirect_uri` 生产环境使用 HTTPS，且精确到路径，不要使用通配或域名级地址。
3. 每次授权生成随机 `state` 并校验回传值。
4. `client_secret` 仅保存在第三方**后端**，不得暴露到前端、日志或代码仓库。
5. access_token 不要发给浏览器长期存储；如需前端使用，请控制有效期并做好刷新。
