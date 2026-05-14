# MxpIO Boot

[简体中文](./README.md) | English

> An enterprise-grade low-code rapid development framework based on Spring Boot 4.0, integrating commonly used enterprise features and components out of the box.

[![License](https://img.shields.io/github/license/J-cafe/mxpio-boot)](https://gitee.com/i_mxpio/mxpio-boot/blob/master/LICENSE)
[![Version](https://img.shields.io/badge/version-4.0.0--beta.1-blue)](https://gitee.com/i_mxpio/mxpio-boot)

---

## 1. Tech Stack

### Backend

| Technology | Version / Notes |
|------------|-----------------|
| JDK | 25 |
| Spring Boot | 4.0.1 |
| Spring Data JPA | Hibernate dialects, supports MySQL / Dameng / Kingbase / GaussDB |
| Spring Security | Lambda DSL, OAuth2 Resource Server |
| Spring Cache | Caffeine (local) / Redis (distributed) |
| Camunda | 7.24.0 Workflow Engine |
| Quartz | JDBC-persisted Job Scheduler |
| Apache POI | 5.5.1 Excel Import/Export |
| Aviator | 4.2.7 Expression Engine |
| MinIO | 8.6.0 File Storage |
| Druid | 1.2.27 Connection Pool |
| SpringDoc | 3.0.2 + Knife4j 3.0.3 API Docs |
| Lombok | 1.18.42 |

### Middleware

- **Database:** MySQL 8.0+ (also compatible with Dameng DM8, Kingbase V8, GaussDB)
- **Cache:** Redis 7.x

### Frontend

Based on [Vue Vben Admin](https://github.com/vbenjs/vue-vben-admin) monorepo:

- Vue 3 + TypeScript + Vite
- UnoCSS / Ant Design Vue
- pnpm + TurboRepo monorepo
- Built-in business modules: System, Inventory, Purchase, Sales, Quality, Equipment, Planning, etc.

---

## 2. Module Structure

```
mxpio-boot/
├── pom.xml                            ← Root POM
├── mxpio-dependencies/                ← BOM (unified version management)
├── mxpio-framework/                   ← Core Framework Layer
│   ├── mxpio-autoconfigure/           ← Auto-configuration Hub
│   ├── mxpio-common/                  ← Common Utilities
│   ├── mxpio-jpa/                     ← JPA Enhancement (Linq-style queries)
│   ├── mxpio-cache/                   ← Cache Abstraction
│   ├── mxpio-cache-redis/             ← Redis Cache Implementation
│   ├── mxpio-cache-caffeine/          ← Caffeine Local Cache
│   ├── mxpio-security/                ← RBAC (URL/Button/Data permissions)
│   ├── mxpio-system/                  ← System Management (Dept, Role, User, Menu, Dict)
│   ├── mxpio-quartz/                  ← Quartz Job Management
│   ├── mxpio-camunda/                 ← Camunda Workflow Adapter
│   ├── mxpio-log/                     ← Audit Logging
│   ├── mxpio-expression/              ← Aviator Expression Engine
│   ├── mxpio-filestorage/             ← File Storage (MinIO/Local)
│   ├── mxpio-multitenant/             ← Multi-tenancy
│   ├── mxpio-dbconsole/               ← Cloud DB Console
│   ├── mxpio-message/                 ← Message Notification Center
│   ├── mxpio-websocket/               ← WebSocket Real-time Push
│   ├── mxpio-excel/                   ← Excel Import/Export
│   └── mxpio-datav/                   ← Data Visualization
├── mxpio-boot-modules/                ← Third-party Integrations
│   ├── mxpio-dingtalk/                ← DingTalk
│   ├── mxpio-wechat/                  ← WeCom
│   ├── mxpio-email/                   ← Email
│   ├── mxpio-msal/                    ← Microsoft Authentication
│   └── mxpio-oauth/                   ← OAuth2 Client
├── mxpio-ui/                     ← Frontend Monorepo
└── examples/                          ← Examples (not a Maven module)
    └── mxpio-boot-example/
```

### Layered Build

```bash
# Build framework layer only
mvn -pl mxpio-framework clean install -DskipTests

# Build integration modules only
mvn -pl mxpio-boot-modules clean install -DskipTests

# Full build
mvn clean install -DskipTests
```

---

## 3. Quick Start

### Prerequisites

| Component | Required Version |
|-----------|-----------------|
| JDK | 25 |
| Maven | 3.8+ |
| MySQL | 8.0+ |
| Redis | 7.x |

### 3.1 Run from Source

```bash
# Clone
git clone https://gitee.com/i_mxpio/mxpio-boot.git
cd mxpio-boot

# Create database
# Run in MySQL: CREATE DATABASE IF NOT EXISTS `mboot` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Edit config (database password, etc.)
# Edit examples/mxpio-boot-example/src/main/resources/application-dev.yml

# Build framework and start example
mvn clean install -DskipTests
cd examples/mxpio-boot-example && mvn spring-boot:run
```

After startup:
- App: `http://localhost:9005`
- API Docs: `http://localhost:9005/swagger-ui.html`
- Default account: `admin` / `123456` (auto-created on first run)

### 3.2 Use as a Dependency

Extend `mxpio-boot-parent` in your Spring Boot project and add modules as needed:

```xml
<parent>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-boot-parent</artifactId>
    <version>4.0.0-beta.1</version>
</parent>

<dependencies>
    <!-- Auto-configuration (required) -->
    <dependency>
        <groupId>com.mxpio</groupId>
        <artifactId>mxpio-autoconfigure</artifactId>
    </dependency>
    <!-- Add feature modules as needed -->
    <dependency>
        <groupId>com.mxpio</groupId>
        <artifactId>mxpio-security</artifactId>
    </dependency>
    <dependency>
        <groupId>com.mxpio</groupId>
        <artifactId>mxpio-system</artifactId>
    </dependency>
    <dependency>
        <groupId>com.mxpio</groupId>
        <artifactId>mxpio-cache-caffeine</artifactId>
    </dependency>
    <dependency>
        <groupId>com.mxpio</groupId>
        <artifactId>mxpio-excel</artifactId>
    </dependency>
    <dependency>
        <groupId>com.mxpio</groupId>
        <artifactId>mxpio-quartz</artifactId>
    </dependency>
    <dependency>
        <groupId>com.mxpio</groupId>
        <artifactId>mxpio-camunda</artifactId>
    </dependency>
</dependencies>
```

### 3.3 Configuration Sample

```yaml
server:
  port: 9005

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mboot?characterEncoding=utf-8&useSSL=true&nullCatalogMeansCurrent=true
    username: root
    password: your-password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  redis:
    host: 127.0.0.1
    port: 6379
  sql:
    init:
      mode: always
      platform: mysql
      data-locations: classpath*:data-${spring.sql.init.platform}.sql,classpath*:data.sql
  quartz:
    job-store-type: jdbc

camunda:
  bpm:
    database:
      type: mysql
      schema-update: true
```

---

## 4. Configuration System

MxpIO Boot uses a **module-level defaults + app-level overrides** mechanism:

```
App-level mxpio.properties   ← Highest priority (overrides module defaults)
        ↓
Module-level mxpio.properties ← Defaults provided by each module
        ↓
application.yml              ← Spring Boot standard config
```

Each module declares defaults in `src/main/resources/mxpio/mxpio.properties`. The application project can override any value in its own `mxpio.properties`.

---

## 5. Core Features

- **Auto-configuration** — `mxpio-autoconfigure` conditionally assembles modules based on classpath
- **RBAC** — Three-level permission control: URL / Button / Data, with JWT Token support
- **Linq Queries** — `mxpio-jpa` provides C# Linq-style JPA query wrappers
- **Multi-tenancy** — Isolated database per tenant, dynamic datasource switching
- **Workflow** — Deep Camunda 7.x integration with process designer + task management
- **Code Generation** — Auto-generate Controller / Service / Repository from entities
- **File Management** — Abstract storage layer supporting Local / MinIO, extensible to OSS
- **Message Center** — Unified push interface: in-app, WebSocket, Email, DingTalk, WeCom
- **Expression Engine** — Aviator for business rule configuration
- **Job Scheduler** — Quartz with JDBC persistence, cluster-ready
- **Data Dashboard** — Built-in visualization configuration (mxpio-datav)

---

## 6. Documentation

See `docs/` directory for detailed development guides, or visit [https://mxpio.com/](https://mxpio.com/).

---

## 7. Adopters

The following companies are using this framework:

- **Henan Talent Group** — [http://www.hn-talent.com/](http://www.hn-talent.com/)
- **Zhengzhou Zhuozhen Information Technology Co., Ltd.** — [https://www.datazhzh.com/](https://www.datazhzh.com/)
- **Shandong Hemei Network Technology Co., Ltd.** — [http://www.unidbg.cn/](http://www.unidbg.cn/)

If your company is also using this framework, feel free to reach out via Issue.

---

## 8. Screenshots

| Login | Menu Management |
|-------|----------------|
| ![Login](screenshots/登录.png) | ![Menu](screenshots/菜单.png) |

| Role Management | Department Management |
|-----------------|----------------------|
| ![Role](screenshots/角色.png) | ![Department](screenshots/部门.png) |

| Import Management | Export Management |
|-------------------|-------------------|
| ![Import](screenshots/导入管理.png) | ![Export](screenshots/导出管理.png) |

| Data Dictionary | Job Scheduler |
|-----------------|---------------|
| ![Dict](screenshots/字典.png) | ![Scheduler](screenshots/任务调度.png) |

---

## 9. License

[MIT License](https://gitee.com/i_mxpio/mxpio-boot/blob/master/LICENSE)

Thanks to [JetBrains](https://www.jetbrains.com/) for the IDE license.
