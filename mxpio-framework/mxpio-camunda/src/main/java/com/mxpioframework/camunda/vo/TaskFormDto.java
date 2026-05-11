package com.mxpioframework.camunda.vo;

import java.io.Serializable;
import java.util.Map;

import org.camunda.bpm.engine.variable.VariableMap;

import com.mxpioframework.camunda.entity.FormModelDef;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TaskFormDto implements Serializable {
	
	public TaskFormDto(VariableMap formData, FormModelDef formModelDef) {
		this.data = formData;
		this.formModelDef = formModelDef;
	}

	private static final long serialVersionUID = 1L;

	@Schema(description = "表单模型定义")
	private FormModelDef formModelDef;
	
	@Schema(description = "表单数据")
	private Map<String, Object> data;

}
