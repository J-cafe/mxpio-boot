package com.mxpioframework.camunda.dto;

import java.io.Serializable;
import java.util.Map;

import com.mxpioframework.camunda.entity.FormModelDef;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TaskDetailDto implements Serializable, BpmnResource {
	
	public TaskDetailDto(String taskId) {
		this.id = taskId;
	}

	private static final long serialVersionUID = 1L;

	@Schema(description = "ID")
	private String id;
	
	@Schema(description = "bpmn文件")
	private String source;
	
	@Schema(description = "是否有启动表单")
	private boolean startFormKey;
	
	@Schema(description = "开始节点数据")
	private Map<String, Object> startDatas;
	
	@Schema(description = "开始节点表单")
	private FormModelDef startFormModelDef;
}
