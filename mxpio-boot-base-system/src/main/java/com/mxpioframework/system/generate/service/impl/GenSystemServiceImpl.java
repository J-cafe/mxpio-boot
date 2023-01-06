package com.mxpioframework.system.generate.service.impl;

import org.springframework.stereotype.Service;

import com.mxpioframework.system.generate.entity.GenSystem;
import com.mxpioframework.system.generate.service.GenSystemService;
import com.mxpioframework.system.service.impl.BaseServiceImpl;

@Service("mxpio.system.genSystemService")
public class GenSystemServiceImpl extends BaseServiceImpl<GenSystem> implements GenSystemService {

	@Override
	public boolean generateFilesBySystemCode(String systemCode, String path) {
		return false;
	}


}
