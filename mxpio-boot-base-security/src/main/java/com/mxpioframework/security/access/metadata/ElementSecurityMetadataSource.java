package com.mxpioframework.security.access.metadata;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;

import com.mxpioframework.security.access.provider.ElementConfigAttributeProvider;
import com.mxpioframework.security.entity.Element;

public class ElementSecurityMetadataSource implements SecurityMetadataSource {
	@Autowired
	private List<ElementConfigAttributeProvider> providers;

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		return getElementMap().get(object.toString());
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();

		for (Map.Entry<String, Collection<ConfigAttribute>> entry : getElementMap().entrySet()) {
			allAttributes.addAll(entry.getValue());
		}

		return allAttributes;
	}

	public Map<String, Collection<ConfigAttribute>> getElementMap() {
		AnnotationAwareOrderComparator.sort(providers);
		Map<String, Collection<ConfigAttribute>> componentMap = new LinkedHashMap<String, Collection<ConfigAttribute>>();
		for (ElementConfigAttributeProvider provider : providers) {
			Map<String, Collection<ConfigAttribute>> map = provider.provide();
			if (map != null && !map.isEmpty()) {
				componentMap.putAll(map);
			}
		}
		return componentMap;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Element.class.isAssignableFrom(clazz);
	}
}
