package com.mxpioframework.security.common;

import org.springframework.core.Ordered;

public final class Constants {

	/**

	 * 所有菜单权限缓存关键字

	 */
	public final static String REQUEST_MAP_CACHE_KEY = "RequestMap";
	/**

	 * 特定菜单权限缓存关键字

	 */
	public final static String URL_ATTRIBUTE_BY_TARGET_CACHE_KEY = "UrlAttributeByTargetCacheKey";
	/**

	 * 树形菜单缓存关键字

	 */
	public final static String URL_TREE_CACHE_KEY = "UrlTree";
	/**

	 * 特定用户下的树形菜单缓存关键字

	 */
	public final static String[] URL_SECURITY = new String[] {REQUEST_MAP_CACHE_KEY, URL_ATTRIBUTE_BY_TARGET_CACHE_KEY, URL_TREE_CACHE_KEY};

	/**

	 * 所有组件权限缓存关键字

	 */
	public final static String ELEMENT_ATTRIBUTE_MAP_CACHE_KEY = "ElementAttributeMap";
	
	/**

	 * 所有组件缓存关键字

	 */
	public final static String ELEMENT_MAP_CACHE_KEY = "ElementMap";
	
	/**

	 * 特定组件权限缓存关键字

	 */
	public final static String ELEMENT_ATTRIBUTE_BY_TARGET_CACHE_KEY = "ElementAttributeByTargetCacheKey";
	public final static String[] ELEMENT_SECURITY = new String[] {ELEMENT_ATTRIBUTE_MAP_CACHE_KEY};
	
	public final static String[] URL_ELEMENT_SECURITY = new String[] {REQUEST_MAP_CACHE_KEY, URL_ATTRIBUTE_BY_TARGET_CACHE_KEY, URL_TREE_CACHE_KEY, ELEMENT_ATTRIBUTE_MAP_CACHE_KEY};

	public static final String KEY_GENERATOR_BEAN_NAME = "securityKeyGenerator";
	
	public static final int SECURITY_CONFIGURER_ORDER = Ordered.LOWEST_PRECEDENCE - 1000;
	
}
