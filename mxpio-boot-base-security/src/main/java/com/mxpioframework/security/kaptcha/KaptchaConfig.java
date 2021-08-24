package com.mxpioframework.security.kaptcha;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@Component
public class KaptchaConfig {
	
	@Value("${mxpio.kaptcha.border}")
	private String border;
	@Value("${mxpio.kaptcha.textproducer.font.color}")
	private String fontColor;
	@Value("${mxpio.kaptcha.image.width}")
	private String imageWidth;
	@Value("${mxpio.kaptcha.image.height}")
	private String imageHeight;
	@Value("${mxpio.kaptcha.textproducer.font.size}")
	private String fontSize;
	@Value("${mxpio.kaptcha.textproducer.char.length}")
	private String charLenth;
	@Value("${mxpio.kaptcha.textproducer.font.names}")
	private String fontNames;
	
	@Bean
	public DefaultKaptcha getDefaultKaptcha() {
		DefaultKaptcha dk = new DefaultKaptcha();
		Properties properties = new Properties();
		// 图片边框
		properties.setProperty("kaptcha.border", border);
		// 字体颜色
		properties.setProperty("kaptcha.textproducer.font.color", fontColor);
		// 图片宽
		properties.setProperty("kaptcha.image.width", imageWidth);
		// 图片高
		properties.setProperty("kaptcha.image.height", imageHeight);
		// 字体大小
		properties.setProperty("kaptcha.textproducer.font.size", fontSize);
		// 验证码长度
		properties.setProperty("kaptcha.textproducer.char.length", charLenth);
		// 字体
		properties.setProperty("kaptcha.textproducer.font.names", fontNames);
		Config config = new Config(properties);
		dk.setConfig(config);
		return dk;
	}
}
