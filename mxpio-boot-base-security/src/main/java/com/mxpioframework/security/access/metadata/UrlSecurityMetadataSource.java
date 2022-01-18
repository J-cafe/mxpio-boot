package com.mxpioframework.security.access.metadata;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import com.mxpioframework.security.access.provider.FilterConfigAttributeProvider;

import lombok.extern.slf4j.Slf4j;

/**
 * 菜单安全元数据源
 */
@Slf4j
@Component
public class UrlSecurityMetadataSource  implements FilterInvocationSecurityMetadataSource {

	private Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;
	
	
	@Autowired
	private List<FilterConfigAttributeProvider> providers;
	
	
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();

		for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : getRequestMap()
				.entrySet()) {
			allAttributes.addAll(entry.getValue());
		}

		return allAttributes;
	}

	public Collection<ConfigAttribute> getAttributes(Object object) {
		final HttpServletRequest request = ((FilterInvocation) object).getRequest();
		try {
			for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : getRequestMap()
				.entrySet()) {
				if (entry.getKey().matches(request)) {
					return entry.getValue();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return null;
	}

	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}
	
	public Map<RequestMatcher, Collection<ConfigAttribute>> getRequestMap() {
		AnnotationAwareOrderComparator.sort(providers);
		requestMap = new ConcurrentHashMap<RequestMatcher, Collection<ConfigAttribute>>();
		for (FilterConfigAttributeProvider provider : providers) {
			Map<String, Collection<ConfigAttribute>> map = provider.provide();
			if (map != null && !map.isEmpty()) {
				for (Entry<String, Collection<ConfigAttribute>> entry : map.entrySet()) {
					requestMap.put(new AntPathRequestMatcher("/" + entry.getKey(), null), entry.getValue());
				}
			}
		}
		return requestMap;
	}
}
