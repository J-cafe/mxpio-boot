package com.mxpioframework.expression.context;

import java.util.HashMap;

public class ElContext extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public void set(String key, Object value) {
		this.put(key, value);
	}

	public boolean has(String key) {
		return this.containsKey(key);
	}

}
