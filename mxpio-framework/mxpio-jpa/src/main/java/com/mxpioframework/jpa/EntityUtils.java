package com.mxpioframework.jpa;

import java.math.BigDecimal;
import java.util.Date;

public class EntityUtils {

	public static boolean isSimpleType(Class<?> type) {
		if(Integer.class.isAssignableFrom(type)
				||String.class.isAssignableFrom(type)
				||Boolean.class.isAssignableFrom(type)
				||Short.class.isAssignableFrom(type)
				||Byte.class.isAssignableFrom(type)
				||Long.class.isAssignableFrom(type)
				||BigDecimal.class.isAssignableFrom(type)
				||Double.class.isAssignableFrom(type)
				||Float.class.isAssignableFrom(type)
				||Date.class.isAssignableFrom(type)){
			return true;
		}
		return false;
	}

}
