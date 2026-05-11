# mxpio-boot 快速开始指南

> 5 分钟上手，完成从环境准备到启动验证的全流程。

---

## 1. 环境要求

| 组件 | 最低版本 | 说明 |
|------|----------|------|
| **JDK** | 25 | 本项目基于 JDK 25 构建，请使用 OpenJDK 或 Oracle JDK 25+ |
| **Maven** | 3.8+ | 构建工具，建议使用最新稳定版 |
| **MySQL** | 8.0+ | 数据库，需支持 MySQL 8.0 及以上 |
| **Redis** | 7.x | 缓存与会话存储，需 7.x 版本 |
| **Git** | 任意 | 克隆代码仓库 |

确保上述组件已正确安装并加入系统 PATH。

---

## 2. 克隆与构建

```bash
# 克隆项目
git clone <项目仓库地址>
cd mxpio-boot

# 编译全部模块（跳过测试以加速）
mvn clean install -DskipTests -T 4
```

构建成功后，你会看到 `BUILD SUCCESS` 输出。

> 💡 `-T 4` 表示使用 4 线程并行编译，可根据机器 CPU 核心数调整。
> 首次构建需下载大量依赖，耗时取决于网络状况。

---

## 3. 数据库准备

### 3.1 创建数据库

登录 MySQL 并创建数据库：

```sql
CREATE DATABASE IF NOT EXISTS `mboot` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3.2 导入初始数据（可选）

应用启动时会自动执行 classpath 下的 `schema-mysql.sql` 和 `data-mysql.sql`（由 `spring.sql.init` 控制），**无需手动导入**。

---

## 4. 配置

找到配置文件 `mxpio-boot-app/mxpio-webapp/src/main/resources/application-dev.yml`，根据你的环境修改关键配置：

```yaml
server:
  port: 9005                    # 服务端口

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mboot?useUnicode=true&characterEncoding=utf8mb4&serverTimezone=Asia/Shanghai
    username: root
    password: mxp9827459

  jpa:
    hibernate:
      ddl-auto: update          # 自动建表/更新表结构
    show-sql: true              # 控制台打印 SQL（开发时建议开启）

  data:
    redis:
      host: localhost
      port: 6379

  sql:
    init:
      mode: always              # 启动时执行 schema.sql / data.sql

camunda:
  bpm:
    database:
      type: mysql
      schema-update: true
      history-level: FULL
    auto-deployment-enabled: false

quartz:
  job-store-type: jdbc
  properties:
    org.quartz.jobStore:
      tablePrefix: QRTZ_
```

> 🔧 如果 MySQL 或 Redis 不在本机，请相应修改 `localhost` 为实际地址及端口。

---

## 5. 启动与验证

### 启动应用

```bash
# 在项目根目录执行
mvn spring-boot:run -pl mxpio-boot-app/mxpio-webapp -am
```

或打包后启动：

```bash
mvn clean package -DskipTests -pl mxpio-boot-app/mxpio-webapp -am
java -jar mxpio-boot-app/mxpio-webapp/target/mxpio-webapp-*.jar
```

启动日志末尾应看到：

```
2025-xx-xx xx:xx:xx  INFO  --- [main] com.mxpio.webapp.MBootApplication    : Started MBootApplication in X.XXX seconds
```

### 验证

打开浏览器访问 Swagger 接口文档：

```
http://localhost:9005/swagger-ui.html
```

如果看到 Swagger UI 页面并列出所有 API 端点，说明启动成功 🎉

---

## 常见问题

### Q1: 启动时报 "Access denied for user 'root'@'...'"

**原因**：数据库用户名或密码配置错误。

**解决**：检查 `application-dev.yml` 中的 `spring.datasource.password` 是否与你的 MySQL 密码一致。

---

### Q2: 报 "Unknown database 'mboot'"

**原因**：`mboot` 数据库未创建。

**解决**：执行第 3 步中的 `CREATE DATABASE` 语句。

---

### Q3: 控制台打印大量 SQL，影响查看

**原因**：`show-sql: true` 开启了 JPA SQL 日志。

**解决**：将配置改为 `show-sql: false` 并重启。

---

### Q4: 端口 9005 被占用

**原因**：其他进程占用了该端口。

**解决**：修改 `server.port` 为其他可用端口（如 9006），或终止占用进程：

```bash
# Linux / macOS
lsof -i :9005
kill -9 <PID>
```

---

### Q5: Redis 连接失败 ("Connection refused")

**原因**：Redis 服务未启动或地址/端口配置错误。

**解决**：

```bash
# 检查 Redis 是否在运行
redis-cli ping
# 应返回 PONG
```

如未启动：`redis-server`（确保配置文件加载正确）。

---

### Q6: Maven 构建时下载依赖过慢

**建议**：为 Maven 配置国内镜像源，在 `~/.m2/settings.xml` 中添加：

```xml
<mirror>
  <id>aliyun</id>
  <url>https://maven.aliyun.com/repository/public</url>
  <mirrorOf>central</mirrorOf>
</mirror>
```

---

### Q7: 启动后 Swagger 页面白屏或 404

**原因**：`context-path` 如果配置了非空值，路径会发生变化。

**解决**：当前配置 `context-path` 为空，直接访问 `/swagger-ui.html` 即可。如果修改过 `context-path`（例如 `/api`），则访问 `http://localhost:9005/api/swagger-ui.html`。

---

### Q8: 启动时执行 data-mysql.sql 报主键冲突

**原因**：多次启动导致重复数据插入。

**解决**：将 `spring.sql.init.mode` 从 `always` 改为 `embedded`（仅在内存数据库时执行），或在首次启动后切换为 `never`。

---

## 下一步

- 阅读项目 README 了解整体架构
- 参考 API 文档探索各模块功能
- 通过 Swagger UI 在线调试接口

如有其他问题，欢迎提交 Issue 或联系项目维护团队。
