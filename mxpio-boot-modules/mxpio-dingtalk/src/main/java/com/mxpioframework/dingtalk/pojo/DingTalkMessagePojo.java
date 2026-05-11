package com.mxpioframework.dingtalk.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author : wpp
 * @Datetime : 2023/10/23 9:53
 * @Desc :
 * @Modor :  Modifytime:
 * @modDesc :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DingTalkMessagePojo implements Serializable {
    private String msgtype;
    private LinkTypeContent link;
    //todo file/text/image ……
}
