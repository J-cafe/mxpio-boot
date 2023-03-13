package com.mxpioframework.module.datav.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.module.datav.entity.DatavScreen;
import com.mxpioframework.module.datav.entity.DatavScreenConfig;
import com.mxpioframework.module.datav.service.DatavScreenService;

@Service("mxpio.datav.datavScreenService")
public class DatavScreenServiceImpl implements DatavScreenService {

	@Override
	@Transactional(readOnly = true)
	public List<DatavScreen> list(Criteria criteria) {
		return JpaUtil.linq(DatavScreen.class).collect(DatavScreenConfig.class).where(criteria).list();
	}

	@Override
	@Transactional(readOnly = true)
	public DatavScreen getById(String id) {
		return JpaUtil.linq(DatavScreen.class).collect(DatavScreenConfig.class).idEqual(id).findOne();
	}

	@Override
	@Transactional(readOnly = false)
	public void save(DatavScreen datavScreen) {
		JpaUtil.save(datavScreen);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(DatavScreen datavScreen) {
		JpaUtil.delete(datavScreen);
	}

}
