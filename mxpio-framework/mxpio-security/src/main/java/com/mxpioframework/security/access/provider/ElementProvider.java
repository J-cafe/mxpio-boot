package com.mxpioframework.security.access.provider;

import java.util.Collection;
import java.util.Map;

import com.mxpioframework.security.entity.Element;

/**
 * 组件提供者
 */
public interface ElementProvider {
	Map<String, Collection<Element>> provide();
}
