package com.mxpioframework.security.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description="修改密码")
public class UpatePassVo {

	@Schema(description = "用户名")
	private String username;

	@Schema(description = "旧密码")
	private String oldPassword;
	
	@Schema(description = "新密码")
	private String newPassword;
	
}
