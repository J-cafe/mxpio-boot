package com.mxpioframework.multitenant.command;

@FunctionalInterface
public interface CommandNeedReturn<T> {
	T execute();
}
