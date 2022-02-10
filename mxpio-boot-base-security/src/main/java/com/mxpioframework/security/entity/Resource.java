package com.mxpioframework.security.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.security.access.ConfigAttribute;

public interface Resource extends Serializable {

	/**

	 * 提供资源对于的权限信息

	 * @return 权限信息

	 */
	List<ConfigAttribute> getAttributes();
	
	ResourceType getResourceType();
}
