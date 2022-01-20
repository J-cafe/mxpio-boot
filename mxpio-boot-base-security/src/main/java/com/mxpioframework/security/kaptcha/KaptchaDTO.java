package com.mxpioframework.security.kaptcha;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="验证码对象")
public class KaptchaDTO {
	
	@ApiModelProperty(value = "验证码code")
	private String code;
	
	@ApiModelProperty(value = "验证码图片")
	private String image;

}
