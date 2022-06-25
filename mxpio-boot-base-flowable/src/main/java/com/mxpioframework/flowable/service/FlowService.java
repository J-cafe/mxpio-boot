package com.mxpioframework.flowable.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.flowable.entity.Flow;
import com.mxpioframework.jpa.query.Criteria;

public interface FlowService {

	Page<Flow> page(Criteria c, Pageable pageAble);

	List<Flow> list(Criteria c);

	void save(Flow flow);

	void update(Flow flow);

	int remove(String key);

}
