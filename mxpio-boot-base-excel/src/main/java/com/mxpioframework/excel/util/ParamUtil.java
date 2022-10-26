package com.mxpioframework.excel.util;

import java.lang.reflect.Parameter;

import com.mxpioframework.jpa.query.CriteriaUtils;

public class ParamUtil {

	public static Object getParamValue(Parameter methodParam, String[] values){
		Object value = null;
		if("criteria".equals(methodParam.getName())){
			value = CriteriaUtils.json2Criteria(values[0]);
		}else if(Integer.class.equals(methodParam.getType())){
			value = Integer.parseInt(values[0]);
		}else{
			value = values[0];
		}
		return value;
	}
}
