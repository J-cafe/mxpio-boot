package com.mxpioframework.security.access.metadata;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.stereotype.Component;

import com.mxpioframework.security.access.provider.DataResourceConfigAttributeProvider;
import com.mxpioframework.security.entity.DataResource;

@Component
public class DataResourceSecurityMetadataSource implements SecurityMetadataSource {
	@Autowired
	private List<DataResourceConfigAttributeProvider> providers;

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		Collection<ConfigAttribute> result = Collections.emptyList();
		if(object instanceof DataResource){
			result = getDataResourceMap().get(((DataResource) object).getPath());
		}
		return result;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();

		for (Map.Entry<String, Collection<ConfigAttribute>> entry : getDataResourceMap().entrySet()) {
			allAttributes.addAll(entry.getValue());
		}

		return allAttributes;
	}

	public Map<String, Collection<ConfigAttribute>> getDataResourceMap() {
		AnnotationAwareOrderComparator.sort(providers);
		Map<String, Collection<ConfigAttribute>> dataResourceMap = new LinkedHashMap<String, Collection<ConfigAttribute>>();
		for (DataResourceConfigAttributeProvider provider : providers) {
			Map<String, Collection<ConfigAttribute>> map = provider.provide();
			if (map != null && !map.isEmpty()) {
				dataResourceMap.putAll(map);
			}
		}
		return dataResourceMap;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return DataResource.class.isAssignableFrom(clazz);
	}
}
