# mxpio-camunda — 工作流引擎整合

> `mxpio-camunda` 模块集成了 Camunda 7.24.0 工作流引擎，为 Spring Boot 4.0 做了适配覆盖。

---

## 模块说明

mxpio-boot 基于 **Camunda 7.24.0** 提供完整的 BPMN 2.0 工作流支持。`mxpio-camunda` 模块包含对 Camunda Spring Boot Starter 的兼容适配（覆盖类），确保在 Spring Boot 4.0 环境下正常工作。

## Maven 依赖

```xml
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-camunda</artifactId>
</dependency>
```

> 版本由 `mxpio-dependencies` BOM 统一管理。

## 配置项

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `camunda.bpm.database.type` | `mysql` | 数据库类型 |
| `camunda.bpm.database.schema-update` | `true` | 自动更新数据库 schema |
| `camunda.bpm.history-level` | `FULL` | 历史级别（NONE/ACTIVITY/AUDIT/FULL） |
| `camunda.bpm.auto-deployment-enabled` | `false` | 是否自动部署 classpath 下 BPMN 文件 |
| `camunda.bpm.eventing.execution` | `true` | 启用执行事件监听 |
| `camunda.bpm.eventing.history` | `true` | 启用历史事件监听 |
| `camunda.bpm.eventing.task` | `true` | 启用任务事件监听 |

## 核心功能

### 流程定义

流程使用 BPMN 2.0 标准进行建模。Camunda Modeler（桌面工具）可进行可视化设计。

**实体说明：**

| 实体 | 表名 | 说明 |
|------|------|------|
| `BpmnFlow` | `mb_bpmn_flow` | 流程流转记录 |
| `FormModelDef` | `mb_form_model_def` | 表单模型定义 |
| `FormModel` | `mb_form_model` | 表单模型实例 |

### 流程控制器 API

**流程管理（ProcessController）：**

| 端点 | 方法 | 说明 |
|------|------|------|
| `/camunda/process/deploy` | POST | 部署 BPMN 流程定义 |
| `/camunda/process/start` | POST | 启动流程实例 |
| `/camunda/process/list` | GET | 流程定义列表 |

**任务管理（TaskController）：**

| 端点 | 方法 | 说明 |
|------|------|------|
| `/camunda/task/myPendingTask` | GET | 我的待办任务 |
| `/camunda/task/myFinishedTask` | GET | 我的已办任务 |
| `/camunda/task/complete` | POST | 完成任务 |
| `/camunda/task/claim` | POST | 签收任务 |

**流程跟踪（FlowController）：**

| 端点 | 方法 | 说明 |
|------|------|------|
| `/camunda/flow/list` | GET | 查询流程流转 |

**表单模型（FormModelController）：**

| 端点 | 方法 | 说明 |
|------|------|------|
| `/camunda/form/list` | GET | 表单模型列表 |
| `/camunda/form/def/list` | GET | 表单定义列表 |

### 全局流程事件

`CamundaGlobalListenerPlugin` 注册全局事件监听器。通过实现 `CamundaGlobalListenerDelegate` 可自定义流程事件处理逻辑。

### EL 表达式

`ElFuncService` 提供 Camunda EL 表达式中的自定义函数注册。在 BPMN 中可使用 `${elFunc.xxx()}` 调用。

## 使用示例

### 1. 部署流程定义

```http
POST /camunda/process/deploy
Content-Type: multipart/form-data

bpmnFile: @leave-process.bpmn
```

### 2. 启动流程

```http
POST /camunda/process/start
Content-Type: application/json

{
  "processDefinitionKey": "leave_process",
  "businessKey": "BIZ-2025-001",
  "variables": {
    "applicant": "张三",
    "days": 3,
    "reason": "年假"
  }
}
```

### 3. 查询待办任务

```http
GET /camunda/task/myPendingTask?assignee=zhangsan
```

### 4. 完成任务

```http
POST /camunda/task/complete
Content-Type: application/json

{
  "taskId": "task-id-from-pending-list",
  "variables": {
    "approved": true,
    "comment": "同意请假"
  }
}
```
