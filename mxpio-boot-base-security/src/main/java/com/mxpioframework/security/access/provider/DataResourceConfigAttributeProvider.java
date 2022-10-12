package com.mxpioframework.security.access.provider;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;

/**
 * 数据权限信息提供者
 */
public interface DataResourceConfigAttributeProvider {
	/**

	 * 提供组件权限信息，以Map结构返回，Key为数据唯一标示<br>

	 * Value为对应的权限信息

	 * @return 权限信息

	 */
	Map<String, Collection<ConfigAttribute>> provide();
}
