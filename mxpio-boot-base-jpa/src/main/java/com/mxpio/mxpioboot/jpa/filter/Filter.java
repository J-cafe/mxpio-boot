package com.mxpio.mxpioboot.jpa.filter;

import com.mxpio.mxpioboot.jpa.policy.LinqContext;

public interface Filter {
	boolean invoke(LinqContext linqContext);
}
