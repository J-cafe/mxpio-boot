package com.mxpioframework.system.service;

import com.alibaba.fastjson.JSONObject;

public interface SnRuleService {

	Object execute(String expression, JSONObject formData);
}
