package com.mxpioframework.filestorage.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.mxpioframework.filestorage.service.FileStoragePolicy;

@Service("mxpio.defaultFileStoragePolicy")
public class DefaultFileStoragePolicy implements FileStoragePolicy {

	@Override
	public void apply(Map<String, Object> result) {

	}

	@Override
	public String getName() {
		return "初始化";
	}

	@Override
	public boolean support(Map<String, Object> parameter) {
		return false;
	}

}
