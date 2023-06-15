package com.mxpioframework.camunda.service;

import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.camunda.dto.ProcessDefDto;
import com.mxpioframework.camunda.entity.BpmnFlow;
import com.mxpioframework.jpa.query.Criteria;

public interface BpmnFlowService {

	List<BpmnFlow> list(Criteria criteria);

	Page<BpmnFlow> listPage(Pageable page, Criteria criteria);

	void save(BpmnFlow bpmnFlow);

	void delete(String key);

	void update(BpmnFlow bpmnFlow);

	BpmnFlow findByID(String code);

	BpmnFlow deploy(String code);

	ProcessInstance startWithFormByKey(String key, String loginUsername, Map<String, Object> properties);

	String getStartFormKeyByProcessDefinitionKey(String key);

	String getTaskFormKeyByTaskId(String taskId);

	List<Task> getTaskListByUser(String loginUsername);

	List<Task> getTaskListPageByUser(String username, Integer pageSize, Integer pageNo);

	long countTaskListByUser(String username);

	VariableMap getTaskFormDataByTaskId(String taskId);

	boolean complete(String taskId, Map<String, Object> properties, String loginUsername);

	ProcessDefinition getProcDefByProcessDefinitionKey(String key);

	void handleFormInfo(ProcessDefinition procDef, ProcessDefDto procDefDto);

}
