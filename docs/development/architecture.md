# 架构概览

> mxpio-boot 是 Spring Boot 4.0 + JDK 25 的企业级快速开发框架。

---

## 模块依赖图

```
mxpio-boot-parent (com.mxpio:mxpio-boot-parent:4.0.0-beta.1)
│   parent: spring-boot-starter-parent:4.0.1
│
├── mxpio-dependencies (BOM — 统一版本管理)
│
├── mxpio-autoconfigure (自动配置聚合入口)
│
├── mxpio-framework/                      [核心框架层]
│   ├── mxpio-common                      ← 公共常量、工具类
│   ├── mxpio-jpa                         ← JPA 增强：Linq/Criteria
│   ├── mxpio-security                    ← RBAC 权限体系
│   ├── mxpio-cache                       ← 缓存抽象接口
│   ├── mxpio-cache-redis                 ← Redis 缓存实现
│   ├── mxpio-cache-caffeine              ← Caffeine 本地缓存
│   ├── mxpio-quartz                      ← Quartz 定时任务
│   ├── mxpio-log                         ← 操作日志审计
│   ├── mxpio-camunda                     ← Camunda 工作流适配
│   ├── mxpio-websocket                   ← WebSocket 实时推送
│   ├── mxpio-message                     ← 统一消息渠道
│   ├── mxpio-multitenant                 ← 多租户支持
│   ├── mxpio-filestorage                 ← 文件存储服务
│   ├── mxpio-excel                       ← Excel 导入导出
│   ├── mxpio-expression                  ← 表达式引擎
│   ├── mxpio-dbconsole                   ← 数据库管理控制台
│   └── mxpio-datav                       ← 数据可视化
│
├── mxpio-boot-modules/                   [业务集成层]
│   ├── mxpio-dingtalk                    ← 钉钉集成
│   ├── mxpio-wechat                      ← 企业微信集成
│   ├── mxpio-email                       ← 邮件发送
│   ├── mxpio-msal                        ← Microsoft 身份认证
│   └── mxpio-oauth                       ← OAuth2 认证授权
│
└── examples/                              [示例工程，非 Maven 模块]
    └── mxpio-boot-example
```

## 分层架构

```
┌──────────────────────────────────────────────────────┐
│                  Controller Layer                     │
│  REST API 控制器（@RestController）                    │
│  负责参数校验、响应封装                               │
├──────────────────────────────────────────────────────┤
│                  Service Layer                         │
│  业务逻辑层（@Service）                               │
│  事务管理、权限校验、日志记录                           │
├──────────────────────────────────────────────────────┤
│                  JPA / Repository Layer                │
│  Linq 查询封装、Entity 管理                            │
│  QueryDSL、Criteria API                               │
├──────────────────────────────────────────────────────┤
│                  Data Access Layer                     │
│  MySQL / Redis / Camunda / Quartz ...                 │
└──────────────────────────────────────────────────────┘
```

## 自动配置机制

mxpio-boot 通过 Spring Boot 的 `AutoConfiguration.imports` 机制实现模块自动装配。

每个框架模块的配置类结构：

```java
@Configuration
@AutoConfigurationPackage          // 注册当前包到扫描范围
@ComponentScan                     // 扫描本模块的 @Component
public class XxxConfiguration {
    // 定义 @Bean
}
```

所有模块的自动配置在 `mxpio-autoconfigure` 中集中注册：

```
META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

该文件列出了 24 个 AutoConfiguration 类，包括：

```
com.mxpioframework.autoconfigure.common.CommonAutoConfiguration
com.mxpioframework.autoconfigure.jpa.LinqAutoConfiguration
com.mxpioframework.autoconfigure.security.SecurityAutoConfiguration
com.mxpioframework.autoconfigure.cache.CacheAutoConfiguration
com.mxpioframework.autoconfigure.quartz.QuartzAutoConfiguration
com.mxpioframework.autoconfigure.camunda.CamundaAutoConfiguration
com.mxpioframework.autoconfigure.log.LogAutoConfiguration
com.mxpioframework.autoconfigure.websocket.WebSocketAutoConfiguration
com.mxpioframework.autoconfigure.message.MessageAutoConfiguration
com.mxpioframework.autoconfigure.filestorage.FilestorageAutoConfiguration
... 以及其他模块的配置
```

> `@SpringBootApplication` 扫描 `com.mxpio.webapp` 包。各模块通过 `@AutoConfigurationPackage` 将自身包注册到组件扫描范围。

## 启动流程

```
SpringApplication.run(MBootApplication.class)
    │
    ├── 1. 加载 application.yml / application-dev.yml
    ├── 2. 读取 AutoConfiguration.imports
    ├── 3. 初始化各模块 Configuration
    ├── 4. JPA Entity 扫描 → 自动建表（ddl-auto=update）
    ├── 5. 执行 schema-{platform}.sql / data-{platform}.sql
    ├── 6. Camunda 引擎初始化
    ├── 7. Quartz 调度器初始化
    ├── 8. WebSocket 端点注册
    └── 9. 应用启动完成
```

## 数据流示例：用户登录

```
用户 → POST /login
    → Security Filter Chain
        → JwtAuthenticationProvider (用户名密码校验)
        → PasswordEncoder (密码匹配)
    → 查询用户角色、权限
    → 生成 Token（JWT/自定义）
    → 返回给前端
```

## 技术栈速览

| 层次 | 技术选型 |
|------|----------|
| 框架基础 | Spring Boot 4.0.1, JDK 25 |
| 数据持久化 | Spring Data JPA + QueryDSL |
| 安全框架 | Spring Security 6.x (lambda DSL) |
| 工作流引擎 | Camunda 7.24.0 |
| 缓存 | Redis 7.x / Caffeine 3.2.3 |
| 定时任务 | Quartz 2.5.2 (JDBC 持久化) |
| 数据库 | MySQL 8.0 (兼容达梦、人大金仓、南大通用) |
| 消息 | 内置消息渠道 + WebSocket |
| API 文档 | SpringDoc 3.0.2 + Knife4j 3.0.3 |
| 构建工具 | Maven 3.8+ (BOM 版本管理) |
