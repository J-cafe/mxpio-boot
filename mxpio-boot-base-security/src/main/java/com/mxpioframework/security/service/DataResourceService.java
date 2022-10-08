package com.mxpioframework.security.service;

import java.util.List;
import java.util.Map;

import com.mxpioframework.security.entity.DataResource;
import com.mxpioframework.security.vo.DataVo;

public interface DataResourceService extends BaseService<DataResource> {

	List<DataVo> findAll();

	Map<String, DataResource> findAllByCatch();

}
