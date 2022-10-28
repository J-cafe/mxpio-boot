package com.mxpioframework.jpa.annotation;

import java.util.Map;

public interface DictAble {

	public String putText(String key, String value);
	
	public Map<String, String> getTextMap();
	
}
