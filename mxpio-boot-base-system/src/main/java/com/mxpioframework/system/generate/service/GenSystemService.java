package com.mxpioframework.system.generate.service;

import com.mxpioframework.system.generate.entity.GenSystem;
import com.mxpioframework.system.service.BaseService;

public interface GenSystemService extends BaseService<GenSystem> {
	
	/**
	 * 根据系统编号生成代码
	 * @param systemCode
	 * @param path
	 * @return
	 */
	public boolean generateFilesBySystemCode(String systemCode, String path);

}
