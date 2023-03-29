package com.mxpioframework.message.controller;


import com.mxpioframework.common.vo.Result;
import com.mxpioframework.message.pojo.SendDto;
import com.mxpioframework.message.service.MessageService;
import com.mxpioframework.message.pojo.MessageChannelVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "MessageController", description = "消息管理")
@RestController("mxpio.security.deptController")
@RequestMapping("/message/")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("channel/list")
    public Result<List<MessageChannelVo>> list(){
        return Result.OK(messageService.getAllChannel());
    }

    @PostMapping("send")
    public Result<String> send(@RequestBody SendDto sendDto){
        try{
            messageService.sendMessage(StringUtils.split(sendDto.getChannel(),","),
                    sendDto.getFrom(), StringUtils.split(sendDto.getTo(),","), sendDto.getTitle(), sendDto.getContent());
            return Result.OK();
        }
        catch (Exception e){
            return Result.error(e.getMessage());
        }

    }




}
