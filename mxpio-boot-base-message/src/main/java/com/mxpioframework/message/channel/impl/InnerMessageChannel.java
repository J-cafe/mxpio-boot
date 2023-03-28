package com.mxpioframework.message.channel.impl;

import com.mxpioframework.common.exception.MBootException;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.message.channel.MessageChannel;
import com.mxpioframework.message.entity.InnerMessage;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.UserService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 站内信
 */
@Component
public class InnerMessageChannel implements MessageChannel {

    private static final String CHANNEL_CODE = "Inner";

    private static final String CHANNEL_NAME = "站内信";

    private final UserService userService;

    @Autowired
    public InnerMessageChannel(UserService userService){
        super();
        this.userService = userService;
    }


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
    public void send(String from, String[] to, String title, String content) {
        if(StringUtils.isBlank(from)|| ArrayUtils.isEmpty(to)||StringUtils.isBlank(title)||StringUtils.isBlank(content)){
            throw new IllegalArgumentException("参数非法");
        }
        User fromUser = userService.findByName(from);
        Set<String> toSet = new HashSet<>(Arrays.asList(to));
        for(String toUserName:toSet){
            User toUser = userService.findByName(toUserName);
            InnerMessage innerMessage = new InnerMessage();
            innerMessage.setToUserName(toUserName);
            innerMessage.setToNickName(toUser.getNickname());
            innerMessage.setFromUserName(from);
            innerMessage.setFromNickName(fromUser.getNickname());
            innerMessage.setMessageTitle(title);
            innerMessage.setMessageContent(content);
            JpaUtil.save(innerMessage);
        }
    }
}
