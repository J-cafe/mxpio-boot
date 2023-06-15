package com.mxpioframework.camunda.dto;

import java.io.Serializable;

import org.camunda.bpm.engine.history.HistoricTaskInstance;

import io.swagger.v3.oas.annotations.media.Schema;

public class HistoricTaskDto implements Serializable  {

	public HistoricTaskDto(HistoricTaskInstance task) {
		
	}

	private static final long serialVersionUID = 1L;
	
	@Schema(description = "ID")
	private String id;
	
	@Schema(description = "表单key")
	private String formKey;

}
