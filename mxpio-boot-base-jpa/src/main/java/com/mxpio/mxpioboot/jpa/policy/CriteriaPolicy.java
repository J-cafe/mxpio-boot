package com.mxpio.mxpioboot.jpa.policy;

public interface CriteriaPolicy<T> {
	void apply(CriteriaContext<T> context);
}
