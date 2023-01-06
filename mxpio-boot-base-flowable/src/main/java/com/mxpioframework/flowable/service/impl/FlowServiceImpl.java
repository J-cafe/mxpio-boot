package com.mxpioframework.flowable.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.flowable.entity.Flow;
import com.mxpioframework.flowable.service.FlowService;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;

@Service("mxpio.flowable.flowService")
public class FlowServiceImpl implements FlowService {

	@Override
	@Transactional(readOnly = true)
	public Page<Flow> page(Criteria c, Pageable pageAble) {
		return JpaUtil.linq(Flow.class).where(c).paging(pageAble);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Flow> list(Criteria c) {
		return JpaUtil.linq(Flow.class).where(c).list();
	}

	@Override
	@Transactional(readOnly = false)
	public void save(Flow flow) {
		JpaUtil.save(flow);
	}

	@Override
	@Transactional(readOnly = false)
	public void update(Flow flow) {
		JpaUtil.update(flow);
	}

	@Override
	@Transactional(readOnly = false)
	public int remove(String key) {
		return JpaUtil.lind(Flow.class).idEqual(key).delete();
	}

}
