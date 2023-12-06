package com.mxpioframework.system.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.system.service.CommonService;

@Service("mxpio.system.commonService")
public class CommonServiceImpl implements CommonService {

	@Override
	@Transactional(readOnly = true)
	public Long duplicate(String tableName, String column, String key, String exclude) {
		
		String sql = "SELECT COUNT(1) FROM " + tableName + " WHERE "+ column + " = '" + key + "'";
		if(exclude != null){
			sql = sql + " AND "+ column + " <> '" + exclude + "'";
		}
		Object singleResult = JpaUtil.nativeQuery(sql).getSingleResult();
		if (singleResult instanceof BigInteger){//mysql类型数据库返回结果类型
			return  ((BigInteger) singleResult).longValue();
		}else if (singleResult instanceof Double){
			return  ((Double) singleResult).longValue();
		}else if (singleResult instanceof BigDecimal){
			return  ((BigDecimal) singleResult).longValue();
		}else {
			return  ((Integer) singleResult).longValue();//sqlserver类型数据库返回结果类型
		}
	}

}
