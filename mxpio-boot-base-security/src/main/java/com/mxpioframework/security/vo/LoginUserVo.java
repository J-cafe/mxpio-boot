package com.mxpioframework.security.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="登录用户")
public class LoginUserVo {

	@ApiModelProperty(value = "用户名")
	private String username;

	@ApiModelProperty(value = "密码")
	private String password;
	
	@ApiModelProperty(value = "手机")
	private String phone;
	
	@ApiModelProperty(value = "令牌")
	private String token;
	
}
