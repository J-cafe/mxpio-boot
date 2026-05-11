package com.mxpioframework.security.captcha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class CaptchaProperties {

	@Value("${mxpio.captcha.open}")
	private Boolean open;
	
	@Value("${mxpio.captcha.border}")
	private String border;
	
	@Value("${mxpio.captcha.textproducer.font.names}")
	private String fontNames;
	
	@Value("${mxpio.captcha.textproducer.font.color}")
	private String fontColor;
	
	@Value("${mxpio.captcha.textproducer.font.size}")
	private String fontSize;
	
	@Value("${mxpio.captcha.textproducer.char.length}")
	private String charLength;
	
	@Value("${mxpio.captcha.image.width}")
	private String width;
	
	@Value("${mxpio.captcha.image.height}")
	private String height;
}
