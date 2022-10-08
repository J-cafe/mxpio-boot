package com.mxpioframework.security.service;

import java.util.List;
import java.util.Map;

import com.mxpioframework.security.entity.DataResource;
import com.mxpioframework.security.vo.DataVo;

public interface DataResourceService extends BaseService<DataResource> {

	/**
	 * 获取全部接口
	 * @return
	 */
	List<DataVo> findAll();

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

}
