package com.mxpio.mxpioboot.security.access.provider;

import java.util.Collection;
import java.util.Map;

import com.mxpio.mxpioboot.security.entity.Element;

/**
 * 组件提供者
 */
public interface ElementProvider {
	Map<String, Collection<Element>> provide();
}
