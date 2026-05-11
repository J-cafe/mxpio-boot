package com.mxpioframework.security.service;

import java.util.List;

import com.mxpioframework.security.entity.Url;

/**
 * 主要内部使用，在接口代理模式下，实现缓存代理的中间类
 */
public interface UrlServiceCache {
	/**

	 * 获取所有菜单，以树形结构返回

	 * @return 所有菜单

	 */
	List<Url> findTree();
}
