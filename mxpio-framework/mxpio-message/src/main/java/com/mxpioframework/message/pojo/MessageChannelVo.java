package com.mxpioframework.message.pojo;

import java.io.Serializable;

public class MessageChannelVo implements Serializable {
    private static final long serialVersionUID = -1L;

    private String channelCode;

    private String channelName;

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
