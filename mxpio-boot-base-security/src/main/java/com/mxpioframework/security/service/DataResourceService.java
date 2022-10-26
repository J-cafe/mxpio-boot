package com.mxpioframework.security.service;

import java.util.List;
import java.util.Map;

import com.mxpioframework.security.entity.DataResource;
import com.mxpioframework.security.vo.DataVo;

public interface DataResourceService extends BaseService<DataResource> {

	/**
	 * 获取全部接口
	 * @param onlyCriteria
	 * @param path
	 * @return
	 */
	List<DataVo> findAllApi(boolean onlyCriteria, String path);

	/**
	 * 数据资源索引
	 * @return
	 */
	Map<String, DataResource> findAllByCatch();

	/**
	 * 根据urlId获取数据资源
	 * @param urlId
	 * @return
	 */
	List<DataResource> getByUrlId(String urlId);

	/**
	 * 获取所有数据权限
	 * @return
	 */
	List<DataResource> findAll();

	/**
	 * 获取用户有权访问的接口(基于当前登录用户)
	 * @param username 用户名
	 * @return 权限列表
	 */
	List<DataResource> findByUsername(String username);

}
