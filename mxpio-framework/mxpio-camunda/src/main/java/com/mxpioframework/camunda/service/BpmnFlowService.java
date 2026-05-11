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

	/**
	 * 根据指定的查询条件查询并返回BpmnFlow对象列表
	 *
	 * @param criteria 查询条件对象，用于构建查询
	 * @return 返回符合查询条件的BpmnFlow对象列表
	 */
	List<BpmnFlow> list(Criteria criteria);

	/**
	 * 根据指定的分页条件和查询条件查询并返回BpmnFlow对象的分页列表
	 *
	 * @param page 分页条件对象，用于构建分页查询
	 * @param criteria 查询条件对象，用于构建查询
	 * @return 返回符合查询条件的BpmnFlow对象的分页列表
	 */
	Page<BpmnFlow> listPage(Pageable page, Criteria criteria);

	/**
	 * 保存BpmnFlow对象到数据库
	 *
	 * @param bpmnFlow 要保存的BpmnFlow对象
	 */
	void save(BpmnFlow bpmnFlow);

	/**
	 * 根据指定的key删除数据
	 *
	 * @param key 要删除的数据的key
	 */
	void delete(String key);

	/**
	 * 更新BpmnFlow对象到数据库
	 *
	 * @param bpmnFlow 要更新的BpmnFlow对象
	 */
	void update(BpmnFlow bpmnFlow);

	/**
	 * 根据指定的code查询并返回BpmnFlow对象
	 *
	 * @param code 要查询的BpmnFlow对象的code
	 * @return 返回符合查询条件的BpmnFlow对象
	 */
	BpmnFlow findByID(String code);

	/**
	 * 部署一个 BPMN 流程
	 *
	 * @param code 流程的唯一标识符
	 * @return 部署后的 BpmnFlow 对象
	 */
	BpmnFlow deploy(String code);

	/**
	 * 根据流程定义的key、登录用户名、业务键和流程变量启动一个流程实例，并返回启动的流程实例对象
	 *
	 * @param key 流程定义的key
	 * @param loginUsername 登录用户名
	 * @param businessKey 业务键
	 * @param properties 流程变量
	 * @return 返回启动的流程实例对象
	 */
	ProcessInstance startWithFormByKey(String key, String loginUsername, String businessKey, Map<String, Object> properties);

	/**
	 * 根据流程定义的key获取开始表单的key
	 *
	 * @param key 流程定义的key
	 * @return 返回开始表单的key
	 */
	String getStartFormKeyByProcessDefinitionKey(String key);

	/**
	 * 根据任务ID获取任务表单的key
	 *
	 * @param taskId 任务ID
	 * @return 返回任务表单的key
	 */
	String getTaskFormKeyByTaskId(String taskId);

	/**
	 * 根据登录用户名获取任务列表
	 *
	 * @param loginUsername 登录用户名
	 * @return 返回符合查询条件的任务列表
	 */
	@Deprecated
	List<Task> getTaskListByUser(String loginUsername);

	/**
	 * 根据用户名、分页大小和页码查询并返回任务列表
	 *
	 * @param username 用户名
	 * @param pageSize 分页大小
	 * @param pageNo 页码
	 * @return 返回符合查询条件的任务列表
	 */
	@Deprecated
	List<Task> pagingTaskListPageByUser(String username, Integer pageSize, Integer pageNo);

	/**
	 * 根据登录用户名统计任务列表的数量
	 *
	 * @param username 登录用户名
	 * @return 返回符合查询条件的任务列表的数量
	 */
	@Deprecated
	long countTaskListByUser(String username);

	/**
	 * 根据任务ID获取任务表单数据
	 *
	 * @param taskId 任务ID
	 * @return 返回任务表单数据的变量映射
	 */
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

	/**
	 * 根据流程定义的key获取流程定义对象
	 *
	 * @param key 流程定义的key
	 * @return 返回符合查询条件的流程定义对象
	 */
	ProcessDefinition getProcDefByProcessDefinitionKey(String key);

	/**
	 * 根据流程定义的id获取流程定义对象
	 *
	 * @param id 流程定义的id
	 * @return 返回符合查询条件的流程定义对象
	 */
	ProcessDefinition getProcDefByProcessDefinitionId(String id);

	/**
	 * 处理表单信息
	 * @param procDef 流程定义对象
	 * @param bpmnResource BPMN资源对象
	 */
	void handleFormInfo(ProcessDefinition procDef, BpmnResource bpmnResource);

	/**
	 * 处理 BPMN 文件
	 * @param procDef 流程定义对象
	 * @param bpmnResource BPMN 资源对象
	 */
	void handleBpmnFile(ProcessDefinition procDef, BpmnResource bpmnResource);

	/**
	 * 获取历史流程实例列表
	 *
	 * @return 返回历史流程实例列表
	 */
	List<HistoricProcessInstance> getHistoricProcessInstances();

	/**
	 * 分页查询历史流程实例
	 *
	 * @param firstResult 起始索引
	 * @param maxResults 最大返回结果数
	 * @param username 用户名
	 * @param finished 是否完成
	 * @return 返回符合查询条件的历史流程实例列表
	 */
	List<HistoricProcessInstance> pagingHistoricProcessInstances(int firstResult, int maxResults, String username, Boolean finished);

	/**
	 * 统计历史流程实例的数量
	 *
	 * @param username 用户名
	 * @param finished 是否完成
	 * @return 返回历史流程实例的数量
	 */
	long countHistoricProcessInstances(String username, Boolean finished);

	/**
	 * 根据登录用户名获取历史任务实例列表
	 *
	 * @param loginUsername 登录用户名
	 * @return 返回符合查询条件的历史任务实例列表
	 */
	@Deprecated
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
	 * @param criteria 查询构造器
	 * @param finished 完成标识
	 * @return 任务计数
	 */
	long countHistoricTaskListByUser(String username, Criteria criteria, boolean finished);

	/**
	 * 分页查询候选用户的历史任务实例
	 *
	 * @param username 用户名
	 * @param criteria 查询构造器
	 * @param pageSize 分页大小
	 * @param pageNo 页数
	 * @param finished 是否完成
	 * @return 返回符合查询条件的历史任务实例列表
	 */
	@Deprecated
	List<HistoricTaskInstance> pagingHistoricTaskListPageByCandidateUser(String username, Criteria criteria,
			Integer pageSize, Integer pageNo, boolean finished);

	/**
	 * 统计历史任务实例的数量
	 *
	 * @param username 用户名
	 * @param criteria 查询构造器
	 * @param finished 是否完成
	 * @return 返回历史任务实例的数量
	 */
	@Deprecated
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

	/**
	 * 根据流程实例ID获取历史任务实例列表
	 *
	 * @param processInstanceId 流程实例ID
	 * @return 返回符合查询条件的历史任务实例列表
	 */
	@Deprecated
	List<HistoricTaskInstance> getHistoricTaskByProcessInstanceId(String processInstanceId);

	/**
	 * 根据流程实例ID获取历史活动实例列表
	 *
	 * @param processInstanceId 流程实例ID
	 * @return 返回符合查询条件的历史活动实例列表
	 */
	List<HistoricActivityInstance> getHistoricActivityByProcessInstanceId(String processInstanceId);

	/**
	 * 根据流程实例ID获取历史活动实例列表
	 *
	 * @param processInstanceId 流程实例ID
	 * @return 返回符合查询条件的历史活动实例列表
	 */
	@Deprecated
	List<HistoricActivityInstance> getHistoricUserActivityByProcessInstanceId(String processInstanceId);

	/**
	 * 根据流程实例ID获取历史流程实例
	 *
	 * @param processInstanceId 流程实例ID
	 * @return 返回符合查询条件的历史流程实例
	 */
	HistoricProcessInstance getHistoricProcessInstanceById(String processInstanceId);

	/**
	 * 根据开始活动ID获取历史任务实例
	 *
	 * @param startActivityId 开始活动ID
	 * @return 返回符合查询条件的历史任务实例
	 */
	HistoricTaskInstance getHistoricTaskById(String startActivityId);

	/**
	 * 根据流程实例ID获取流程定义
	 *
	 * @param processInstanceId 流程实例ID
	 * @return 返回符合查询条件的流程定义
	 */
	ProcessDefinition getProcDefByProcessInstanceId(String processInstanceId);

	/**
	 * 处理流程实例的变量
	 *
	 * @param processInstanceId 流程实例ID
	 * @param taskDetail 任务详情
	 */
	void handleVariables(String processInstanceId, TaskDetailVO taskDetail);

	/**
	 * 将任务回退到最后一个节点
	 * @param taskId 任务ID
	 * @param properties 流程变量
	 * @param loginUsername 登录用户名
	 * @return 结果信息
	 */
	ResultMessage rejectToLast(String taskId, Map<String, Object> properties, String loginUsername);

	/**
	 * 将任务回退到第一个节点
	 * @param taskId 任务ID
	 * @param properties 流程变量
	 * @param loginUsername 登录用户名
	 * @return 结果信息
	 */
	ResultMessage rejectToFirst(String taskId, Map<String, Object> properties, String loginUsername);

    /**
     * 查询流程某个变量的值
     * @param processInstanceId 流程实例ID
     * @param variableName 流程变量名称
     * @return 流程变量值
     */
    String getVariableByName(String processInstanceId, String variableName);

    /**
	 * 根据任务ID获取任务的评论列表
	 *
	 * @param taskId 任务ID
	 * @return 返回符合查询条件的任务评论列表
	 */
	List<Comment> getCommentsByTaskId(String taskId);

	/**
	 * 根据任务ID获取任务表单模型定义
	 *
	 * @param taskId 任务ID
	 * @return 返回符合查询条件的任务表单模型定义
	 */
	FormModelDef getTaskFormModelByTaskId(String taskId);

	/**
	 * 声明一个任务
	 * @param taskId 任务ID
	 * @param loginUsername 登录用户名
	 * @return 结果信息
	 */
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
	 * @param pageAble 分页
	 * @return 任务列表
	 */
	AllTaskRetVO getAllTasks(String username, Set<String> authorities, Criteria criteria,Pageable pageAble);

	/**
	 * 流程加急
	 * @param processInstanceId 流程实例ID
	 * @return 结果
	 */
	ResultMessage urgent(String processInstanceId);

	List<HistoricProcessInstance> pagingHistoricProcessInstances(Criteria criteria, int i, int pageSize, String username, Boolean finished);

	long countHistoricProcessInstances(Criteria criteria, String username, Boolean finished);
}
