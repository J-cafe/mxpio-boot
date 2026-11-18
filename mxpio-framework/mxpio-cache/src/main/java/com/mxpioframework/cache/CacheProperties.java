package com.mxpioframework.cache;

import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

/**
 * 缓存配置属性
 * <p>
 * keyPrefix 用于给所有写入缓存中间件（如Redis）的key加上应用级命名空间，
 * 避免多个应用共用同一缓存实例时互相冲突。
 * 未配置时默认取 spring.application.name，最终统一以 ":" 结尾。
 */
@ConfigurationProperties(prefix = "mxpio.cache")
public class CacheProperties implements ApplicationContextAware {

	public static final String DEFAULT_KEY_PREFIX = "mxpio";

	private String keyPrefix;

	private ApplicationContext applicationContext;

	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	/**
	 * 解析最终生效的key前缀：优先 mxpio.cache.key-prefix，缺省回退 spring.application.name，
	 * 保证以 ":" 结尾
	 */
	public String resolveKeyPrefix() {
		String prefix = keyPrefix;
		if (!StringUtils.hasText(prefix) && applicationContext != null) {
			prefix = applicationContext.getEnvironment().getProperty("spring.application.name");
		}
		if (!StringUtils.hasText(prefix)) {
			prefix = DEFAULT_KEY_PREFIX;
		}
		prefix = prefix.trim();
		return prefix.endsWith(":") ? prefix : prefix + ":";
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
