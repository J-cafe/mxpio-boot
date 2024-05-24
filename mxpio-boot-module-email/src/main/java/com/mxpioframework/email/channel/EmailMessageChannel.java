package com.mxpioframework.email.channel;

import com.mxpioframework.common.exception.MBootException;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.message.entity.Message;
import com.mxpioframework.security.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


@Component
@Slf4j
public class EmailMessageChannel extends EmailAbstractMessageChannel {

    private static final String CHANNEL_CODE = "emailMsg";

    private static final String CHANNEL_NAME = "邮件消息";

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

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
        //创建一个MINE消息
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
            from = this.from;
            //谁发
            minehelper.setFrom(from);
            log.info("发送HTML邮件from:"+from);
            //谁要接收 收件人接收系统用户
            log.info("发送HTML邮件to(原始):"+to);
            List<String> toEmailAddressList = new ArrayList<>();
            for (String per:to){
                Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
                if (StringUtils.isNotBlank(per)&&pattern.matcher(per).matches()){
                    toEmailAddressList.add(per);
                }else {
                    User user = JpaUtil.getOne(User.class, per);
                    if (user != null){
                        toEmailAddressList.add( user.getEmail());
                    }
                }
            }
            if (toEmailAddressList.isEmpty()){
                log.error("有效的收件人为空");
                throw new MBootException("有效的收件人为空");
            }
            minehelper.setTo(toEmailAddressList.stream().toArray(String[]::new));
            log.info("发送HTML邮件to(转换):"+StringUtils.join(toEmailAddressList, ","));
            //邮件主题
            minehelper.setSubject(title);
            log.info("发送HTML邮件title:"+title);
            //邮件内容   true 表示带有附件或html
            minehelper.setText(content, true);
            log.info("发送HTML邮件content:"+content);
            mailSender.send(message);
            log.info("发送HTML邮件成功。");
        } catch (MailException e) {
            e.printStackTrace();
            log.error("HTML邮件发送失败"+e.getMessage());
        } catch (MessagingException e) {
            log.error("HTML邮件发送失败"+e.getMessage());
            e.printStackTrace();
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
