package com.mxpioframework.camunda.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.camunda.entity.BpmnFlow;
import com.mxpioframework.jpa.query.Criteria;

public interface BpmnFlowService {

	List<BpmnFlow> list(Criteria criteria);

	Page<BpmnFlow> listPage(Pageable page, Criteria criteria);

	void save(BpmnFlow bpmnFlow);

	void delete(String key);

	void update(BpmnFlow bpmnFlow);

	BpmnFlow findByID(String code);

}
