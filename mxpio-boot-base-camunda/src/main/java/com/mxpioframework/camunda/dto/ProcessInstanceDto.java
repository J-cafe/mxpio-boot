package com.mxpioframework.camunda.dto;

import java.io.Serializable;

import org.camunda.bpm.engine.runtime.ProcessInstance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProcessInstanceDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Schema(description = "ID")
	private String id;
	
	@Schema(description = "业务ID")
	private String businessKey;

	public ProcessInstanceDto(ProcessInstance procInst) {
		this.id = procInst.getId();
		this.businessKey = procInst.getBusinessKey();
	}
}
