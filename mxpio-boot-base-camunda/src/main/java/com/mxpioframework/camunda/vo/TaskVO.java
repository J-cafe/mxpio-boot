package com.mxpioframework.camunda.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mxpioframework.jpa.annotation.DictAble;
import com.mxpioframework.security.annotation.Dict;
import com.mxpioframework.security.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TaskVO implements Serializable, DictAble {

	private static final long serialVersionUID = 1L;

	@Schema(description = "ID")
	private String id;

	@Dict(dicCode = "username", dicEntity = User.class, dicText = "nickname")
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

	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@Schema(description = "完成时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endTime;

	@Schema(description = "流程发起时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date procStartTime;

	@Schema(description = "发起人")
	@Dict(dicCode = "username", dicEntity = User.class, dicText = "nickname")
	private String procStartUserId;

	@Schema(description = "流程定义名称")
	private String processDefinitionName;

	@Schema(description = "流程定义Key")
	private String processDefinitionKey;

	@Schema(description = "流程状态")
	private String procState;

	@Schema(description = "流程标题")
	private String title;

	@Schema(description = "是否有表单")
	private Boolean hasForm;

	@Schema(description = "task类别,active,candidateUser,candidateGroup")
	private String taskType;

	private String executionId;

	public TaskVO(HistoricTaskInstance task) {
		this.id = task.getId();
		this.assignee = task.getAssignee();
		this.name = task.getName();
		this.taskDefinitionKey = task.getTaskDefinitionKey();
		this.processInstanceId = task.getProcessInstanceId();
		this.processDefinitionId = task.getProcessDefinitionId();
		this.processDefinitionKey = task.getProcessDefinitionKey();
		this.createTime = task.getStartTime();
		this.endTime = task.getEndTime();
		this.executionId = task.getExecutionId();
	}

	public TaskVO(Task task) {
		this.id = task.getId();
		this.assignee = task.getAssignee();
		this.name = task.getName();
		this.taskDefinitionKey = task.getTaskDefinitionKey();
		this.processInstanceId = task.getProcessInstanceId();
		this.processDefinitionId = task.getProcessDefinitionId();
		this.createTime = task.getCreateTime();
		// this.endTime = task.getEndTime();
		this.executionId = task.getExecutionId();
	}

	public TaskVO(HistoricTaskInstance task, HistoricProcessInstance historicProcessInstance) {
		this(task);
		this.procStartTime = historicProcessInstance.getStartTime();
		this.procStartUserId = historicProcessInstance.getStartUserId();
		this.processDefinitionName = historicProcessInstance.getProcessDefinitionName();
		this.procState = historicProcessInstance.getState();
	}

	public TaskVO(Task task, HistoricProcessInstance historicProcessInstance) {
		this(task);
		this.procStartTime = historicProcessInstance.getStartTime();
		this.procStartUserId = historicProcessInstance.getStartUserId();
		this.processDefinitionName = historicProcessInstance.getProcessDefinitionName();
		this.processDefinitionKey = historicProcessInstance.getProcessDefinitionKey();
	}

	private Map<String, String> textMap;

	public String putText(String key, String value) {
		if (textMap == null) {
			textMap = new HashMap<>();
		}
		return textMap.put(key, value);
	}

}
