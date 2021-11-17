package com.mxpioframework.security.kaptcha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class KaptchaProperties {

	@Value("${mxpio.kaptcha.open}")
	private Boolean open;
	
	@Value("${mxpio.kaptcha.border}")
	private String border;
	
	@Value("${mxpio.kaptcha.textproducer.font.names}")
	private String fontNames;
	
	@Value("${mxpio.kaptcha.textproducer.font.color}")
	private String fontColor;
	
	@Value("${mxpio.kaptcha.textproducer.font.size}")
	private String fontSize;
	
	@Value("${mxpio.kaptcha.textproducer.char.length}")
	private String charLength;
	
	@Value("${mxpio.kaptcha.image.width}")
	private String width;
	
	@Value("${mxpio.kaptcha.image.height}")
	private String height;
}
