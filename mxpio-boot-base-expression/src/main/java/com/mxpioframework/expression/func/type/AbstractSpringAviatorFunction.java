package com.mxpioframework.expression.func.type;

import com.googlecode.aviator.runtime.function.AbstractFunction;

public abstract class AbstractSpringAviatorFunction extends AbstractFunction {

	@Override
	public abstract String getName();
	
	public abstract boolean disabled();

	public abstract String getDesc();

}
