package com.mxpioframework.message.service;

import com.mxpioframework.message.pojo.MessageChannelVo;

import java.util.List;

public interface MessageService {

    List<MessageChannelVo> getAllChannel();

    void sendMessage(String [] channelCodes,String from, String[] to, String title, String content);
}
