package com.mxpioframework.camunda.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResultMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private boolean success;
	
	private String msg;

	public static ResultMessage error(String msg) {
		ResultMessage resultMessage = new ResultMessage();
		resultMessage.setSuccess(false);
		resultMessage.setMsg(msg);
		return resultMessage;
	}

	public static ResultMessage success(String msg) {
		ResultMessage resultMessage = new ResultMessage();
		resultMessage.setSuccess(true);
		resultMessage.setMsg(msg);
		return resultMessage;
	}

}
