package com.mxpioframework.camunda.dto;

import java.io.Serializable;
import java.util.Date;

import org.camunda.bpm.engine.task.Task;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TaskDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Schema(description = "ID")
	private String id;
	
	@Schema(description = "当前审批人")
	private String assignee;
	
	@Schema(description = "表单key")
	private String formKey;
	
	@Schema(description = "节点名称")
	private String name;
	
	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	public TaskDto(Task task) {
		this.id = task.getId();
		this.assignee = task.getAssignee();
		this.name = task.getName();
		this.createTime = task.getCreateTime();
	}

}
