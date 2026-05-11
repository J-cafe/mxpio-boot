# mxpio-boot 使用文档

> mxpio-boot — 基于 Spring Boot 4.0 + JDK 25 的企业级快速开发框架。

## 快速入口

| 文档 | 说明 |
|------|------|
| [📖 QUICK_START.md](./QUICK_START.md) | **从这里开始** — 5 分钟内从零启动项目 |
| [📚 GUIDE.md](./GUIDE.md) | 完整使用指南 — 覆盖所有核心功能 |
| [🏗️ 架构概览](./development/architecture.md) | 模块依赖图、分层架构、启动流程 |

## 模块文档

### 核心框架模块

| 模块 | 文档 | 说明 |
|------|------|------|
| mxpio-common | [📄 common.md](./modules/common.md) | 公共常量、工具类、SpringUtil |
| mxpio-jpa | [📄 jpa.md](./modules/jpa.md) | Linq 语言集成查询、Criteria 构建器 |
| mxpio-security | [📄 security.md](./modules/security.md) | RBAC 权限体系（URL/按钮/数据权限） |
| mxpio-cache | [📄 cache.md](./modules/cache.md) | 缓存抽象层（Redis + Caffeine） |
| mxpio-quartz | [📄 scheduler.md](./modules/scheduler.md) | Quartz 定时任务管理 |
| mxpio-log | [📄 logger.md](./modules/logger.md) | 操作日志审计记录 |
| mxpio-camunda | [📄 camunda.md](./modules/camunda.md) | Camunda 工作流引擎集成 |
| mxpio-websocket | [📄 websocket.md](./modules/websocket.md) | WebSocket 实时推送 |
| 工作流整体 | [📄 workflow.md](./modules/workflow.md) | 工作流程自动化总览 |

### 部署文档

| 文档 | 说明 |
|------|------|
| [📄 MySQL 配置](./deployment/mysql.md) | 数据库配置与初始化 |
| [📄 Redis 配置](./deployment/redis.md) | Redis 缓存配置 |
| [📄 RocketMQ 配置](./deployment/rocketmq.md) | 消息队列集成 |
| [📄 Docker 部署](./deployment/docker.md) | 容器化部署方案 |

### 开发指南

| 文档 | 说明 |
|------|------|
| [📄 架构概览](./development/architecture.md) | 整体架构和模块依赖 |
| [📄 模块开发指南](./development/module-guide.md) | 如何新增业务模块 |
| [📄 自定义与扩展](./development/customize.md) | 框架扩展点说明 |

## 项目信息

| 项目 | 值 |
|------|-----|
| 版本 | 4.0.0-beta.1 |
| JDK | 25 |
| Spring Boot | 4.0.1 |
| 许可 | MIT License |
| 开发者 | MxpIO (i@mxpio.com) |
