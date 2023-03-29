package com.mxpioframework.message.service.impl;

import com.mxpioframework.message.channel.MessageChannel;
import com.mxpioframework.message.service.MessageService;
import com.mxpioframework.message.pojo.MessageChannelVo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService, ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private Collection<MessageChannel> channels;

    @Override
    @Transactional(readOnly = true)
    public List<MessageChannelVo> getAllChannel() {
        List<MessageChannelVo> channelVOList = new ArrayList<>();
        for(MessageChannel channel:channels){
            MessageChannelVo vo = new MessageChannelVo();
            vo.setChannelCode(channel.getChannelCode());
            vo.setChannelName(channel.getChannelName());
            channelVOList.add(vo);
        }
        return channelVOList;
    }

    @Override
    @Transactional
    public void sendMessage(String [] channelCodes, String from, String[] to, String title, String content) {
        for(String channelCode:channelCodes){
            for(MessageChannel channel:channels){
                if(channel.support(channelCode)){
                    channel.send(from,to,title,content);
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        channels = applicationContext.getBeansOfType(MessageChannel.class).values();
    }
}
