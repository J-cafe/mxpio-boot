package com.mxpio.mxpioboot.security.access.provider.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Component;

import com.mxpio.mxpioboot.security.access.provider.ElementConfigAttributeProvider;
import com.mxpio.mxpioboot.security.common.Constants;
import com.mxpio.mxpioboot.security.entity.Element;
import com.mxpio.mxpioboot.security.service.ElementService;

/**
 * 
 * 默认组件权限信息提供者
 * 
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * 
 * @since 2016年1月24日
 * 
 */
@Component
@Order(100)
public class ElementConfigAttributeProviderImpl implements ElementConfigAttributeProvider {

	@Autowired
	private ElementService elementService;

	@Override
	@Cacheable(cacheNames = Constants.ELEMENT_ATTRIBUTE_MAP_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
	public Map<String, Collection<ConfigAttribute>> provide() {
		List<Element> elements = elementService.findAll();
		Map<String, Collection<ConfigAttribute>> elementMap = new LinkedHashMap<String, Collection<ConfigAttribute>>();
		for (Element element : elements) {
			String key = element.toString();
			Collection<ConfigAttribute> attributes = elementMap.get(key);
			if (attributes == null) {
				attributes = element.getAttributes();
				elementMap.put(key, attributes);
			} else {
				if (element.getAttributes() != null) {
					attributes.addAll(element.getAttributes());
				}
			}

			elementMap.put(element.getPath(), Collections.<ConfigAttribute>emptyList());
		}
		return elementMap;
	}

}