package com.mxpioframework.message.channel;


public interface MessageChannel {

    String getChannelCode();

    String getChannelName();

    boolean support(String channelCode);

    void send(String from,String [] to,String title,String msg);
}
