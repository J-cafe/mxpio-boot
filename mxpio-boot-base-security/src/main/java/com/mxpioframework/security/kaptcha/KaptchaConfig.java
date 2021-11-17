package com.mxpioframework.security.kaptcha;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@Component
public class KaptchaConfig {
	
	@Autowired
	private KaptchaProperties kaptchaProperties;
	
	@Bean
	public DefaultKaptcha getDefaultKaptcha() {
		DefaultKaptcha dk = new DefaultKaptcha();
		Properties properties = new Properties();
		// 图片边框
		properties.setProperty("kaptcha.border", kaptchaProperties.getBorder());
		// 字体颜色
		properties.setProperty("kaptcha.textproducer.font.color", kaptchaProperties.getFontColor());
		// 图片宽
		properties.setProperty("kaptcha.image.width", kaptchaProperties.getWidth());
		// 图片高
		properties.setProperty("kaptcha.image.height", kaptchaProperties.getHeight());
		// 字体大小
		properties.setProperty("kaptcha.textproducer.font.size", kaptchaProperties.getFontSize());
		// 验证码长度
		properties.setProperty("kaptcha.textproducer.char.length", kaptchaProperties.getCharLength());
		// 字体
		properties.setProperty("kaptcha.textproducer.font.names", kaptchaProperties.getFontNames());
		Config config = new Config(properties);
		dk.setConfig(config);
		return dk;
	}
}
