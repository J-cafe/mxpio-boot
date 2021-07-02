package com.mxpio.mxpioboot.jpa.transform.impl;


import net.sf.cglib.beans.BeanMap;

import java.lang.reflect.InvocationTargetException;

import com.mxpio.mxpioboot.jpa.transform.ResultTransformer;

/**
 * 别名转Bean结果转换器
 */
public class AliasToBeanResultTransformer implements ResultTransformer {

	private Class<?> resultClass;
	
	public AliasToBeanResultTransformer(Class<?> resultClass) {
		this.resultClass = resultClass;
	}
	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		Object result;
		
		try {
			result = resultClass.getDeclaredConstructor().newInstance();;
			
			BeanMap beanMap = BeanMap.create(result);

			for ( int i = 0; i < aliases.length; i++ ) {
				String alias = aliases[i];
				if (alias != null) {
					beanMap.put(alias, tuple[i]);
				}
			}
		} catch ( InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e ) {
			throw new RuntimeException( "Could not instantiate resultclass: " + resultClass.getName() );
		}
		return result;
	}

}
