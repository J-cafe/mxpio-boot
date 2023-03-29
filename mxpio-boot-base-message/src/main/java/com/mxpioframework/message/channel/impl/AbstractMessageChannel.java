package com.mxpioframework.message.channel.impl;

import com.mxpioframework.message.channel.MessageChannel;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractMessageChannel implements MessageChannel {
    @Override
    @Transactional
    public void send(String from, String[] to, String title, String content) {
        if (!beforeSend(from,to,title,content)) {
            throw new IllegalArgumentException("前置校验不通过");
        }
        doSend(from,to,title,content);

    }
    @Override
    public boolean beforeSend(String from, String[] to, String title, String content){
        return StringUtils.isBlank(from) || ArrayUtils.isEmpty(to) || StringUtils.isBlank(title) || StringUtils.isBlank(content);
    }

    @Override
    public void afterSend(String from,String [] to,String title,String msg){

    }
}
