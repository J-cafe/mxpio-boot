package com.mxpioframework.system.websocket.service;

import com.mxpioframework.system.websocket.vo.ResponseMessage;

public interface WebSocketService {

	public void sendMessage(ResponseMessage message) throws Exception;
}
