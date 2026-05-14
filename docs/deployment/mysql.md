# MySQL 配置与初始化

> mxpio-boot 使用 MySQL 8.0 作为主数据库，同时兼容达梦 DM8、人大金仓 Kingbase V8、南大通用 GaussDB。

---

## 1. 环境要求

| 数据库 | 版本要求 | 说明 |
|--------|----------|------|
| **MySQL** | 8.0+ | 主推数据库，建议 8.0.30+ |
| **达梦 DM8** | DM8 | 国产数据库，通过 JPA 方言兼容 |
| **人大金仓 Kingbase V8** | V8 | 国产数据库 |
| **南大通用 GaussDB** | 兼容版本 | 国产数据库 |

> 本项目基于 JPA 标准操作数据库，更换数据库只需修改 `spring.datasource` 配置和 JPA 方言。

---

## 2. 创建数据库

登录 MySQL 后执行：

```sql
CREATE DATABASE IF NOT EXISTS `mboot`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

> 使用 `utf8mb4` 字符集以支持完整的 Unicode（包括 Emoji）。

如需测试数据库：

```sql
CREATE DATABASE IF NOT EXISTS `mboot_test`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

---

## 3. 配置文件

### 3.1 开发环境配置

文件位置：`examples/mxpio-boot-example/src/main/resources/application-dev.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mboot?characterEncoding=utf-8&useSSL=true&nullCatalogMeansCurrent=true
    username: root
    password: mxp9827459
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
        format_sql: true

  sql:
    init:
      mode: always
```

### 3.2 配置项说明

| 配置项 | 值 | 说明 |
|--------|-----|------|
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/mboot` | 数据库连接地址，`mboot` 为数据库名 |
| `spring.datasource.url` 参数 | `characterEncoding=utf-8` | 字符编码 |
| | `useSSL=true` | 启用 SSL 连接 |
| | `nullCatalogMeansCurrent=true` | 解决 JPA 初始化时表空间扫描问题 |
| `spring.datasource.username` | `root` | 数据库用户名 |
| `spring.datasource.password` | `mxp9827459` | 数据库密码（请按实际修改） |
| `spring.datasource.driver-class-name` | `com.mysql.cj.jdbc.Driver` | MySQL 8.0 JDBC 驱动 |
| `spring.jpa.hibernate.ddl-auto` | `update` | 自动建表/更新表结构（开发模式推荐） |
| `spring.sql.init.mode` | `always` | 每次启动执行初始化 SQL |

### 3.3 生产环境配置

生产环境建议使用 `application-prod.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://prod-host:3306/mboot?characterEncoding=utf-8&useSSL=true&nullCatalogMeansCurrent=true&serverTimezone=Asia/Shanghai
    username: mboot_user
    password: ${MYSQL_PASSWORD}      # 从环境变量读取
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 300000
      connection-timeout: 20000

  jpa:
    hibernate:
      ddl-auto: validate    # 生产环境仅校验，不自动修改表结构
    show-sql: false

  sql:
    init:
      mode: never           # 生产环境禁用自动初始化
```

---

## 4. 数据初始化机制

### 4.1 JPA 自动建表

`ddl-auto: update` 模式下，Hibernate 会根据 `@Entity` 注解自动创建或更新数据库表结构。

> **注意**：生产环境建议使用 `ddl-auto: validate` 或 `none`，通过 Flyway 或 Liquibase 管理数据库版本。

### 4.2 SQL 脚本初始化

当 `spring.sql.init.mode=always` 时，Spring Boot 会在启动时自动加载 classpath 下的初始化脚本：

- `schema-${platform}.sql` — DDL 建表脚本（平台相关）
- `data-${platform}.sql` — DML 初始数据脚本（平台相关）

`platform` 的值由 `spring.sql.init.platform` 指定（默认 `mysql`）。因此文件名示例：

- `schema-mysql.sql`
- `data-mysql.sql`

> 这两个文件位于 `mxpio-boot-example/src/main/resources/` 目录下。

### 4.3 框架模块内置初始化数据

各框架模块自带 `data.sql` 文件，用于初始化业务所需的基础数据：

| 模块 | 初始化数据 | 用途 |
|------|-----------|------|
| **mxpio-security** | `data-mysql.sql` | 初始化菜单、权限、角色 |
| **mxpio-quartz** | Quartz 自带建表脚本 | 定时任务持久化表 |
| **mxpio-camunda** | Camunda 自带建表脚本 | 工作流引擎表 |
| **mxpio-excel** | `data-mysql.sql` | Excel 导出模板定义 |
| **mxpio-message** | `data-mysql.sql` | 消息模板、通道配置 |
| **mxpio-dbconsole** | `data-mysql.sql` | 数据库控制台配置 |

> 这些初始化脚本会随 `spring.sql.init.mode=always` 一同执行，因此**首次启动时数据库会自动完成初始化**，无需手动导入 SQL。

---

## 5. 切换数据库

### 5.1 达梦 DM8

```yaml
spring:
  datasource:
    url: jdbc:dm://localhost:5236/mboot
    username: SYSDBA
    password: ${DM_PASSWORD}
    driver-class-name: dm.jdbc.driver.DmDriver
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.DmDialect
```

### 5.2 人大金仓 Kingbase V8

```yaml
spring:
  datasource:
    url: jdbc:kingbase8://localhost:54321/mboot
    username: system
    password: ${KINGBASE_PASSWORD}
    driver-class-name: com.kingbase8.Driver
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.KingbaseSQLDialect
```

### 5.3 南大通用 GaussDB

```yaml
spring:
  datasource:
    url: jdbc:gaussdb://localhost:8000/mboot
    username: gaussdb
    password: ${GAUSSDB_PASSWORD}
    driver-class-name: com.huawei.gaussdb.jdbc.Driver
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.GaussDBDialect
```

---

## 6. 性能调优建议

### 连接池配置（HikariCP）

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20        # 最大连接数
      minimum-idle: 5              # 最小空闲连接数
      idle-timeout: 300000         # 空闲超时（毫秒）
      connection-timeout: 20000    # 连接超时（毫秒）
      max-lifetime: 1200000        # 连接最大存活时间（毫秒）
```

### MySQL 参数建议

```ini
# my.cnf 推荐配置
[mysqld]
max_connections = 200
character-set-server = utf8mb4
collation-server = utf8mb4_unicode_ci
innodb_buffer_pool_size = 2G    # 根据服务器内存调整，建议为物理内存的 60-70%
innodb_log_file_size = 512M
innodb_flush_log_at_trx_commit = 2  # 平衡性能与安全性
sql_mode = STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
```

---

## 7. 常见问题

### Q1: 启动报 "Table 'mboot.xxx' doesn't exist"

**原因**：`ddl-auto: update` 未能成功创建表。

**解决**：检查数据库用户是否有 `CREATE TABLE` 权限。如手动执行，可运行 `schema-mysql.sql`。

### Q2: 启动报 "Unknown column 'xxx' in 'field list'"

**原因**：实体类与表结构不一致，Hibernate 未能自动更新。

**解决**：检查 `ddl-auto` 是否为 `update`，或手动 ALTER TABLE。

### Q3: 多数据源

本项目目前使用单一数据源。如需多数据源，可参考 Spring Boot 的 `@Primary` + `DataSourceConfiguration` 配置。
