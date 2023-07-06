package com.mxpioframework.system.generate.service;

import java.io.IOException;

import com.mxpioframework.system.generate.entity.GenModel;
import com.mxpioframework.system.service.BaseService;

public interface GenModelService extends BaseService<GenModel> {
	
	public String generateFilesByModelCode(String modelCode) throws IOException;

}
