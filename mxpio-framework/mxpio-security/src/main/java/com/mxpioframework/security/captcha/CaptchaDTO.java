package com.mxpioframework.security.captcha;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description="验证码对象")
public class CaptchaDTO {
	
	@Schema(description = "验证码code")
	private String code;
	
	@Schema(description = "验证码图片")
	private String image;

}
