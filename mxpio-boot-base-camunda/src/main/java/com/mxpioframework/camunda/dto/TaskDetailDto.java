package com.mxpioframework.camunda.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

public class TaskDetailDto implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	@Schema(description = "ID")
	private String id;
	
	@Schema(description = "表单key")
	private String formKey;

}
