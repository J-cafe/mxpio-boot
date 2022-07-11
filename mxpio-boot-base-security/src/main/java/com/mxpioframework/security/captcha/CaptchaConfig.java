package com.mxpioframework.security.captcha;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.mxpioframework.security.captcha.impl.DefaultCaptcha;
import com.mxpioframework.security.captcha.util.Config;

@Component
public class CaptchaConfig {
	
	@Autowired
	private CaptchaProperties captchaProperties;
	
	@Bean
	public DefaultCaptcha getDefaultCaptcha() {
		DefaultCaptcha dk = new DefaultCaptcha();
		Properties properties = new Properties();
		// 图片边框
		properties.setProperty("captcha.border", captchaProperties.getBorder());
		// 字体颜色
		properties.setProperty("captcha.textproducer.font.color", captchaProperties.getFontColor());
		// 图片宽
		properties.setProperty("captcha.image.width", captchaProperties.getWidth());
		// 图片高
		properties.setProperty("captcha.image.height", captchaProperties.getHeight());
		// 字体大小
		properties.setProperty("captcha.textproducer.font.size", captchaProperties.getFontSize());
		// 验证码长度
		properties.setProperty("captcha.textproducer.char.length", captchaProperties.getCharLength());
		// 字体
		properties.setProperty("captcha.textproducer.font.names", captchaProperties.getFontNames());
		Config config = new Config(properties);
		dk.setConfig(config);
		return dk;
	}
}
