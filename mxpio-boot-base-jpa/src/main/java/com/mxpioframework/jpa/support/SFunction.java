package com.mxpioframework.jpa.support;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 支持序列化的 Function
 * @param <T>
 * @param <R>
 */
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {
}
