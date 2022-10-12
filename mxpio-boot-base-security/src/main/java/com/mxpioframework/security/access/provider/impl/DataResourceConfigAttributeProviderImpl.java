package com.mxpioframework.security.access.provider.impl;

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

import com.mxpioframework.security.access.provider.DataResourceConfigAttributeProvider;
import com.mxpioframework.security.common.Constants;
import com.mxpioframework.security.entity.DataResource;
import com.mxpioframework.security.service.DataResourceService;

/**
 * 默认数据权限信息提供者
 */
@Component
@Order(100)
public class DataResourceConfigAttributeProviderImpl implements DataResourceConfigAttributeProvider {

	@Autowired
	private DataResourceService dataResourceService;

	@Override
	@Cacheable(cacheNames = Constants.DATA_ATTRIBUTE_MAP_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
	public Map<String, Collection<ConfigAttribute>> provide() {
		List<DataResource> datas = dataResourceService.findAll();
		Map<String, Collection<ConfigAttribute>> dataMap = new LinkedHashMap<String, Collection<ConfigAttribute>>();
		for (DataResource data : datas) {
			String key = data.toString();
			Collection<ConfigAttribute> attributes = dataMap.get(key);
			if (attributes == null) {
				attributes = data.getAttributes();
				dataMap.put(key, attributes);
			} else {
				if (data.getAttributes() != null) {
					attributes.addAll(data.getAttributes());
				}
			}

			dataMap.put(data.getPath(), Collections.<ConfigAttribute>emptyList());
		}
		return dataMap;
	}

}