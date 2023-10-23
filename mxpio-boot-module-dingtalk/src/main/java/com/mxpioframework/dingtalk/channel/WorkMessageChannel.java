package com.mxpioframework.dingtalk.channel;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.mxpioframework.dingtalk.enums.DingTalkEnums;
import com.mxpioframework.dingtalk.pojo.LinkTypeContent;
import com.mxpioframework.dingtalk.pojo.DingTalkMessagePojo;
import com.mxpioframework.dingtalk.provider.AppTokenFactory;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.message.entity.Message;
import com.mxpioframework.message.handler.MessageWebSocketHandler;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 钉钉工作通知消息
 */
@Component
@Slf4j
public class WorkMessageChannel extends DingTalkAbstractMessageChannel {

    private static final String CHANNEL_CODE = "dingtalkWorkMsg";

    private static final String CHANNEL_NAME = "钉钉工作通知消息";

    @Value("${dingtalk.agentId}")
    private String agentId;
    @Autowired
    private AppTokenFactory appTokenFactory;

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
    @Transactional
    public void doSend(String from, String[] to, String title, String content) {
        String appToken = appTokenFactory.getAppToken();
        String toUserIds = StringUtils.join(to, ",");
        DingTalkMessagePojo messagePojo = JSONObject.parseObject(content, DingTalkMessagePojo.class);//转换消息体
        try {
            //link类型消息
            if (StringUtils.equals(messagePojo.getMsgtype(), DingTalkEnums.MsgType.LINK.getCode())){
                LinkTypeContent linkTypeContent = messagePojo.getLink();
                DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
                OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
                req.setAgentId(Long.parseLong(agentId));
                req.setUseridList(toUserIds);
                OapiMessageCorpconversationAsyncsendV2Request.Msg obj1 = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
                obj1.setMsgtype(DingTalkEnums.MsgType.LINK.getCode());
                OapiMessageCorpconversationAsyncsendV2Request.Link obj2 = new OapiMessageCorpconversationAsyncsendV2Request.Link();
                obj2.setPicUrl(linkTypeContent.getPicUrl());
                obj2.setMessageUrl(linkTypeContent.getMessageUrl());
                obj2.setTitle(linkTypeContent.getTitle());
                obj2.setText(linkTypeContent.getText());
                obj1.setLink(obj2);
                req.setMsg(obj1);
                OapiMessageCorpconversationAsyncsendV2Response response = client.execute(req, appToken);
                log.info("钉钉消息推送，内容："+content);
                if (response.getErrcode()!=0L){
                    log.error(response.getErrmsg());
                    throw new ApiException(response.getErrmsg());
                }
            }
        } catch (ApiException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        /*List<User> toUserList = JpaUtil.linq(User.class).in("thirdId", (Object[]) to).list();
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

        }*/
    }

    @Override
    public void read(String msgId) {

    }

    @Override
    public void readAll() {

    }

    @Override
    public Page<Message> myMessagePaged(Criteria criteria, Pageable pageable) {
        return null;
    }

    @Override
    public List<Message> myMessage(Criteria criteria) {
        return null;
    }

    @Override
    public Page<Message> myUnreadPaged(Criteria criteria, Pageable pageable) {
        return null;
    }

    @Override
    public List<Message> myUnread(Criteria criteria) {
        return null;
    }


    /*@Override
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
        Criteria c = getMyMessageCriteria(criteria);
        return JpaUtil.linq(InnerMessage.class).where(c).paging(pageable);
    }

    @Override
    public List<Message> myMessage(Criteria criteria){
        Criteria c = getMyMessageCriteria(criteria);
        return JpaUtil.linq(InnerMessage.class).where(c).list();
    }

    @Override
    public Page<Message> myUnreadPaged(Criteria criteria, Pageable pageable){
        Criteria c = getMyUnreadCriteria(criteria);
        return JpaUtil.linq(InnerMessage.class).where(c).paging(pageable);
    }

    @Override
    public List<Message> myUnread(Criteria criteria){
        Criteria c = getMyUnreadCriteria(criteria);
        return JpaUtil.linq(InnerMessage.class).where(c).list();
    }

    private Criteria getMyMessageCriteria(Criteria criteria){
        Criteria c = Criteria.create();
        if(criteria==null){
            return c;
        }
        c.addCriterion(new SimpleCriterion("toUserName", Operator.EQ,SecurityUtils.getLoginUsername()));
        for(Object o:criteria.getCriterions()){
            c.addCriterion(o);
        }
        c.setOrders(criteria.getOrders());
        return c;
    }

    private Criteria getMyUnreadCriteria(Criteria criteria){
        Criteria c = Criteria.create();
        if(criteria==null){
            return c;
        }
        c.addCriterion(new SimpleCriterion("toUserName", Operator.EQ,SecurityUtils.getLoginUsername()));
        c.addCriterion("readStatus", Operator.EQ,"0");
        for(Object o:criteria.getCriterions()){
            c.addCriterion(o);
        }
        c.setOrders(criteria.getOrders());
        return c;
    }*/
}
