package com.mxpio.mxpioboot.security.access.provider;

import java.util.Collection;
import java.util.Map;

import com.mxpio.mxpioboot.security.entity.Element;

/**

 * 组件提供者

 * @author Kevin Yang (mailto:kevin.yang@bstek.com)

 * @since 2016年8月6日

 */
public interface ElementProvider {
	Map<String, Collection<Element>> provide();
}
