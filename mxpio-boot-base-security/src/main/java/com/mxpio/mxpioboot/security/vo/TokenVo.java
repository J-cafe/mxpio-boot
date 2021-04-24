package com.mxpio.mxpioboot.security.vo;

import com.mxpio.mxpioboot.security.entity.User;

import lombok.Data;

@Data
public class TokenVo {
	private String token;
	private User user;
}
