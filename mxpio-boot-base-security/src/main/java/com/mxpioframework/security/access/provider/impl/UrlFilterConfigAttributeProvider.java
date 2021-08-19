package com.mxpioframework.security.access.provider.impl;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mxpioframework.security.access.provider.FilterConfigAttributeProvider;
import com.mxpioframework.security.common.Constants;
import com.mxpioframework.security.entity.Url;
import com.mxpioframework.security.service.UrlService;

/**
 * 默认菜单权限信息提供者
 */
@Component
@Order(100)
public class UrlFilterConfigAttributeProvider implements
		FilterConfigAttributeProvider  {
	
	@Autowired(required = true)
	private UrlService urlService;
	

	@Override
	@Cacheable(cacheNames = Constants.REQUEST_MAP_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
	public Map<String, Collection<ConfigAttribute>> provide() {
		List<Url> urls = urlService.findAll();
		Map<String, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<String, Collection<ConfigAttribute>>();
		for (Url url : urls) {
			if (validate(url)) {
				requestMap.put(url.getPath(), url.getAttributes());
			}
		}
		return requestMap;
	}

	protected boolean validate(Url url) {
		if (StringUtils.hasText(url.getPath())) {
			return false;
		}
		return true;
	}

}