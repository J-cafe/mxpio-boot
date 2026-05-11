package com.mxpioframework.dingtalk.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author : wpp
 * @Datetime : 2023/10/23 9:55
 * @Desc :
 * @Modor :  Modifytime:
 * @modDesc :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkTypeContent implements Serializable {
    private String messageUrl;
    private String picUrl;
    private String title;
    private String text;
}
