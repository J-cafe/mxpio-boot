# mxpio-security — 权限安全模块

## 概述

`mxpio-security` 是基于 Spring Security 6.x 的 RBAC（Role-Based Access Control）权限模型实现，提供完整的用户、部门、岗位、角色、权限五层管理体系和细粒度的资源访问控制。

### 五层 RBAC 模型

```
User → Dept / Post → Role → Authority (URL / Element / DataResource)
```

| 层级         | 说明                         | 对应实体/接口                          |
|--------------|------------------------------|----------------------------------------|
| User         | 系统用户                     | `UserController` 管理                  |
| Dept         | 用户所属部门                 | `DeptController` 管理                  |
| Post         | 用户所属岗位                 | `PostController` 管理                  |
| Role         | 角色（关联权限）             | `RoleController` 管理                  |
| Authority    | URL/按钮/数据权限            | `ResourceController`、`DataFilterController` |

### 功能特性

- URL 级接口权限控制
- 页面按钮级权限控制
- 数据范围权限控制（按部门等维度）
- Token 认证（JWT/AES 加密）
- 多 Provider 授权
- 缓存自动清理

---

## Maven 依赖

```xml
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-security</artifactId>
</dependency>
```

---

## 资源控制

### URL 资源 — `mb_url` 表

URL 资源定义了系统的 REST API 接口权限，存储在 `mb_url` 表中。框架通过 `UrlFilterConfigAttributeProvider` 自动加载 URL 权限配置并应用于 Spring Security 的过滤器链。

```java
// UrlFilterConfigAttributeProvider 自动注册 URL 权限配置
// 框架自动扫描 @RequestMapping 注解并关联 mb_url 中的权限配置
```

### 元素（按钮）资源 — `mb_element` 表

元素资源控制页面按钮级别的可见/可用权限，存储在 `mb_element` 表中。通过 `ElementProviderImpl` 提供前端按钮权限判断。

```java
// 元素资源提供者
public class ElementProviderImpl implements ElementProvider {
    @Override
    public Set<String> getElementsByUserId(Long userId) {
        // 查询用户拥有的按钮权限标识
        return elementService.findCodesByUserId(userId);
    }
}
```

前端权限判断示例：

```html
<!-- 通过权限标识控制按钮显示 -->
<button th:if="${#perms.has('system:user:add')}">新增用户</button>
```

---

## 数据权限

### DataScapeProvider — 数据权限提供者

数据权限用于控制用户能访问哪些范围内的数据（如仅本部门、本部门及下级、全部等）。

```java
@Component
public class MyDataScapeProvider implements DataScapeProvider {
    @Override
    public String getDataScapeSql(Long userId) {
        // 根据用户 ID 返回 SQL 条件片段
        // 例如: "dept_id IN (SELECT id FROM sys_dept WHERE parent_ids LIKE '%/101/%')"
        return "dept_id = (SELECT dept_id FROM sys_user WHERE id = " + userId + ")";
    }
}
```

### DataScapeContext — 数据权限上下文

用于在查询时注入数据权限上下文：

```java
// 在 Service 层设置数据权限上下文
DataScapeContext.setUserId(currentUserId);

// 使用 DataScapeProvider 生成的 SQL 片段拼接到查询中
```

---

## 授权 Provider

框架提供多个 `GrantedAuthorityProvider`，分别处理不同维度的权限源：

| Provider                          | 说明                 |
|-----------------------------------|----------------------|
| `UserGrantedAuthorityProvider`    | 用户固有权限         |
| `DeptGrantedAuthorityProvider`    | 部门派生权限         |
| `PostGrantedAuthorityProvider`    | 岗位派生权限         |

```java
// 自定义授权提供者（可选）
@Component
public class CustomGrantedAuthorityProvider implements GrantedAuthorityProvider {
    @Override
    public Set<GrantedAuthority> provide(Long userId) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        // 从自定义来源加载权限
        authorities.add(new SimpleGrantedAuthority("CUSTOM_PERMISSION"));
        return authorities;
    }
}
```

---

## 密码加密

使用 `PasswordEncoderFactories` 创建委派密码编码器（支持多种编码格式自动识别）：

```java
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Bean
public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
}
```

默认支持的编码类型：`bcrypt`, `ldap`, `MD4`, `MD5`, `noop`, `pbkdf2`, `scrypt`, `SHA-1`, `SHA-256`, `argon2`。

---

## Token 认证

### TokenUtil — Token 工具

生成和解析用户认证 Token：

```java
import com.mxpio.security.util.TokenUtil;

// 生成 Token
String token = TokenUtil.generateToken(userId, username);

// 从 Token 解析用户信息
Long userId = TokenUtil.getUserId(token);
String username = TokenUtil.getUsername(token);
```

### AesEncryptUtil — AES 加密

Token 内容使用 AES 加密增强安全性：

```java
import com.mxpio.security.util.AesEncryptUtil;

// 加密
String encrypted = AesEncryptUtil.encrypt(data);

// 解密
String decrypted = AesEncryptUtil.decrypt(encrypted);
```

---

## REST Controller 一览

| Controller              | 路径               | 说明               |
|-------------------------|--------------------|--------------------|
| `UserController`        | `/user/**`         | 用户管理 CRUD      |
| `RoleController`        | `/role/**`         | 角色管理 CRUD      |
| `DeptController`        | `/dept/**`         | 部门管理 CRUD      |
| `PostController`        | `/post/**`         | 岗位管理 CRUD      |
| `PermissionController`  | `/permission/**`   | 权限分配管理       |
| `ResourceController`    | `/resource/**`     | URL/元素资源配置    |
| `DataFilterController`  | `/data-filter/**`  | 数据权限过滤配置    |
| `SystemController`      | `/system/**`       | 系统配置管理        |

---

## 缓存清理

当权限数据发生变化时，使用 `SecurityCacheEvict` 自动清理相关缓存：

```java
import com.mxpio.security.cache.SecurityCacheEvict;

@Service
public class RoleService {

    @CacheEvict(value = "security", allEntries = true)
    public void updateRolePermissions(Long roleId, List<Long> permissionIds) {
        // 更新角色权限
        // SecurityCacheEvict 负责在权限变更时自动清理缓存
    }
}
```

---

## 配置文件

| 配置项                                    | 默认值  | 说明                       |
|-------------------------------------------|---------|----------------------------|
| `mxpio.security.token.secret`             | —       | Token 加密密钥             |
| `mxpio.security.token.expire`             | `86400` | Token 过期时间（秒）       |
| `mxpio.security.exclude-paths`            | —       | 无需认证的路径列表         |
| `mxpio.security.aes-key`                  | —       | AES 加密密钥               |
| `spring.security.filter.order`            | —       | 安全过滤器顺序             |

---

## 依赖关系

```
mxpio-security
├── mxpio-jpa
├── mxpio-common
├── spring-boot-starter-security 6.x
└── spring-boot-starter-web
```
