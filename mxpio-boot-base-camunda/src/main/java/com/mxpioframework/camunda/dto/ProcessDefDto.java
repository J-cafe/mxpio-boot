package com.mxpioframework.camunda.dto;

import java.io.Serializable;

import org.camunda.bpm.engine.repository.ProcessDefinition;

import com.mxpioframework.camunda.entity.FormModelDef;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProcessDefDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Schema(description = "ID")
	private String id;
	
	@Schema(description = "key")
	private String key;
	
	@Schema(description = "名称")
	private String name;
	
	@Schema(description = "描述")
	private String description;
	
	@Schema(description = "版本")
	private Integer version;
	
	@Schema(description = "是否有启动表单")
	private boolean startFormKey;
	
	private FormModelDef formModelDef;
	
	public ProcessDefDto(ProcessDefinition procDef) {
		this.id = procDef.getId();
		this.key = procDef.getKey();
		this.name = procDef.getName();
		this.description = procDef.getDescription();
		this.version = procDef.getVersion();
		this.startFormKey = procDef.hasStartFormKey();
	}

}
