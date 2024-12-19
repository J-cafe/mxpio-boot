package com.mxpioframework.message.channel;


import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageChannel {

    /**
     * 渠道编码
     * @return 渠道编码
     */
    String getChannelCode();

    /**
     * 渠道名称
     * @return 渠道名称
     */
    String getChannelName();

    /**
     * 此渠道编码是否为channelCode
     * @param channelCode 渠道编码
     * @return 是否支持
     */
    boolean support(String channelCode);

    /**
     * 发送消息
     * @param from 发送人
     * @param to 收件人
     * @param title 标题
     * @param msg 消息主体
     */
    void send(String from,String [] to,String title,String msg);

    /**
     * 发送消息，各渠道实现自己的特殊发送方法
     * @param from 发送人
     * @param to 收件人
     * @param title 标题
     * @param msg 消息主体
     */
    void doSend(String from,String [] to,String title,String msg);

    /**
     * 消息发送前钩子
     * @param from 发送人
     * @param to 收件人
     * @param title 标题
     * @param msg 消息主体
     * @return 是否执行
     */
    boolean beforeSend(String from,String [] to,String title,String msg);

    /**
     * 消息发送后钩子
     * @param from 发送人
     * @param to 收件人
     * @param title 标题
     * @param msg 消息主体
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
     * @param criteria 查询过滤器
     * @param pageable 分页
     * @return 消息列表（分页）
     */
    Page<Message> myMessagePaged(Criteria criteria, Pageable pageable);

    /**
     * 我的所有消息
     * @param criteria 查询过滤器
     * @return 消息列表
     */
    List<Message> myMessage(Criteria criteria);

    /**
     * 我的未读消息
     * @param criteria 查询过滤器
     * @param pageable 分页
     * @return 消息列表（分页）
     */
    Page<Message> myUnreadPaged(Criteria criteria,Pageable pageable);

    /**
     * 我的未读消息
     * @param criteria 查询过滤器
     * @return 消息列表
     */
    List<Message> myUnread(Criteria criteria);

}
