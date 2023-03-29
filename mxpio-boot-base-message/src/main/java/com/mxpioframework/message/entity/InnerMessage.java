package com.mxpioframework.message.entity;


import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.security.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Schema(description="站内信")
@Table(name = "MB_INNER_MESSAGE")
@EqualsAndHashCode(callSuper=false)
public class InnerMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Generator
    @Column(name = "ID_", length = 64)
    @Schema(description = "ID")
    private String id;

    @Column(name = "TO_USERNAME_", length = 64)
    @Schema(description = "接收人")
    private String toUserName;

    @Column(name = "TO_NICKNAME_", length = 64)
    @Schema(description = "接收人昵称")
    private String toNickName;

    @Column(name = "FROM_USERNAME_", length = 64)
    @Schema(description = "发送人")
    private String fromUserName;

    @Column(name = "FROM_NICKNAME_", length = 64)
    @Schema(description = "发送人昵称")
    private String fromNickName;

    @Column(name = "MESSAGE_TITLE_", length = 128)
    @Schema(description = "标题")
    private String messageTitle;

    @Column(name = "MESSAGE_CONTENT_", length = 512)
    @Schema(description = "消息内容")
    private String messageContent;

    @Column(name = "READ_STATUS_",length = 1)
    @Schema(description = "阅读状态")
    private String readStatus = "0";

}
