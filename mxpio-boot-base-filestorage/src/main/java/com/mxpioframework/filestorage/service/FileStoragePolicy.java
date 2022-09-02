package com.mxpioframework.filestorage.service;

import java.util.Map;

public interface FileStoragePolicy {

	void apply(Map<String, Object> result);

	String getName();

	boolean support(Map<String, Object> parameter);

}
