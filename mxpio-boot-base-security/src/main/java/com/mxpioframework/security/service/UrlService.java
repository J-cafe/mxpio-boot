package com.mxpioframework.security.service;

import java.util.List;

import com.mxpioframework.security.entity.Url;

/**
 * 菜单服务接口
 */
public interface UrlService extends BaseService<Url> {
	
	/**

	 * 获取所有菜单（包含权限信息）

	 * @return 所有菜单（包含权限信息）

	 */
	List<Url> findAll();
	
	/**
	 * 获取所有菜单
	 * @return 所有菜单
	 */
	public List<Url> findAllTree();

	/**

	 * 获取用户有权访问的菜单，已树形结构返回(基于当前登录用户)

	 * @param username 用户名

	 * @return 菜单列表

	 */
	List<Url> findTreeByUsername(String username);

	/**

	 * 获取用户有权访问的菜单，已树形结构返回（基于传入的用户名）

	 * @param username 用户名

	 * @return 菜单列表

	 */
	List<Url> getAccessibleUrlsByUsername(String username);

	boolean deleteBundleById(String id);

}
