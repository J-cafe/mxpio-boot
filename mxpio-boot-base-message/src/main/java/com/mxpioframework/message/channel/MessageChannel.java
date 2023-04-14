package com.mxpioframework.message.channel;


import com.mxpioframework.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageChannel {

    String getChannelCode();

    String getChannelName();

    boolean support(String channelCode);

    void send(String from,String [] to,String title,String msg);

    void doSend(String from,String [] to,String title,String msg);

    boolean beforeSend(String from,String [] to,String title,String msg);

    void afterSend(String from,String [] to,String title,String msg);

    void read(String msgId);

    void readAll();

    Page<Message> myMessage(Pageable pageable);

}
