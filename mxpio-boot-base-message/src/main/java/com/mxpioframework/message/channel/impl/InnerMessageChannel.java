package com.mxpioframework.message.channel.impl;

import com.alibaba.fastjson.JSON;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.Operator;
import com.mxpioframework.jpa.query.Order;
import com.mxpioframework.message.entity.InnerMessage;
import com.mxpioframework.message.entity.Message;
import com.mxpioframework.message.handler.MessageWebSocketHandler;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * 站内信
 */
@Component
@Slf4j
public class InnerMessageChannel extends AbstractMessageChannel {

    private static final String CHANNEL_CODE = "innerMsg";

    private static final String CHANNEL_NAME = "站内信";

    @Autowired
    private MessageWebSocketHandler messageWebSocketHandler;

    @Override
    public String getChannelCode() {
        return CHANNEL_CODE;
    }

    @Override
    public String getChannelName() {
        return CHANNEL_NAME;
    }

    @Override
    public boolean support(String channelCode) {
        return StringUtils.equals(CHANNEL_CODE,channelCode);
    }
    @Override
    public void doSend(String from, String[] to, String title, String content) {
        User fromUser = JpaUtil.getOne(User.class, from);
        List<User> toUserList = JpaUtil.linq(User.class).in("username", (Object[]) to).list();
        for(User toUser:toUserList){
            InnerMessage innerMessage = new InnerMessage();
            innerMessage.setToUserName(toUser.getUsername());
            innerMessage.setToNickName(toUser.getNickname());
            innerMessage.setFromUserName(from);
            innerMessage.setFromNickName(fromUser.getNickname());
            innerMessage.setMessageTitle(title);
            innerMessage.setMessageContent(content);
            JpaUtil.save(innerMessage);
            try{
                messageWebSocketHandler.send(toUser.getUsername(), JSON.toJSONString(innerMessage));
            }
            catch (Exception e){
                log.error("websocket发送信息错误:{}",e.getMessage());
            }

        }
    }

    @Override
    public void read(String msgId) {
        InnerMessage message = JpaUtil.getOne(InnerMessage.class,msgId);
        message.setReadStatus("1");
        JpaUtil.update(message);
    }

    @Override
    public void readAll() {
        List<InnerMessage> messages =JpaUtil.linq(InnerMessage.class).equal("toUserName", SecurityUtils.getLoginUsername()).equal("readStatus","0").list();
        for(InnerMessage message:messages){
            message.setReadStatus("1");
            JpaUtil.update(message);
        }
    }

    @Override
    public Page<Message> myMessagePaged(Criteria criteria, Pageable pageable){
        getMyMessageCriteria(criteria);
        return JpaUtil.linq(InnerMessage.class).where(criteria).paging(pageable);
    }

    @Override
    public List<Message> myMessage(Criteria criteria){
        getMyMessageCriteria(criteria);
        return JpaUtil.linq(InnerMessage.class).where(criteria).list();
    }

    @Override
    public Page<Message> myUnreadPaged(Criteria criteria, Pageable pageable){
        getMyUnreadCriteria(criteria);
        return JpaUtil.linq(InnerMessage.class).where(criteria).paging(pageable);
    }

    @Override
    public List<Message> myUnread(Criteria criteria){
        getMyUnreadCriteria(criteria);
        return JpaUtil.linq(InnerMessage.class).where(criteria).list();
    }

    private void getMyMessageCriteria(Criteria criteria){
        if(criteria==null){
            criteria = Criteria.create();
        }
        criteria.addCriterion("toUserName", Operator.EQ,SecurityUtils.getLoginUsername());
        criteria.setOrders(new ArrayList<>());
        criteria.addOrder(new Order("readStatus",false));
        criteria.addOrder(new Order("createTime",true));
    }

    private void getMyUnreadCriteria(Criteria criteria){
        if(criteria==null){
            criteria = Criteria.create();
        }
        criteria.addCriterion("toUserName", Operator.EQ,SecurityUtils.getLoginUsername());
        criteria.addCriterion("readStatus", Operator.EQ,"0");
        criteria.setOrders(new ArrayList<>());
        criteria.addOrder(new Order("createTime",true));
    }
}
