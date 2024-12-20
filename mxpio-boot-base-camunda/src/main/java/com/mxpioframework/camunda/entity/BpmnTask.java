package com.mxpioframework.camunda.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name = "V_BPMN_ALL_TASKS")
@Schema(description="任务")
public class BpmnTask implements Serializable {

    @Id
    @Column(name = "VIEW_ID_")
    @Schema(description = "视图ID")
    private String viewId;

    @Column(name = "REV_")
    @Schema(description = "rev")
    private Integer rev;

    @Column(name = "ID_")
    @Schema(description = "taskId")
    private String id;

    @Column(name = "NAME_")
    @Schema(description = "名称")
    private String name;

    @Column(name = "PARENT_TASK_ID_")
    @Schema(description = "父taskId")
    private String parentTaskId;

    @Column(name = "DESCRIPTION_")
    @Schema(description = "描述")
    private String description;

    @Column(name = "PRIORITY_")
    @Schema(description = "优先级")
    private Integer priority;

    @Column(name = "CREATE_TIME_")
    @Schema(description = "创建时间")
    private Date createTime;

    @Column(name = "OWNER_")
    @Schema(description = "所有者")
    private String owner;

    @Column(name = "ASSIGNEE_")
    @Schema(description = "ASSIGNEE_")
    private String assignee;

    @Column(name = "DELEGATION_")
    @Schema(description = "DELEGATION_")
    private String delegation;

    @Column(name = "EXECUTION_ID_")
    @Schema(description = "EXECUTION_ID_")
    private String executionId;

    @Column(name = "PROC_INST_ID_")
    @Schema(description = "PROC_INST_ID_")
    private String processInstanceId;

    @Column(name = "PROC_DEF_ID_")
    @Schema(description = "PROC_DEF_ID_")
    private String processDefinitionId;

    @Column(name = "PROC_DEF_NAME_")
    @Schema(description = "PROC_DEF_NAME_")
    private String processDefinitionName;

    @Column(name = "CASE_EXECUTION_ID_")
    @Schema(description = "CASE_EXECUTION_ID_")
    private String caseExecutionId;

    @Column(name = "CASE_INST_ID_")
    @Schema(description = "CASE_INST_ID_")
    private String caseInstId;

    @Column(name = "CASE_DEF_ID_")
    @Schema(description = "CASE_DEF_ID_")
    private String caseDefId;

    @Column(name = "TASK_DEF_KEY_")
    @Schema(description = "TASK_DEF_KEY_")
    private String taskDefinitionKey;

    @Column(name = "DUE_DATE_")
    @Schema(description = "DUE_DATE_")
    private Date dueDate;

    @Column(name = "FOLLOW_UP_DATE_")
    @Schema(description = "FOLLOW_UP_DATE_")
    private Date followUpDate;

    @Column(name = "SUSPENSION_STATE_")
    @Schema(description = "SUSPENSION_STATE_")
    private Integer suspensionState;

    @Column(name = "TENANT_ID_")
    @Schema(description = "TENANT_ID_")
    private String tenantId;

    @Column(name = "CANDIDATE_USER")
    @Schema(description = "CANDIDATE_USER")
    private String candidateUser;

    @Column(name = "CANDIDATE_GROUP")
    @Schema(description = "CANDIDATE_GROUP")
    private String candidateGroup;

    @Column(name = "PROC_START_USER_ID_")
    @Schema(description = "PROC_START_USER_ID_")
    private String procStartUserId;

    @Column(name = "BPMN_SORT_FLAG_")
    @Schema(description = "BPMN_SORT_FLAG_")
    private String bpmnSortFlag;

    @Column(name = "PROC_START_TIME_")
    @Schema(description = "流程发起时间")
    private Date procStartTime;

    @Column(name = "PROCESS_DEFINITION_KEY_")
    @Schema(description = "流程定义Key")
    private String processDefinitionKey;

    @Column(name = "PROC_TITLE_")
    @Schema(description = "流程标题")
    private String procTitle;

}
