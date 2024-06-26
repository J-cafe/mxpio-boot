package com.mxpioframework.security.access.provider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.mxpioframework.security.access.provider.ElementProvider;
import com.mxpioframework.security.common.Constants;
import com.mxpioframework.security.entity.Element;
import com.mxpioframework.security.service.ElementService;

/**
 * 默认组件权提供者
 */
@Component
@Order(100)
public class ElementProviderImpl implements ElementProvider {

	@Autowired
	private ElementService elementService;
	
	@Override
	@Cacheable(cacheNames = Constants.ELEMENT_MAP_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
	public Map<String, Collection<Element>> provide() {
		List<Element> elements = elementService.findAll();
		Map<String, Collection<Element>> elementMap = new LinkedHashMap<>();
		for (Element element : elements) {
			String key = element.getPath();
			Collection<Element> cs = elementMap.computeIfAbsent(key,k -> new ArrayList<>());
			if (cs.isEmpty()) {
				elementMap.put(key, cs);
			}
			cs.add(element);
		}
		return elementMap;
	}

}