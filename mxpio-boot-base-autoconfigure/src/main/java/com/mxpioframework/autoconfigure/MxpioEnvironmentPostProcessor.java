package com.mxpioframework.autoconfigure;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * 加载模块配置文件
 * @author MxpIO
 *
 */
public class MxpioEnvironmentPostProcessor implements EnvironmentPostProcessor {

	private final Properties properties = new Properties();

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		String[] profiles = { "classpath*:mxpio/mxpio.properties" };

		for (String profile : profiles) {
			ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
			try {
				Resource[] resources = resourcePatternResolver.getResources(profile);
				Collections.reverse(Arrays.asList(resources));
				for (Resource resource : resources) {
					environment.getPropertySources().addFirst(loadProfiles(resource));
				}
			} catch (IOException e) {
				throw new IllegalStateException("加载配置文件失败" + profile, e);
			}
		}
	}

	// 加载单个配置文件
	private PropertySource<?> loadProfiles(Resource resource) {
		if (!resource.exists()) {
			throw new IllegalArgumentException("资源" + resource + "不存在");
		}
		try {
			// 从输入流中加载一个Properties对象
			properties.load(resource.getInputStream());
			return new PropertiesPropertySource(resource.getFilename(), properties);
		} catch (IOException ex) {
			throw new IllegalStateException("加载配置文件失败" + resource, ex);
		}
	}

}
