# 工作流引擎 — 流程自动化

> mxpio-boot 的工作流能力由 `mxpio-camunda` 模块提供，
> 基于 Camunda 7.24.0 BPMN 2.0 引擎。

---

## 概述

mxpio-boot 的任务/流程自动化功能通过集成的 **Camunda BPM 引擎** 实现。它不是一个独立模块，而是由 `mxpio-camunda` 模块完整提供。

**能力范围：**

- BPMN 2.0 流程建模与部署
- 流程实例的启动、挂起、终止
- 人工任务分配、签收、审批
- 流程历史追踪与审计
- 表单模型绑定
- 全局流程事件监听
- 与消息系统联动（审批待办通知）

## 工作流 vs. 业务代码

```
┌─────────────────────────────────────────────────┐
│                业务代码                          │
│  ┌──────────┐  ┌──────────┐  ┌──────────────┐  │
│  │ OrderSvc │  │ TaskSvc  │  │ UserService  │  │
│  └────┬─────┘  └────┬─────┘  └──────┬───────┘  │
│       │              │               │          │
│  ┌────▼──────────────▼───────────────▼───────┐  │
│  │         Camunda 流程引擎                    │  │
│  │  ┌─────────┐ ┌─────────┐ ┌────────────┐  │  │
│  │  │ BPMN    │ │ Task    │ │ History    │  │  │
│  │  │ Deploy  │ │ Runtime │ │ Audit      │  │  │
│  │  └─────────┘ └─────────┘ └────────────┘  │  │
│  └───────────────────────────────────────────┘  │
└─────────────────────────────────────────────────┘
```

## 典型流程：请假审批

一个典型的 BPMN 流程：

```
开始 → 提交申请 → 部门审批 → 人事审批 → 结束
                    │                      ↑
                    └→ 驳回 ───────────────┘
```

### 步骤 1 — 设计 BPMN

使用 Camunda Modeler 设计 `leave-process.bpmn`，定义任务节点、流转条件、表单字段。

### 步骤 2 — 部署

```http
POST /camunda/process/deploy
Content-Type: multipart/form-data

bpmnFile: @leave-process.bpmn
```

### 步骤 3 — 启动

```http
POST /camunda/process/start
Content-Type: application/json

{
  "processDefinitionKey": "leave_process",
  "businessKey": "LEAVE-2025-001",
  "variables": {
    "applicant": "张三",
    "days": 3,
    "deptApprover": "李四",
    "hrApprover": "王五"
  }
}
```

### 步骤 4 — 部门审批

```http
GET /camunda/task/myPendingTask?assignee=李四

# 审批通过
POST /camunda/task/complete
Content-Type: application/json

{
  "taskId": "task-001",
  "variables": {
    "deptApproved": true,
    "deptComment": "同意"
  }
}
```

### 步骤 5 — 流程跟踪

```http
GET /camunda/flow/list?processInstanceId=proc-inst-001
```

## 与消息系统联动

审批任务创建时自动推送通知：

```java
// CamundaGlobalListenerDelegate 实现中
public class TaskCreateListener implements CamundaGlobalListenerDelegate {

    @Autowired
    private MessageChannel messageChannel;

    @Override
    public void notify(DelegateTask delegateTask) {
        String assignee = delegateTask.getAssignee();
        String taskName = delegateTask.getName();

        messageChannel.send("system",
            new String[]{assignee},
            "审批提醒",
            "您有一个待审批任务：" + taskName,
            delegateTask.getProcessInstanceId()
        );
    }
}
```

## 更多信息

详细使用说明请参考：

- [mxpio-camunda 模块文档](./modules/camunda.md)
- GUIDE.md 的[工作流引擎章节](../GUIDE.md#4-工作流引擎)
