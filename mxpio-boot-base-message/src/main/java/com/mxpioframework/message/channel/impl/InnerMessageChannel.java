package com.mxpioframework.message.channel.impl;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.message.entity.InnerMessage;
import com.mxpioframework.security.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 站内信
 */
@Component
public class InnerMessageChannel extends AbstractMessageChannel {

    private static final String CHANNEL_CODE = "innerMsg";

    private static final String CHANNEL_NAME = "站内信";

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
        }
    }
}
