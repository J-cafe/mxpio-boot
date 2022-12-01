package com.mxpioframework.system.service.impl;

import java.math.BigInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.system.service.CommonService;

@Service
public class CommonServiceImpl implements CommonService {

	@Override
	@Transactional(readOnly = true)
	public Long duplicate(String tableName, String column, String key) {
		
		String sql = "SELECT COUNT(1) FROM " + tableName + " WHERE "+ column + " = '" + key + "'";
		BigInteger count = (BigInteger) JpaUtil.nativeQuery(sql).getSingleResult();
		return count.longValue();
	}

}
