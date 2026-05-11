package com.mxpioframework.jpa.filter;

import com.mxpioframework.jpa.policy.LinqContext;

public interface Filter {
	boolean invoke(LinqContext linqContext);
}
