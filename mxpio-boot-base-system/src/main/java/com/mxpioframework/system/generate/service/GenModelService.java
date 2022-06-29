package com.mxpioframework.system.generate.service;

import com.mxpioframework.system.generate.entity.GenModel;
import com.mxpioframework.system.service.BaseService;

public interface GenModelService extends BaseService<GenModel> {
	
	public boolean generateFilesByModelId(String modelId, String path);

}
