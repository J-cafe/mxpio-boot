package com.mxpioframework.camunda.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mxpioframework.camunda.vo.AllTaskRetVO;
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
	 * @param taskId  任务ID
	 * @param properties 流程变量
	 * @param loginUsername 登录用户
	 * @return 返回消息
	 */
	ResultMessage complete(String taskId, Map<String, Object> properties, String loginUsername);

	/**
	 * 任务委托
	 * @param taskId 任务ID
	 * @param username 被委托人
	 * @param loginUsername 登录用户
	 * @return 返回消息
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

	/**
	 * 历史任务查询
	 * @param username 用户名
	 * @param criteria 查询构造器
	 * @param pageSize 分页大小
	 * @param pageNo 页数
	 * @param finished 完成标识
	 * @return 历史任务列表
	 */
	List<HistoricTaskInstance> pagingHistoricTaskListPageByUser(String username, Criteria criteria, Integer pageSize, Integer pageNo, boolean finished);

	/**
	 * 历史任务计数
	 * @param username 用户名
	 * @param finished 完成标识
	 * @return 任务计数
	 */
	long countHistoricTaskListByUser(String username, boolean finished);

	List<HistoricTaskInstance> pagingHistoricTaskListPageByCandidateUser(String username, Criteria criteria,
			Integer pageSize, Integer pageNo, boolean finished);

	long countHistoricTaskListByCandidateUser(String username, Criteria criteria, boolean finished);

	/**
	 * 获取分页组任务
	 * @param authorities 组
	 * @param criteria 查询构造器
	 * @param pageSize 分页大小
	 * @param pageNo 页码
	 * @return 分页组任务列表
	 */
	List<Task> pagingCandidateGroupTasks(Set<String> authorities, Criteria criteria,
			Integer pageSize, Integer pageNo);

	/**
	 * 组任务计数
	 * @param authorities 组
	 * @param criteria 查询构造器
	 * @return 组任务计数
	 */
	long countCandidateGroupTasks(Set<String> authorities, Criteria criteria);

	List<HistoricTaskInstance> getHistoricTaskByProcessInstanceId(String processInstanceId);

	List<HistoricActivityInstance> getHistoricActivityByProcessInstanceId(String processInstanceId);

	List<HistoricActivityInstance> getHistoricUserActivityByProcessInstanceId(String processInstanceId);

	HistoricProcessInstance getHistoricProcessInstanceById(String processInstanceId);

	HistoricTaskInstance getHistoricTaskById(String startActivityId);

	ProcessDefinition getProcDefByProcessInstanceId(String processInstanceId);

	void handleVariables(String processInstanceId, TaskDetailVO taskDetail);

	ResultMessage rejectToLast(String taskId, Map<String, Object> properties, String loginUsername);

	ResultMessage rejectToFirst(String taskId, Map<String, Object> properties, String loginUsername);

	List<Comment> getCommentsByTaskId(String taskId);

	FormModelDef getTaskFormModelByTaskId(String taskId);

	ResultMessage claim(String taskId, String loginUsername);

	/**
	 * 获取活动任务
	 * @param username 用户名
	 * @return 活动任务列表
	 */
	List<Task> listActiveTasks(String username);

	/**
	 * 获取分页活动任务
	 * @param username 用户名
	 * @param criteria 查询构造器
	 * @param pageSize 分页大小
	 * @param pageNo 页数
	 * @return 分页活动任务
	 */
	List<Task> pagingActiveTasks(String username, Criteria criteria, Integer pageSize, Integer pageNo);

	/**
	 * 活动任务计数
	 * @param username 用户名
	 * @param criteria 查询构造器
	 * @return 活动任务计数
	 */
	long countActiveTasks(String username, Criteria criteria);

	/**
	 * 获取候选任务列表
	 * @param username 用户名
	 * @param criteria 查询构造器
	 * @param pageSize 分页大小
	 * @param pageNo 页数
	 * @return 候选任务列表
	 */
	List<Task> pagingCandidateTasks(String username, Criteria criteria, Integer pageSize, Integer pageNo);

	/**
	 * 候选任务计数
	 * @param username 用户名
	 * @param criteria 查询构造器
	 * @return 候选任务计数
	 */
	long countCandidateTasks(String username, Criteria criteria);

	/**
	 * 获取流程标题
	 * @param id 流程实例ID
	 * @return 流程标题
	 */
	String getTitleByInstanceId(String id);

	/**
	 * 根据流程实例ID获取活动节点
	 * @param processInstanceId 流程实例ID
	 * @return 活动节点清单
	 */
    List<Task> getActiveTaskByProcessInstanceId(String processInstanceId);

	/**
	 * 获取节点表单Key
	 * @param processDefinitionId 流程定义ID
	 * @param taskDefinitionKey 节点定义ID
	 * @return 节点表单Key
	 */
	String getTaskFormKey(String processDefinitionId, String taskDefinitionKey);


	/**
	 * 获取所有任务，包括组任务，候选任务，活动任务
	 * @param username 用户名
	 * @param authorities 组
	 * @param criteria 查询构造器
	 * @return 任务列表
	 */
	AllTaskRetVO getAllTasks(String username, Set<String> authorities, Criteria criteria);

}
