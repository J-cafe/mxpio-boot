package com.mxpioframework.message.channel;


import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageChannel {

    /**
     * 渠道编码
     * @return
     */
    String getChannelCode();

    /**
     * 渠道名称
     * @return
     */
    String getChannelName();

    /**
     * 此渠道编码是否为channelCode
     * @param channelCode
     * @return
     */
    boolean support(String channelCode);

    /**
     * 发送消息
     * @param from
     * @param to
     * @param title
     * @param msg
     */
    void send(String from,String [] to,String title,String msg);

    /**
     * 发送消息，各渠道实现自己的特殊发送方法
     * @param from
     * @param to
     * @param title
     * @param msg
     */
    void doSend(String from,String [] to,String title,String msg);

    /**
     * 消息发送前钩子
     * @param from
     * @param to
     * @param title
     * @param msg
     * @return
     */
    boolean beforeSend(String from,String [] to,String title,String msg);

    /**
     * 消息发送后钩子
     * @param from
     * @param to
     * @param title
     * @param msg
     */
    void afterSend(String from,String [] to,String title,String msg);

    /**
     * 消息已读
     * @param msgId
     */
    void read(String msgId);

    /**
     * 已读所有消息
     */
    void readAll();

    /**
     * 我的所有消息
     * @param pageable
     * @return
     */
    Page<Message> myMessagePaged(Criteria criteria, Pageable pageable);

    /**
     * 我的所有消息
     * @param pageable
     * @return
     */
    List<Message> myMessage(Criteria criteria);
    /**
     * 我的未读消息
     * @param pageable
     * @return
     */
    Page<Message> myUnreadPaged(Criteria criteria,Pageable pageable);

    /**
     * 我的未读消息
     * @param pageable
     * @return
     */
    List<Message> myUnread(Criteria criteria);

}
