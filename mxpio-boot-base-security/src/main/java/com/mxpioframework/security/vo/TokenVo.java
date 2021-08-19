package com.mxpioframework.security.vo;

import com.mxpioframework.security.entity.User;

import lombok.Data;

@Data
public class TokenVo {
	private String token;
	private User user;
}
