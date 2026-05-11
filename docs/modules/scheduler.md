# mxpio-quartz — 定时任务调度模块

## 概述

`mxpio-quartz` 是基于 Quartz 调度框架的定时任务管理模块，提供 JDBC 存储的任务持久化、REST API 任务管理、以及 POJO 方法调度能力。支持单机和集群部署模式。

### 功能特性

- **JDBC 任务存储** — 任务元数据持久化到数据库，重启不丢失
- **两种 Job 类型** — "Class" 模式（新建实例）和 "SpringBean" 模式（Spring 容器管理）
- **REST API 管理** — 通过 `QuartzController` 创建、修改、暂停、恢复、删除任务
- **Cron 表达式** — 灵活的调度时间配置
- **集群支持** — 通过数据库锁实现集群下的任务协调
- **POJO 方法调度** — 无需实现 Quartz Job 接口，直接调用任意 POJO 的方法

---

## Maven 依赖

```xml
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-quartz</artifactId>
</dependency>
```

---

## 核心实体

### QuartzJob — 任务实体

映射任务配置表，存储任务的完整元信息：

| 字段              | 类型     | 说明                     |
|-------------------|----------|--------------------------|
| `id`              | Long     | 主键                     |
| `jobClassName`    | String   | 任务类全限定名           |
| `jobMethodName`   | String   | 任务执行方法名           |
| `jobType`         | String   | 任务类型：`Class`/`SpringBean` |
| `cronExpression`  | String   | Cron 表达式               |
| `params`          | String   | 任务参数（JSON 格式）     |
| `status`          | Integer  | 状态：0-暂停，1-运行中   |
| `remark`          | String   | 备注                     |
| `createBy`        | String   | 创建人                   |
| `createTime`      | Date     | 创建时间                 |
| `updateBy`        | String   | 更新人                   |
| `updateTime`      | Date     | 更新时间                 |

---

## MxpioJob — 任务调度封装

`MxpioJob` 实现 `org.quartz.Job` 接口，是 Quartz 调度器实际调用的入口。它负责根据 `QuartzJob` 的配置动态调用目标 POJO 的方法：

```java
import com.mxpio.quartz.job.MxpioJob;
// 框架自动注册，无需手动实现
```

### 两种任务类型

| 类型          | 说明                                       | 适用场景                           |
|---------------|--------------------------------------------|------------------------------------|
| `Class`       | 每次执行创建新的实例                        | 无状态任务，不需要 Spring 注入     |
| `SpringBean`  | 从 Spring 容器获取 Bean 实例（单例）         | 需要依赖注入的业务任务             |

---

## 完整示例

### 步骤 1：创建任务类

```java
package com.example.job;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * 示例定时任务 — 数据清理
 */
@Component("dataCleanJob")
public class DataCleanJob {

    /**
     * 注意：方法签名必须为 public，参数可选
     * 如果配置了 params 参数，方法可以接收 String 参数
     */
    public void cleanExpiredData() {
        System.out.println("[" + LocalDateTime.now() + "] 开始清理过期数据...");
        // 执行数据清理逻辑
        // ...
        System.out.println("[" + LocalDateTime.now() + "] 清理完成");
    }

    /**
     * 带参数的方法
     */
    public void cleanWithParam(String params) {
        System.out.println("[" + LocalDateTime.now() + "] 参数: " + params);
        // 根据参数执行清理
    }
}
```

### 步骤 2：注册任务（通过 QuartzController API）

通过 REST API 创建定时任务：

```bash
# 创建 SpringBean 类型的任务
curl -X POST http://localhost:8080/quartz/job \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "jobClassName": "com.example.job.DataCleanJob",
    "jobMethodName": "cleanExpiredData",
    "jobType": "SpringBean",
    "cronExpression": "0 0 2 * * ?",
    "params": "",
    "remark": "每天凌晨2点清理过期数据"
  }'
```

```bash
# 创建 Class 类型的任务
curl -X POST http://localhost:8080/quartz/job \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "jobClassName": "com.example.job.ReportJob",
    "jobMethodName": "generateReport",
    "jobType": "Class",
    "cronExpression": "0 0 8 * * 1",
    "params": "{\"type\":\"weekly\"}",
    "remark": "每周一早8点生成周报"
  }'
```

### 步骤 3：管理任务

```bash
# 暂停任务
PUT /quartz/job/{jobId}/pause

# 恢复任务
PUT /quartz/job/{jobId}/resume

# 立即执行一次（不影响调度计划）
POST /quartz/job/{jobId}/run

# 更新任务
PUT /quartz/job/{jobId}

# 删除任务
DELETE /quartz/job/{jobId}

# 查询任务列表
GET /quartz/job/list?status=1

# 查看任务执行日志
GET /quartz/job/log/{jobId}
```

---

## QuartzController — REST API

| 接口                       | 方法     | 说明           |
|----------------------------|----------|----------------|
| `POST /quartz/job`         | 创建     | 新建定时任务   |
| `PUT /quartz/job/{id}`     | 修改     | 更新任务配置   |
| `DELETE /quartz/job/{id}`  | 删除     | 删除定时任务   |
| `GET /quartz/job/list`     | 列表     | 查询任务列表   |
| `PUT /quartz/job/{id}/pause`  | 暂停  | 暂停任务       |
| `PUT /quartz/job/{id}/resume` | 恢复  | 恢复任务       |
| `POST /quartz/job/{id}/run`   | 立即执行 | 触发一次执行 |
| `GET /quartz/job/log/{id}`    | 日志  | 查看执行日志   |

---

## 配置文件

```yaml
spring:
  quartz:
    job-store-type: jdbc          # 使用 JDBC 存储
    jdbc:
      initialize-schema: never    # 生产环境设为 never，首次可设为 always
    properties:
      org.quartz.jobStore.tablePrefix: QRTZ_    # 表前缀
      org.quartz.jobStore.isClustered: true     # 启用集群模式（多个实例时）
      org.quartz.jobStore.clusterCheckinInterval: 20000
      org.quartz.scheduler.instanceName: MxpioScheduler
      org.quartz.scheduler.instanceId: AUTO
      org.quartz.threadPool.threadCount: 10     # 线程池大小
```

| 配置项                                             | 默认值        | 说明                     |
|----------------------------------------------------|---------------|--------------------------|
| `spring.quartz.job-store-type`                     | `memory`      | 设为 `jdbc` 持久化到数据库 |
| `spring.quartz.jdbc.initialize-schema`             | `embedded`    | 是否自动初始化 Quartz 表  |
| `spring.quartz.properties.org.quartz.jobStore.tablePrefix` | `QRTZ_` | Quartz 表前缀          |
| `spring.quartz.properties.org.quartz.jobStore.isClustered` | `false` | 启用集群模式              |

### 集群部署注意事项

1. 所有节点必须使用同一个数据库
2. 确保 `org.quartz.jobStore.isClustered` 设为 `true`
3. 每个节点的 `instanceId` 设为 `AUTO`（自动生成唯一 ID）
4. 建议所有节点使用相同的 `instanceName`
5. 需初始化 Quartz 数据库表（约 11 张 `QRTZ_` 前缀的表）

---

## Spring Bean 自动注入支持

对于 `SpringBean` 类型的任务，可以正常使用 `@Autowired` 等 Spring 依赖注入：

```java
@Component
public class OrderStatisticsJob {

    @Autowired
    private OrderService orderService;

    @Autowired
    private NotificationService notificationService;

    public void generateDailyReport() {
        // 可以正常使用注入的 Service
        List<Order> todayOrders = orderService.getTodayOrders();
        notificationService.sendReport(todayOrders);
    }
}
```

---

## 数据库表

使用 JDBC 存储时，Quartz 会使用以下表结构（`QRTZ_` 前缀）：

| 表名                     | 说明             |
|--------------------------|------------------|
| `QRTZ_TRIGGERS`          | 触发器信息       |
| `QRTZ_JOB_DETAILS`       | Job 详情         |
| `QRTZ_CRON_TRIGGERS`     | Cron 触发器      |
| `QRTZ_SIMPLE_TRIGGERS`   | 简单触发器       |
| `QRTZ_SCHEDULER_STATE`   | 调度器状态       |
| `QRTZ_LOCKS`             | 集群锁           |
| `QRTZ_FIRED_TRIGGERS`    | 正在执行的触发器 |
| `QRTZ_CALENDARS`         | 日历信息         |
| `QRTZ_PAUSED_TRIGGER_GRPS` | 暂停的触发器组 |
| `QRTZ_BLOB_TRIGGERS`     | BLOB 触发器      |

---

## 依赖关系

```
mxpio-quartz
├── mxpio-jpa
├── mxpio-common
├── spring-boot-starter-quartz
└── spring-boot-starter-web
```
