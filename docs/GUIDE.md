# mxpio-boot 完整使用指南

> 本文档面向有一定 Spring Boot 开发经验的 Java 后端开发者，
> 详细介绍 mxpio-boot 各模块的实际使用方法。

---

## 目录

1. [系统管理](#1-系统管理)
2. [权限体系](#2-权限体系)
3. [CRUD 快速开发](#3-crud-快速开发)
4. [工作流引擎](#4-工作流引擎)
5. [定时任务](#5-定时任务)
6. [操作日志](#6-操作日志)
7. [消息推送与通知](#7-消息推送与通知)
8. [缓存使用](#8-缓存使用)
9. [文件存储](#9-文件存储)
10. [第三方集成](#10-第三方集成)

---

## 1. 系统管理

mxpio-boot 提供了完整的系统管理功能，包括用户、角色、菜单、组织架构管理。所有功能通过 REST API 暴露，前端基于 Vue3 + Element Plus。

### 1.1 用户管理

用户数据存储在 `mb_user` 表中，核心字段：

| 字段 | 说明 | 备注 |
|------|------|------|
| username_ | 用户名 | 登录账号 |
| password_ | 密码 | 使用 `{bcrypt}` 加密存储 |
| nickname_ | 昵称 | 显示名 |
| administrator_ | 是否管理员 | 1=是，0=否 |
| enabled_ | 是否启用 | 1=启用，0=禁用 |

**关键 API：**

- `GET /user/list` — 分页查询用户列表
- `POST /user/add` — 新增用户
- `PUT /user/edit` — 编辑用户
- `DELETE /user/delete/{id}` — 删除用户

默认管理员账户：`admin` / `mxp9827459`（密码在 data.sql 中定义）。

### 1.2 角色管理

角色数据存储在 `mb_role` 表中，通过 `mb_role_granted_authority` 关联用户与角色。

**关键 API：**

- `GET /role/list` — 查询角色列表
- `POST /role/add` — 新增角色
- `PUT /role/edit` — 编辑角色
- `DELETE /role/delete/{id}` — 删除角色
- `GET /role/paging` — 分页查询角色

### 1.3 菜单管理

菜单数据存储在 `mb_url` 表中。菜单支持多级树形结构，节点通过 `parent_id_` 关联。

主要字段：

| 字段 | 说明 |
|------|------|
| name_ | 路由名称 |
| title_ | 菜单标题 |
| path_ | 前端路由路径 |
| component_ | 前端组件路径 |
| icon_ | 图标 |
| parent_id_ | 父级菜单 ID |
| order_ | 排序号 |
| navigable_ | 是否导航可见 |
| outside_ | 是否外链 |

**关键 API：**

- `GET /resource/url/list` — 获取 URL 资源树
- `POST /resource/url/add` — 新增菜单
- `PUT /resource/url/edit` — 编辑菜单
- `DELETE /resource/url/delete/{id}` — 删除菜单

### 1.4 组织管理

系统支持部门（Dept）和岗位（Post）两级组织架构。

- **部门管理**: `GET /dept/list`，`POST /dept/add` 等
- **岗位管理**: `GET /post/list`，包含岗位类型管理

---

## 2. 权限体系

mxpio-boot 采用五层 RBAC（基于角色的访问控制）权限模型：

```
用户 → 部门/岗位 → 角色 → 权限 → 资源
                              ├─ URL（菜单/页面）
                              ├─ Element（按钮/操作）
                              └─ Data（数据范围）
```

### 2.1 权限核心架构

权限系统基于 Spring Security 6.x 的 lambda DSL 配置，通过 `SecurityConfiguration` 自动装配。

**密码编码**：使用 `PasswordEncoderFactories.createDelegatingPasswordEncoder()`，默认采用 `{bcrypt}` 前缀格式。

### 2.2 URL 权限

URL 资源对应 `mb_url` 表中的菜单/页面记录。系统通过 `UrlFilterConfigAttributeProvider` 将 URL 与需要的角色关联。

当用户访问一个 URL 时，`GrantedAuthority` 列表会与 URL 的授权要求进行匹配，不匹配则返回 403。

### 2.3 按钮权限（Element）

按钮/操作层面的细粒度权限。按钮资源存储在 `mb_element` 表中，通过 `ElementProviderImpl` 和 `ElementConfigAttributeProviderImpl` 实现权限校验。

前端可以根据用户的 element 权限动态显示/隐藏按钮：

```json
// 用户拥有的按钮权限列表示例
["user:add", "user:edit", "user:delete", "role:add"]
```

### 2.4 数据权限

数据权限通过 `DataScapeProvider` / `DataScapeContext` 实现。允许对同一角色的不同用户设置不同的数据可见范围（例如：仅查看本部门数据）。

通过 `DataFilterController` 管理角色的数据过滤规则。

### 2.5 扩展权限

系统提供了 `GrantedAuthorityProvider` 扩展点，允许自定义权限来源：

- `UserGrantedAuthorityProvider` — 用户级别权限
- `DeptGrantedAuthorityProvider` — 部门级别权限
- `PostGrantedAuthorityProvider` — 岗位级别权限

可自行实现接口添加新的权限提供者。

---

## 3. CRUD 快速开发

mxpio-boot 通过 `mxpio-jpa` 模块提供了 Linq（Language Integrated Query，语言集成查询），大幅简化 JPA 数据访问代码。

### 3.1 基础查询

使用 `Linq` 进行查询：

```java
@Autowired
private Linq linq;

// 查询所有用户
List<User> users = linq.from(User.class).list();

// 条件查询：查询用户名为 admin 的用户
User admin = linq.from(User.class)
    .where(SimpleCriterion.eq("username", "admin"))
    .findOne();
```

### 3.2 Criteria 条件构建

mxpio-boot 提供了 `Criteria`、`SimpleCriterion`、`Junction` 等查询条件构建器：

```java
// 构建查询条件
Criteria criteria = new Criteria();
criteria.addCriterion(new SimpleCriterion("username", Operator.LIKE, "admin"));
criteria.addCriterion(new SimpleCriterion("status", Operator.EQ, 1));

// 使用条件查询
List<User> users = linq.from(User.class)
    .where(criteria)
    .list();

// 复杂条件：AND + OR 组合
Junction junction = new Junction(JunctionType.AND);
junction.add(new SimpleCriterion("deptId", Operator.EQ, deptId));
Junction orJunction = new Junction(JunctionType.OR);
orJunction.add(new SimpleCriterion("status", Operator.EQ, 1));
orJunction.add(new SimpleCriterion("status", Operator.EQ, 2));
junction.add(orJunctionhed);

criteria.addCriterion(junction);
```

**支持的运算符：**

| 运算符 | 说明 |
|--------|------|
| EQ | 等于 |
| NE | 不等于 |
| LIKE | 模糊匹配（%value%） |
| LIKE_END | 结尾匹配（%value） |
| LIKE_START | 开头匹配（value%） |
| NOT_LIKE | 不匹配 |
| GT | 大于 |
| LT | 小于 |
| GE | 大于等于 |
| LE | 小于等于 |
| IN | IN 查询 |
| NOT_IN | NOT IN 查询 |
| IS_NULL | 为空判断 |
| IS_NOT_NULL | 非空判断 |

### 3.3 Lambda 属性引用

使用 Lambda 表达式代替字符串属性名，编译期安全：

```java
linq.from(User.class)
    .where(SimpleCriterion.eq(User::getUsername, "admin"))
    .list();
```

### 3.4 分页查询

```java
Pageable pageable = PageRequest.of(0, 10); // 第1页，每页10条
Page<User> page = linq.from(User.class)
    .where(criteria)
    .paging(pageable);

// 获取分页数据
List<User> users = page.getContent();
long total = page.getTotalElements();
```

### 3.5 N+1 查询优化

使用 `collect()` 避免 N+1 问题：

```java
// 自动收集关联数据
List<Order> orders = linq.from(Order.class)
    .collect(Order::getUserId, User.class)
    .collect("orderItems", OrderItem.class)
    .list();
// orders 中的 userId 关联的 User 信息会自动回填
```

### 3.6 增删改操作

```java
// 新增
User user = new User();
user.setUsername("newuser");
linq.persist(user);

// 更新
Lindu lindu = linq.update(User.class)
    .set("nickname", "新昵称")
    .where(SimpleCriterion.eq("id", userId))
    .done();

// 删除
Lind lind = linq.delete(User.class)
    .where(SimpleCriterion.eq("id", userId))
    .done();
```

### 3.7 结果转换

```java
// 转 Map
List<Map<String, Object>> maps = linq.from(User.class)
    .select("id", "username", "nickname")
    .aliasToMap()
    .list();

// 转指定 Bean 类型
List<UserVO> vos = linq.from(User.class)
    .select("id", "username", "nickname")
    .aliasToBean(UserVO.class)
    .list();
```

---

## 4. 工作流引擎

mxpio-boot 集成了 Camunda 7.24.0 工作流引擎（通过 `mxpio-camunda` 模块适配），提供完整的 BPMN 2.0 流程管理能力。

### 4.1 配置

```yaml
camunda:
  bpm:
    database:
      type: mysql
      schema-update: true
    history-level: FULL
    auto-deployment-enabled: false
    eventing:
      execution: true
      history: true
      task: true
```

> `auto-deployment-enabled: false` 表示不自动部署 resources 下的 BPMN 文件，需要通过 API 手动部署。

### 4.2 流程定义部署

通过 `ProcessController` 提供的 API 部署 BPMN 流程定义：

```http
POST /camunda/process/deploy
Content-Type: multipart/form-data

bpmnFile: @leave-process.bpmn
```

### 4.3 启动流程实例

```http
POST /camunda/process/start
Content-Type: application/json

{
  "processDefinitionKey": "leave_process",
  "businessKey": "BIZ-2025-001",
  "variables": {
    "applicant": "张三",
    "days": 3
  }
}
```

### 4.4 任务审批

查询待办任务：

```http
GET /camunda/task/myPendingTask?assignee=zhangsan
```

办理任务：

```http
POST /camunda/task/complete
Content-Type: application/json

{
  "taskId": "abc123",
  "variables": {
    "approved": true,
    "comment": "同意"
  }
}
```

### 4.5 流程跟踪

查询已办任务：

```http
GET /camunda/task/myFinishedTask?assignee=zhangsan
```

查询流程流转记录（通过 `FlowController`）：

```http
GET /camunda/flow/list?processInstanceId=xxx
```

### 4.6 表单模型

`mxpio-camunda` 提供了 `FormModelDef` 和 `FormModel` 实体用于管理任务表单。`FormModelController` 提供表单模型的 CRUD API。

### 4.7 全局事件监听

系统通过 `CamundaGlobalListenerPlugin` 注册全局流程事件监听器，可自定义 `CamundaGlobalListener` 实现业务逻辑。

---

## 5. 定时任务

`mxpio-quartz` 模块基于 Quartz 提供持久化定时任务管理。

### 5.1 创建任务类

```java
@Component
public class MyTaskService {

    public void syncData() {
        // 定时执行的业务逻辑
        System.out.println("数据同步执行时间：" + LocalDateTime.now());
    }

    public void sendNotification(String param) {
        // 带参数的任务
        System.out.println("发送通知：" + param);
    }
}
```

### 5.2 注册定时任务

通过 `QuartzController` 的 REST API 注册：

```http
POST /quartz/job/add
Content-Type: application/json

{
  "jobClassName": "com.example.MyTaskService",
  "jobMethodName": "syncData",
  "jobType": "SpringBean",
  "cronExpression": "0 0 2 * * ?",
  "description": "每日凌晨2点同步数据"
}
```

**任务类型说明：**

| 类型 | 说明 |
|------|------|
| `SpringBean` | 从 Spring 容器获取 Bean 实例执行方法 |
| `Class` | 通过 `Class.forName()` 反射创建实例执行 |

### 5.3 任务管理 API

```http
GET  /quartz/job/paging          # 分页查询任务列表
PUT  /quartz/job/edit            # 编辑任务
POST /quartz/job/pause/{id}      # 暂停任务
POST /quartz/job/resume/{id}     # 恢复任务
POST /quartz/job/run/{id}        # 立即执行一次
DELETE /quartz/job/delete/{id}   # 删除任务
```

### 5.4 Quartz 集群

配置中已启用 Quartz 集群模式（`isClustered: true`），多实例部署时可保证任务不重复执行。相关配置：

```yaml
spring.quartz.properties.org.quartz.jobStore.isClustered: true
spring.quartz.properties.org.quartz.jobStore.clusterCheckinInterval: 10000
```

---

## 6. 操作日志

`mxpio-log` 模块基于 `bizlog-sdk` 提供声明式业务操作日志记录。

### 6.1 启用

模块通过 `@EnableLogRecord(tenant="com.mxpio")` 自动启用日志功能。

### 6.2 使用 @LogRecord 注解

在 Service 方法上添加注解即可自动记录操作日志：

```java
@Service
public class UserService {

    @LogRecord(
        success = "{{#userName}} 创建了新用户 {{#user.nickname}}",
        type = "USER",            // 日志类型
        bizNo = "{{#user.id}}",   // 业务主键
        extra = "{{#user.username}}"  // 额外信息
    )
    public User createUser(User user) {
        // 创建用户逻辑
        return user;
    }
}
```

### 6.3 日志查询

日志存储到 `DBMxpioLog` 表中，通过 `MxpioLogController` 查询：

```http
GET /log/listPage?page=0&size=10&type=USER
```

### 6.4 存储方式

`StorageProvider` 接口定义了日志存储行为：

| 实现类 | 说明 |
|--------|------|
| `DatabaseStorageProvider` | 默认实现，日志存到数据库 |
| `ElasticsearchStorageProvider` | 可选的 ES 存储实现 |

日志记录通过 `@EnableAsync` 异步执行，不影响主业务流程。

---

## 7. 消息推送与通知

mxpio-boot 提供了两套消息机制：**消息渠道**（`mxpio-message`）用于抽象消息发送，**WebSocket**（`mxpio-websocket`）用于实时推送。

### 7.1 消息渠道

`MessageChannel` 接口定义了统一的消息发送规范：

```java
// 获取消息渠道实现
@Autowired
private MessageChannel innerChannel; // 内部消息渠道

// 发送消息
innerChannel.send(
    "admin",                          // 发送人
    new String[]{"user1", "user2"},   // 接收人数组
    "系统通知",                       // 标题
    "您的审批任务待处理",             // 内容
    "BIZ-001"                         // 业务主键
);
```

**支持的渠道：**

| 渠道 | 类 | 说明 |
|------|-----|------|
| 内部消息 | `InnerMessageChannel` | 站内信，存入 message 表 |
| 钉钉 | `WorkMessageChannel` | 钉钉工作通知 |
| 企微 | `CpMessageChannel` | 企业微信消息 |
| 邮件 | `EmailMessageChannel` | 邮件发送 |

### 7.2 消息查询

```http
# 查询我的消息（分页）
GET /message/myMessagePaged?page=0&size=10

# 查询未读消息
GET /message/myUnreadPaged?page=0&size=10

# 标记已读
POST /message/read/{msgId}

# 全部已读
POST /message/readAll
```

### 7.3 WebSocket 实时推送

系统内置 WebSocket 支持，通过 `MxpioWebSocketManager` 管理连接：

```java
@Autowired
private MxpioWebSocketManager webSocketManager;

// 向指定用户发送消息
webSocketManager.send("userId123", "您有新的审批任务");

// 向指定端点的用户发送消息
webSocketManager.sendToEndpoint("endpointName", "userId123", "消息内容");

// 广播消息（所有连接）
webSocketManager.broadcast("系统维护通知：今晚22:00-23:00升级");

// 向端点广播
webSocketManager.broadcastToEndpoint("endpointName", "通知内容");
```

**后端模式选择：**

| 模式 | 实现类 | 适用场景 |
|------|--------|----------|
| 内存模式 | `InMemoryMxpioWebSocketManagerImpl` | 单节点部署 |
| Redis 模式 | `RedisMxpioWebSocketManagerImpl` | 多节点集群部署 |

---

## 8. 缓存使用

`mxpio-cache` 模块提供了统一的缓存抽象层和便捷的 API。

### 8.1 CacheProvider API

```java
@Autowired
private CacheProvider cacheProvider;

// 写入缓存
cacheProvider.set("user:1001", userData);

// 写入缓存（带过期时间）
cacheProvider.set("session:token_abc", sessionData, 30, TimeUnit.MINUTES);

// 读取缓存
Object data = cacheProvider.get("user:1001");

// 判断是否存在
boolean exists = cacheProvider.hasKey("user:1001");

// 删除缓存
cacheProvider.del("user:1001");
cacheProvider.del("key1", "key2", "key3"); // 批量删除
```

### 8.2 Spring Cache 注解

也支持标准的 Spring Cache 注解：

```java
@Service
public class UserService {

    @Cacheable(value = "users", key = "#id")
    public User findById(String id) {
        // 方法结果会被缓存
        return userRepository.findById(id).orElse(null);
    }

    @CacheEvict(value = "users", key = "#user.id")
    public void update(User user) {
        // 更新后清除缓存
        userRepository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void clearAll() {
        // 清除所有用户缓存
    }
}
```

### 8.3 缓存实现选择

| 实现 | 模块 | 说明 |
|------|------|------|
| Redis | `mxpio-cache-redis` | 分布式缓存，支持集群 |
| Caffeine | `mxpio-cache-caffeine` | 本地缓存，性能极高 |

引入对应 Maven 依赖即可自动启用。同时引入两者可实现多级缓存。

---

## 9. 文件存储

`mxpio-filestorage` 模块提供文件存储服务，支持文件上传和下载。

```http
# 上传文件
POST /file/upload
Content-Type: multipart/form-data

file: @document.pdf

# 响应
{
  "id": "file-uuid-xxx",
  "fileName": "document.pdf",
  "fileSize": 1024000,
  "fileType": "application/pdf",
  "storePath": "2025/01/document-uuid.pdf"
}

# 下载文件
GET /file/download/{fileId}

# 查看文件列表
GET /file/list?page=0&size=20
```

---

## 10. 第三方集成

### 10.1 钉钉通知

`mxpio-dingtalk` 模块集成钉钉工作通知能力：

```yaml
# application-dev.yml
dingtalk:
  app-key: your-app-key
  app-secret: your-app-secret
  agent-id: your-agent-id
```

发送钉钉消息：

```java
@Autowired
@Qualifier("workMessageChannel")
private MessageChannel dingTalkChannel;

dingTalkChannel.send("system", new String[]{"userId123"}, "审批通知", "您有一个审批待处理", "BIZ-001");
```

### 10.2 企业微信

`mxpio-wechat` 模块提供企业微信（WeChat Work）集成，支持：

- 企业微信回调事件处理（`WxPortalController`）
- 应用消息推送（`CpMessageChannel`）
- 联系人变更事件处理

### 10.3 邮件服务

`mxpio-email` 模块提供邮件发送：

```yaml
spring:
  mail:
    host: smtp.example.com
    port: 587
    username: your-email@example.com
    password: your-password
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true
```

```java
@Autowired
@Qualifier("emailMessageChannel")
private MessageChannel emailChannel;

emailChannel.send("system", new String[]{"user@example.com"}, "邮件主题", "邮件内容", "BIZ-001");
```

### 10.4 Microsoft 身份认证

`mxpio-msal` 模块基于 Microsoft MSAL 提供 Azure AD 身份认证集成，支持企业 Office 365 账户登录。

### 10.5 OAuth2 认证

`mxpio-oauth` 模块提供 OAuth2 认证授权支持，可用于对接第三方登录。

---

## 附录：模块 Maven 坐标

所有模块版本由 `mxpio-dependencies` BOM 统一管理，使用时无需指定版本：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.mxpio</groupId>
            <artifactId>mxpio-dependencies</artifactId>
            <version>4.0.0-beta.1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

引入具体模块示例：

```xml
<!-- JPA 扩展 -->
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-jpa</artifactId>
</dependency>

<!-- 权限 -->
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-security</artifactId>
</dependency>

<!-- 缓存 -->
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-cache</artifactId>
</dependency>
```

---

## 11. 配置体系说明

mxpio-boot 的配置采用**模块级默认 + 应用级覆盖**的分层机制：

```
应用级覆盖 (mxpio-boot-webapp/mxpio.properties)    ← 最高优先级
           ↓
模块级默认 (各模块 mxpio/mxpio.properties)
           ↓
application.yml / application-mysql.yml     ← 最低优先级
```

### 11.1 配置文件位置

每个核心模块在 `src/main/resources/mxpio/` 下都有自己的 `mxpio.properties`：

| 模块 | 配置文件 | 配置内容 |
|------|---------|---------|
| mxpio-common | `framework/mxpio-common/.../mxpio.properties` | Swagger、应用信息 |
| mxpio-security | `framework/mxpio-security/.../mxpio.properties` | 登录、验证码、密码策略、Token、权限 |
| mxpio-system | `framework/mxpio-system/.../mxpio.properties` | 代码生成路径、资源文件 |
| mxpio-multitenant | `framework/mxpio-multitenant/.../mxpio.properties` | 多租户数据库配置 |
| mxpio-excel | `framework/mxpio-excel/.../mxpio.properties` | 导出设置、CSV 配置 |
| mxpio-filestorage | `framework/mxpio-filestorage/.../mxpio.properties` | 文件存储路径、存储提供商 |
| mxpio-dingtalk | `modules/mxpio-dingtalk/.../mxpio.properties` | 钉钉 AppKey/Secret/AgentId |

### 11.2 应用级覆盖

`mxpio-boot-webapp` 中的 `mxpio.properties` 可以覆盖任何模块的默认配置。例如，
security 模块默认开启了密码过期和验证码，但 webapp 将其关闭：

```properties
# mxpio-boot-app/mxpio-boot-webapp/src/main/resources/mxpio/mxpio.properties
mxpio.password.expiredswitch=off
mxpio.captcha.open=false
```

### 11.3 通过 application.yml 覆盖

所有 `mxpio.*` 配置项也支持通过 Spring Boot 标准配置文件覆盖：

```yaml
mxpio:
  appName: 我的生产管理系统
  swagger:
    title: 生产管理 API
  password:
    expiredswitch: on
    expireddays: 90
  captcha:
    open: true
```

> 📖 完整配置项清单参见 [config.md](./modules/config.md)。
