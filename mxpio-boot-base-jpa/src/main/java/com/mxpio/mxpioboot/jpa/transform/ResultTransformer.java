package com.mxpio.mxpioboot.jpa.transform;
/**
 * 查询结果转换器
 */
public interface ResultTransformer {

	public Object transformTuple(Object[] tuple, String[] aliases);
}
