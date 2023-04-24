package com.mxpioframework.message.controller;


import com.mxpioframework.common.vo.Result;
import com.mxpioframework.message.entity.Message;
import com.mxpioframework.message.pojo.SendDto;
import com.mxpioframework.message.service.MessageService;
import com.mxpioframework.message.pojo.MessageChannelVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "MessageController", description = "消息管理")
@RestController
@RequestMapping("/message/")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("myMessage/{channelCode}")
    @Operation(summary = "我的消息", description = "我的消息", method = "GET")
    public Result<Page<Message>> myMessage(@Parameter(description="channelCode") @PathVariable(name = "channelCode", required = true) String channelCode,
                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                           @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo){
        Pageable pageAble = PageRequest.of(pageNo - 1, pageSize);
        return Result.OK(messageService.myMessage(channelCode,pageAble));
    }

    @GetMapping("channel/list")
    @Operation(summary = "所有发送渠道", description = "所有发送渠道", method = "GET")
    public Result<List<MessageChannelVo>> list(){
        return Result.OK(messageService.getAllChannel());
    }

    @PostMapping("send")
    @Operation(summary = "发送消息", description = "发送消息", method = "POST")
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

    @GetMapping("read/{channelCode}/{id}")
    @Operation(summary = "消息已读", description = "消息已读", method = "GET")
    public Result<String> read(@Parameter(description="channelCode") @PathVariable(name = "channelCode", required = true) String channelCode,
                               @Parameter(description="ID") @PathVariable(name = "id", required = true) String id){
        messageService.read(channelCode,id);
        return Result.OK();
    }

    @GetMapping("readAll/{channelCode}")
    @Operation(summary = "一键已读", description = "一键已读", method = "GET")
    public Result<String> readAll(@Parameter(description="channelCode") @PathVariable(name = "channelCode", required = true) String channelCode){
        messageService.readAll(channelCode);
        return Result.OK();
    }
}
