package com.mxpioframework.camunda.service;

import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.camunda.dto.BpmnResource;
import com.mxpioframework.camunda.dto.TaskDetailDto;
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

	List<Task> pagingTaskListPageByUser(String username, Integer pageSize, Integer pageNo);

	long countTaskListByUser(String username);

	VariableMap getTaskFormDataByTaskId(String taskId);

	boolean complete(String taskId, Map<String, Object> properties, String loginUsername);

	ProcessDefinition getProcDefByProcessDefinitionKey(String key);
	
	ProcessDefinition getProcDefByProcessDefinitionId(String id);

	void handleFormInfo(ProcessDefinition procDef, BpmnResource bpmnResource);

	void handleBpmnFile(ProcessDefinition procDef, BpmnResource bpmnResource);

	List<HistoricProcessInstance> getHistoricProcessInstances();

	List<HistoricProcessInstance> pagingHistoricProcessInstances(int firstResult, int maxResults);

	long countHistoricProcessInstances();

	List<HistoricTaskInstance> getHistoricTaskListByUser(String loginUsername);

	List<HistoricTaskInstance> pagingHistoricTaskListPageByUser(String username, Integer pageSize, Integer pageNo);

	long countHistoricTaskListByUser(String username);

	List<HistoricTaskInstance> getHistoricTaskByProcessInstanceId(String processInstanceId);

	HistoricProcessInstance getHistoricProcessInstanceById(String processInstanceId);

	HistoricTaskInstance getHistoricTaskById(String startActivityId);

	ProcessDefinition getProcDefByProcessInstanceId(String processInstanceId);

	void handleVariables(String processInstanceId, TaskDetailDto taskDetail);

}
