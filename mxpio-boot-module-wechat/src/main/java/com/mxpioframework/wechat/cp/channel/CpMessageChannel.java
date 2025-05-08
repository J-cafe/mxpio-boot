package com.mxpioframework.wechat.cp.channel;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.message.channel.impl.AbstractMessageChannel;
import com.mxpioframework.message.entity.Message;
import com.mxpioframework.wechat.cp.config.single.WxCpConfiguration;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpMessageService;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpMessageServiceImpl;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 企业微信消息发送
 * https://github.com/binarywang/WxJava/wiki/CP_%E4%B8%BB%E5%8A%A8%E5%8F%91%E9%80%81%E6%B6%88%E6%81%AF sdk开发文档说明地址
 */
@Component
@Slf4j
public class CpMessageChannel extends AbstractMessageChannel {

    private static final String CHANNEL_CODE = "CpMsg";

    private static final String CHANNEL_NAME = "企业微信消息";

    @Value("${wechat.cp.appConfigs[0].agentId}")
    private Integer agentId;

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
    public void doSend(String from, String[] to, String title, String content,String businessKey) {
        String toUserIds = StringUtils.join(to, "|");//此处的to取对应user的thirdId
        try {
            final WxCpService wxCpService = WxCpConfiguration.getCpService(agentId);
            WxCpMessageService wxCpMessageService = new WxCpMessageServiceImpl(wxCpService);
            WxCpMessage wxCpMessage = WxCpMessage
                    .TEXT()
                    .agentId(agentId) // 企业号应用ID
                    .toUser(toUserIds)
                    //.toParty("非必填，PartyID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数")
                    .toTag(title)
                    .content(content)
                    .build();
            wxCpMessageService.send(wxCpMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
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

}
