package com.mxpioframework.message.service.impl;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.message.channel.MessageChannel;
import com.mxpioframework.message.entity.InnerMessage;
import com.mxpioframework.message.service.MessageService;
import com.mxpioframework.message.pojo.MessageChannelVo;
import com.mxpioframework.security.util.SecurityUtils;
import javassist.runtime.Inner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Transactional
    public void read(String msgId){
        InnerMessage message = JpaUtil.getOne(InnerMessage.class,msgId);
        message.setReadStatus("1");
        JpaUtil.update(message);
    }

    @Override
    @Transactional
    public void readAll(){
        List<InnerMessage> messages =JpaUtil.linq(InnerMessage.class).equal("fromUserName", SecurityUtils.getLoginUsername()).equal("readStatus","0").list();
        for(InnerMessage message:messages){
            message.setReadStatus("1");
            JpaUtil.update(message);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InnerMessage> myMessage(Pageable pageable){
        return JpaUtil.linq(InnerMessage.class).equal("fromUserName", SecurityUtils.getLoginUsername()).desc("readStatus","createTime").paging(pageable);
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
