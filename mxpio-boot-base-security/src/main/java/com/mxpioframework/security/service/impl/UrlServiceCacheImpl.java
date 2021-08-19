package com.mxpioframework.security.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.common.Constants;
import com.mxpioframework.security.entity.Permission;
import com.mxpioframework.security.entity.Url;
import com.mxpioframework.security.service.UrlServiceCache;

@Service
@Transactional(readOnly = true)
public class UrlServiceCacheImpl implements UrlServiceCache {

	@Override
	@Cacheable(cacheNames = Constants.URL_TREE_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
	public List<Url> findTree() {
		List<Url> result = new ArrayList<Url>();
		List<Url> urls = JpaUtil.linq(Url.class).asc("order").list();
		List<Permission> permissions = JpaUtil.linq(Permission.class).equal("resourceType", Url.RESOURCE_TYPE).list();
		Map<String, Url> urlMap = new HashMap<String, Url>(urls.size());
		Map<String, List<Url>> childrenMap = new HashMap<String, List<Url>>(urls.size());

		for (Url url : urls) {
			urlMap.put(url.getId(), url);
			if (childrenMap.containsKey(url.getId())) {
				url.setChildren(childrenMap.get(url.getId()));
			} else {
				url.setChildren(new ArrayList<Url>());
				childrenMap.put(url.getId(), url.getChildren());
			}

			if (!StringUtils.hasText(url.getParentId())) {
				result.add(url);
			} else {
				List<Url> children;
				if (childrenMap.containsKey(url.getParentId())) {
					children = childrenMap.get(url.getParentId());
				} else {
					children = new ArrayList<Url>();
					childrenMap.put(url.getParentId(), children);
				}
				children.add(url);
			}
		}
		for (Permission permission : permissions) {
			Url url = urlMap.get(permission.getResourceId());
			if(url == null) {
				continue;
			}
			List<ConfigAttribute> configAttributes = url.getAttributes();
			if (configAttributes == null) {
				configAttributes = new ArrayList<ConfigAttribute>();
				url.setAttributes(configAttributes);
			}
			configAttributes.add(permission);
		}

		return result;
	}

}
