package com.mxpioframework.message.service;

import com.mxpioframework.message.entity.InnerMessage;
import com.mxpioframework.message.pojo.MessageChannelVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {

    List<MessageChannelVo> getAllChannel();

    void sendMessage(String [] channelCodes,String from, String[] to, String title, String content);

    Page<InnerMessage> myMessage(Pageable pageable);

    void read(String msgId);

    void readAll();
}
