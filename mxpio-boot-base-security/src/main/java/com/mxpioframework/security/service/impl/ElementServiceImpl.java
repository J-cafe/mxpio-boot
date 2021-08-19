package com.mxpioframework.security.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.entity.Element;
import com.mxpioframework.security.entity.Permission;
import com.mxpioframework.security.service.ElementService;

@Service
@Transactional(readOnly = true)
public class ElementServiceImpl extends BaseServiceImpl<Element> implements ElementService {

	@Override
	public List<Element> findAll() {
		List<Element> elements = JpaUtil.linq(Element.class).list();
		List<Permission> permissions = JpaUtil.linq(Permission.class).equal("resourceType", Element.RESOURCE_TYPE).list();
		if (!permissions.isEmpty()) {
			Map<String, Element> elementMap = JpaUtil.index(elements);
			for (Permission permission : permissions) {
				Element element = elementMap.get(permission.getResourceId());
				List<ConfigAttribute> configAttributes = element.getAttributes();
				if (configAttributes == null) {
					configAttributes = new ArrayList<ConfigAttribute>();
					element.setAttributes(configAttributes);
				}
				configAttributes.add(permission);
			}
		}
		return elements;

	}

}
