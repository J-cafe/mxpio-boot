package com.mxpio.mxpioboot.security.vo;

import lombok.Data;

@Data
public class LoginUserVo {

	private String username;

	private String password;
	
	private String phone;
	
	private String token;
	
}
