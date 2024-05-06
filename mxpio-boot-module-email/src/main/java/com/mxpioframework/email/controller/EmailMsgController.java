package com.mxpioframework.email.controller;


import com.mxpioframework.common.vo.Result;
import com.mxpioframework.message.pojo.SendDto;
import com.mxpioframework.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "EmailMsgController", description = "邮件消息")
@RestController
@RequestMapping("/email/")
public class EmailMsgController {

    @Autowired
    private MessageService messageService;


    /* 测试body入参
    {
        "channel":"emailMsg",
        "to":"252437792@qq.com",
        "title":"测试邮件发下",
        "content":"这里是邮件内容，<a href='#'>test</a>"
    }
    */
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
}
