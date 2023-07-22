package com.mxpioframework.camunda.vo;

import java.io.Serializable;
import java.util.Date;

import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mxpioframework.security.annotation.Dict;
import com.mxpioframework.security.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TaskVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Schema(description = "ID")
	private String id;
	
	@Dict(dicCode="username", dicEntity = User.class, dicText = "nickname")
	@Schema(description = "当前审批人")
	private String assignee;
	
	@Schema(description = "节点名称")
	private String name;
	
	@Schema(description = "节点定义Key")
	private String taskDefinitionKey;
	
	@Schema(description = "流程实例ID")
	private String processInstanceId;
	
	@Schema(description = "流程定义ID")
	private String processDefinitionId;
	
	@Schema(description = "流程定义Key")
	private String processDefinitionKey;
	
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	@Schema(description = "流程发起时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date procStartTime;
	
	@Schema(description = "发起人")
	@Dict(dicCode="username", dicEntity = User.class, dicText = "nickname")
	private String procStartUserId;
	
	@Schema(description = "流程定义名称")
	private String processDefinitionName;
	
	public TaskVO(HistoricTaskInstance task) {
		this.id = task.getId();
		this.assignee = task.getAssignee();
		this.name = task.getName();
		this.taskDefinitionKey = task.getTaskDefinitionKey();
		this.processInstanceId = task.getProcessInstanceId();
		this.processDefinitionKey = task.getProcessDefinitionKey();
		this.createTime = task.getStartTime();
	}

	public TaskVO(HistoricTaskInstance task, HistoricProcessInstance historicProcessInstance) {
		this(task);
		this.procStartTime = historicProcessInstance.getStartTime();
		this.procStartUserId = historicProcessInstance.getStartUserId();
		this.processDefinitionName = historicProcessInstance.getProcessDefinitionName();
	}

}