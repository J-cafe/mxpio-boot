package com.mxpio.mxpioboot.jpa.policy.impl;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;

import com.mxpio.mxpioboot.jpa.policy.GeneratorPolicy;

/**

 * @author Kevin Yang (mailto:kevin.yang@bstek.com)

 * @since 2017年8月10日

 */
public abstract class AbstractGeneratorPolicy implements GeneratorPolicy {

	@Override
	public void apply(Object entity, Field field) {
		field.setAccessible(true);
		ReflectionUtils.setField(field, entity, getValue(entity, field));
	}
	
	protected abstract Object getValue(Object entity, Field field);

}
