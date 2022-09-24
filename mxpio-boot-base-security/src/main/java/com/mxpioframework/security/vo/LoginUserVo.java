package com.mxpioframework.security.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description="登录用户")
public class LoginUserVo {

	@Schema(description = "用户名")
	private String username;

	@Schema(description = "密码")
	private String password;
	
	@Schema(description = "手机")
	private String phone;
	
	@Schema(description = "令牌")
	private String token;
	
}
