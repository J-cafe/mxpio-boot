package com.mxpioframework.system.websocket.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.mxpioframework.system.websocket.service.WebSocketService;
import com.mxpioframework.system.websocket.vo.ResponseMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WebSocketServiceImpl implements WebSocketService {
	
	//使用SimpMessagingTemplate 向浏览器发送消息
	@Autowired
    private SimpMessagingTemplate template;

	@Override
	public void sendMessage(ResponseMessage message) throws Exception {
		for(int i=0;i<10;i++)
        {
            Thread.sleep(1000);
            template.convertAndSend("/topic/getResponse",message);
            log.info("sendMessage:{}", message.getContent());
        }
	}

}
