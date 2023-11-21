package com.mxpioframework.camunda.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Comment;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.camunda.dto.ResultMessage;
import com.mxpioframework.camunda.entity.BpmnFlow;
import com.mxpioframework.camunda.entity.FormModelDef;
import com.mxpioframework.camunda.vo.BpmnResource;
import com.mxpioframework.camunda.vo.TaskDetailVO;
import com.mxpioframework.jpa.query.Criteria;

public interface BpmnFlowService {

	List<BpmnFlow> list(Criteria criteria);

	Page<BpmnFlow> listPage(Pageable page, Criteria criteria);

	void save(BpmnFlow bpmnFlow);

	void delete(String key);

	void update(BpmnFlow bpmnFlow);

	BpmnFlow findByID(String code);

	BpmnFlow deploy(String code);

	ProcessInstance startWithFormByKey(String key, String loginUsername, String businessKey, Map<String, Object> properties);

	String getStartFormKeyByProcessDefinitionKey(String key);

	String getTaskFormKeyByTaskId(String taskId);

	List<Task> getTaskListByUser(String loginUsername);

	List<Task> pagingTaskListPageByUser(String username, Integer pageSize, Integer pageNo);

	long countTaskListByUser(String username);

	VariableMap getTaskFormDataByTaskId(String taskId);

	/**
	 * 任务签核
	 * @param taskId
	 * @param properties
	 * @param loginUsername
	 * @return
	 */
	ResultMessage complete(String taskId, Map<String, Object> properties, String loginUsername);
	
	/**
	 * 任务委托
	 * @param taskId
	 * @param username
	 * @param loginUsername
	 * @return
	 */
	ResultMessage delegate(String taskId, String username, String loginUsername);

	ProcessDefinition getProcDefByProcessDefinitionKey(String key);
	
	ProcessDefinition getProcDefByProcessDefinitionId(String id);

	void handleFormInfo(ProcessDefinition procDef, BpmnResource bpmnResource);

	void handleBpmnFile(ProcessDefinition procDef, BpmnResource bpmnResource);

	List<HistoricProcessInstance> getHistoricProcessInstances();

	List<HistoricProcessInstance> pagingHistoricProcessInstances(int firstResult, int maxResults, String username, Boolean finished);

	long countHistoricProcessInstances(String username, Boolean finished);

	List<HistoricTaskInstance> getHistoricTaskListByUser(String loginUsername);

	List<HistoricTaskInstance> pagingHistoricTaskListPageByUser(String username, Criteria criteria, Integer pageSize, Integer pageNo, boolean finished);
	
	List<HistoricTaskInstance> pagingHistoricTaskListPageByCandidateUser(String username, Criteria criteria,
			Integer pageSize, Integer pageNo, boolean finished);
	
	List<Task> pagingHistoricTaskListPageByCandidateGroup(Set<String> authorities, Criteria criteria,
			Integer pageSize, Integer pageNo, boolean finished);

	long countHistoricTaskListByUser(String username, boolean finished);

	List<HistoricTaskInstance> getHistoricTaskByProcessInstanceId(String processInstanceId);
	
	List<HistoricActivityInstance> getHistoricActivityByProcessInstanceId(String processInstanceId);

	HistoricProcessInstance getHistoricProcessInstanceById(String processInstanceId);

	HistoricTaskInstance getHistoricTaskById(String startActivityId);

	ProcessDefinition getProcDefByProcessInstanceId(String processInstanceId);

	void handleVariables(String processInstanceId, TaskDetailVO taskDetail);

	ResultMessage rejectToLast(String taskId, Map<String, Object> properties, String loginUsername);
	
	ResultMessage rejectToFirst(String taskId, Map<String, Object> properties, String loginUsername);

	List<Comment> getCommentsByTaskId(String taskId);

	FormModelDef getTaskFormModelByTaskId(String taskId);

	ResultMessage claim(String taskId, String loginUsername);

	long countHistoricTaskListByCandidateUser(String username, Criteria criteria, boolean finished);

	long countHistoricTaskListByCandidateGroup(Set<String> authorities, Criteria criteria, boolean finished);

}
