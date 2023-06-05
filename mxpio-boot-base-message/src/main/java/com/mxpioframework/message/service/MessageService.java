package com.mxpioframework.message.service;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.message.entity.Message;
import com.mxpioframework.message.pojo.MessageChannelVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {

    List<MessageChannelVo> getAllChannel();

    void sendMessage(String [] channelCodes,String from, String[] to, String title, String content);

    Page<Message> myMessage(String channelCode, Criteria criteria, Pageable pageable);

    Page<Message> myUnread(String channelCode, Criteria criteria,Pageable pageable);

    void read(String channelCode, String msgId);

    void readAll(String channelCode);
}
