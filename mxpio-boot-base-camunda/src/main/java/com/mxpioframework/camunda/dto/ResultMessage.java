package com.mxpioframework.camunda.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResultMessage implements Serializable {
	
	private boolean success;
	
	private String msg;

	private Object data;

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

	public static ResultMessage success(String msg, Object data) {
		ResultMessage resultMessage = ResultMessage.success(msg);
		resultMessage.setData(data);
		return resultMessage;
	}

}
