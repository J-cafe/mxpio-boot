package com.mxpio.mxpioboot.security.service;

import java.util.List;

import com.mxpio.mxpioboot.security.entity.Element;

public interface ElementService extends BaseService<Element> {

	/**

	 * 获取所有的组件（包含权限信息）

	 * @return 所有组件（包含权限信息）

	 */
	List<Element> findAll();
}
