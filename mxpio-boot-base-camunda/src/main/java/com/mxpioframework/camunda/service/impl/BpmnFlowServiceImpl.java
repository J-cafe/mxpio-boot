package com.mxpioframework.camunda.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import com.googlecode.aviator.AviatorEvaluator;
import com.mxpioframework.camunda.entity.BpmnTask;
import com.mxpioframework.camunda.vo.*;
import com.mxpioframework.jpa.query.Order;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.*;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.pvm.PvmTransition;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceModificationBuilder;
import org.camunda.bpm.engine.task.Comment;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springdoc.core.converters.AdditionalModelsConverter;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.camunda.CamundaConstant;
import com.mxpioframework.camunda.dto.ResultMessage;
import com.mxpioframework.camunda.entity.BpmnFlow;
import com.mxpioframework.camunda.entity.FormModelDef;
import com.mxpioframework.camunda.enums.BpmnEnums;
import com.mxpioframework.camunda.service.BpmnFlowService;
import com.mxpioframework.camunda.service.FormModelService;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.Operator;
import com.mxpioframework.jpa.query.SimpleCriterion;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("mxpio.camunda.bpmnFlowService")
public class BpmnFlowServiceImpl implements BpmnFlowService {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private FormService formService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private FormModelService formModelService;

	@Autowired
	private HistoryService historyService;
	private AdditionalModelsConverter additionalModelsConverter;

	/**
	 * 自引用
	 */
	@Autowired
	private BpmnFlowService bpmnFlowService;

	@Override
	@Transactional(readOnly = true)
	public List<BpmnFlow> list(Criteria criteria) {
		return JpaUtil.linq(BpmnFlow.class).where(criteria).list();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<BpmnFlow> listPage(Pageable page, Criteria criteria) {
		return JpaUtil.linq(BpmnFlow.class).where(criteria).paging(page);
	}

	@Override
	@Transactional
	public void save(BpmnFlow bpmnFlow) {
		JpaUtil.save(bpmnFlow);
	}

	@Override
	@Transactional
	public void delete(String key) {
		JpaUtil.lind(BpmnFlow.class).idEqual(key).delete();
	}

	@Override
	@Transactional
	public void update(BpmnFlow bpmnFlow) {
		JpaUtil.update(bpmnFlow);
	}

	@Override
	@Transactional(readOnly = true)
	public BpmnFlow findByID(String code) {
		return JpaUtil.linq(BpmnFlow.class).idEqual(code).findOne();
	}

	@Override
	@Transactional
	public BpmnFlow deploy(String code) {
		BpmnFlow bpmnFlow = this.findByID(code);
		Deployment deployment = repositoryService.createDeployment()
				.addString(bpmnFlow.getCode() + ".bpmn", bpmnFlow.getXml()).deploy();
		ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId())
				.singleResult();
		bpmnFlow.setStatus(BpmnEnums.DeployStatusEnums.DEPLOY.getCode());
		bpmnFlow.setLastDefId(procDef.getId());
		bpmnFlow.setLastDefVersion(procDef.getVersion());
		this.update(bpmnFlow);
		return bpmnFlow;
	}

	@Override
	@Transactional
	public ProcessInstance startWithFormByKey(String key, String loginUsername, String businessKey, Map<String, Object> properties) {

		BpmnFlow flow = JpaUtil.linq(BpmnFlow.class).idEqual(key).findOne();
		String title;
		if (StringUtils.isNotBlank(flow.getTitle())){
			title = AviatorEvaluator.execute(flow.getTitle(),properties).toString();
		}else{
			title = flow.getName() + "---" + loginUsername;
		}

		properties.put(CamundaConstant.BPMN_TITLE, title);
		properties.put(CamundaConstant.BPMN_START_USER, loginUsername);


		ProcessInstance procInst;
		ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key)
				.latestVersion().singleResult();
		identityService.setAuthenticatedUserId(loginUsername);
		if (procDef.hasStartFormKey()) {
			procInst = formService.submitStartForm(procDef.getId(), businessKey, properties);
		} else {
			procInst = runtimeService.startProcessInstanceById(procDef.getId(), businessKey, properties);
		}
		return procInst;
	}

	@Override
	@Transactional(readOnly = true)
	public String getStartFormKeyByProcessDefinitionKey(String key) {
		String formKey = "";
		ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key)
				.latestVersion().singleResult();
		if (procDef.hasStartFormKey()) {
			formKey = formService.getStartFormKey(procDef.getId());
		}
		return formKey;
	}

	@Override
	@Transactional(readOnly = true)
	public String getTaskFormKeyByTaskId(String taskId) {
		try {
			HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
			// Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			return formService.getTaskFormKey(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<Task> getTaskListByUser(String loginUsername) {
		return taskService.createTaskQuery().taskAssignee(loginUsername).active().orderByTaskCreateTime().desc().list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Task> pagingTaskListPageByUser(String username, Integer pageSize, Integer pageNo) {
		return taskService.createTaskQuery().taskAssignee(username).active().orderByTaskCreateTime().desc()
				.listPage((pageNo - 1) * pageSize, pageSize);
	}

	@Override
	@Transactional(readOnly = true)
	public long countTaskListByUser(String username) {
		return taskService.createTaskQuery().taskAssignee(username).active().count();
	}

	@Override
	@Transactional(readOnly = true)
	public VariableMap getTaskFormDataByTaskId(String taskId) {
		return formService.getTaskFormVariables(taskId);
	}

	@Override
	@Transactional
	public ResultMessage complete(String taskId, Map<String, Object> properties, String loginUsername) {
		Task task = getTaskById(taskId);
		if(task == null){
			return ResultMessage.error("任务不存在！");
		}
		if (task.getAssignee().equals(loginUsername)) {
			if(properties.get(CamundaConstant.BPMN_COMMENT) != null){
				taskService.createComment(taskId, task.getProcessInstanceId(), properties.get(CamundaConstant.BPMN_COMMENT)+"");
			}
			taskService.complete(taskId, properties);
			HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
			HistoricProcessInstance procInst = query.processInstanceId(task.getProcessInstanceId()).singleResult();
			return ResultMessage.success("审批成功！", new ProcessInstanceVO(procInst));
		}else{
			return ResultMessage.error("无权审批！");
		}
	}

	@Override
	@Transactional
	public ResultMessage delegate(String taskId, String username, String loginUsername) {
		Task task = getTaskById(taskId);
		if(task == null){
			return ResultMessage.error("任务不存在！");
		}
		if (task.getAssignee().equals(loginUsername)) {
			taskService.delegateTask(taskId, username);
			return ResultMessage.success("审批成功！");
		}else{
			return ResultMessage.error("无权审批！");
		}
	}

	@Override
	@Transactional
	public ResultMessage rejectToFirst(String taskId, Map<String, Object> properties, String loginUsername) {
		Task task = getTaskById(taskId);
		String processInstanceId = task.getProcessInstanceId();
		taskService.createComment(task.getId(), processInstanceId, "用户"+loginUsername+"进行了驳回操作，驳回原因:" + properties.get(CamundaConstant.BPMN_COMMENT));
		HistoricProcessInstance inst = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		runtimeService.deleteProcessInstance(processInstanceId, "不同意！");
		return ResultMessage.success("不同意！", inst);
		//获取流程实例
		/*HistoricProcessInstance inst = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		String businessKey = inst.getBusinessKey();
		String processDefinitionId = inst.getProcessDefinitionId();
		String startUser = inst.getStartUserId();
		//获取流程变量
		HistoricVariableInstanceQuery query = historyService.createHistoricVariableInstanceQuery()
				.processInstanceId(processInstanceId);
		List<HistoricVariableInstance> variables = query.list();
		Map<String, Object> startDatas = new HashMap<>();
		for(HistoricVariableInstance variable : variables){
			startDatas.put(variable.getName(),variable.getValue());
		}
		//获取流程定义
		ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();*/

		/*ProcessInstance newProcInst = startWithFormByKey(procDef.getKey(), startUser, businessKey, startDatas);*/

	}

	/*@Override
	@Transactional
	public ResultMessage rejectToLast(String taskId, Map<String, Object> properties, String loginUsername) {
		Task task = getTaskById(taskId);
		String processInstanceId = task.getProcessInstanceId();
		ActivityInstance tree = runtimeService.getActivityInstance(processInstanceId);
		List<HistoricActivityInstance> resultList = getHistoricUserActivityByProcessInstanceId(processInstanceId);
		if(null == resultList || resultList.isEmpty()){
			log.error("第一个用户节点无法驳回！");
            return ResultMessage.error("第一个用户节点无法驳回！");
        }

		if(!task.getAssignee().equals(loginUsername)){
			return ResultMessage.error("非当前审批人！");
		}

		//得到上一个任务节点的ActivityId和待办人
        Map<String,String> lastNode = getLastNode(resultList,task.getTaskDefinitionKey());
        if(null == lastNode){
        	log.error("回退节点异常！");
            return ResultMessage.error("回退节点异常！");
        }
        String toActId = lastNode.get("toActId");
        taskService.createComment(task.getId(), processInstanceId, "驳回原因:" + properties.get(CamundaConstant.BPMN_COMMENT));
        runtimeService.createProcessInstanceModification(processInstanceId)
                .cancelActivityInstance(getInstanceIdForActivity(tree, task.getTaskDefinitionKey()))//关闭相关任务
                .setAnnotation("进行了驳回到上一个任务节点操作")
                .startBeforeActivity(toActId)//启动目标活动节点
                .execute();

		return ResultMessage.success("进行了驳回到上一个任务节点操作");
	}*/
	public ResultMessage rejectToLast(String taskId, Map<String, Object> properties, String loginUsername) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processDefinitionId = task.getProcessDefinitionId();
		String processInstanceId = task.getProcessInstanceId();
		List<HistoricActivityInstanceEntity> histActInstList = getHistoricActivityInstanceEntityList(processInstanceId);

		if(!task.getAssignee().equals(loginUsername)){
			return ResultMessage.error("非当前审批人！");
		}

		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(processDefinitionId);
		ActivityImpl activityImpl = processDefinition.getChildActivity(task.getTaskDefinitionKey());

		List<String> histUserTaskActIdList = gethistoricUserTaskActIdList(processInstanceId);

		//需要回退到的节点
		Set<String> toActSet = new HashSet<>();
		findRejectToUserTask(processDefinition,histActInstList,histUserTaskActIdList,activityImpl,toActSet);
		if(CollectionUtils.isEmpty(toActSet)){
			return ResultMessage.error("第一个用户节点无法驳回！");
		}

		//获取所有未执行task
		List<ExecutionEntity> executionEntityList = getExecutionListByProcessInstanceId(processInstanceId);

		//从回退节点开始向后寻找在act_ru_execution表中需要取消的execution
		Set<ExecutionEntity> cancelSet = new HashSet<>();
		for (String actId : toActSet) {
			ActivityImpl act = processDefinition.getChildActivity(actId);
			findNeedCanceledUserTask(executionEntityList, act, cancelSet);
		}
		//更改工作流
		taskService.createComment(task.getId(), processInstanceId, "用户"+loginUsername+"进行了驳回操作，驳回原因:" + properties.get(CamundaConstant.BPMN_COMMENT));
		ProcessInstanceModificationBuilder builder = runtimeService.createProcessInstanceModification(processInstanceId);
		for(ExecutionEntity e:cancelSet){
			builder.cancelActivityInstance(e.getActivityInstanceId())//关闭相关任务
					.setAnnotation("用户"+loginUsername+"进行了驳回到上一个任务节点操作");
		}
		for(String toActId:toActSet){
			builder.startBeforeActivity(toActId);
		}
		builder.execute();
		return ResultMessage.success("进行了驳回到上一个任务节点操作");
	}

	/**
	 * 从驳回当前节点开始向前找，直到所有前驱都找到一个userTask节点为止
	 * @param historyActivityList 历史活动清单
	 * @param activityImpl 活动实体
	 * @param toActSet 活动ID
	 */
	private void findRejectToUserTask(ProcessDefinitionEntity processDefinition,List<HistoricActivityInstanceEntity> HistoricActivityInstanceEntityList,List<String> historyActivityList,ActivityImpl activityImpl,Set<String> toActSet){
		List<PvmTransition> incomingtTransition = activityImpl.getIncomingTransitions();
		if(CollectionUtils.isEmpty(incomingtTransition)){
			return;
		}
		List<PvmTransition> realTransition = getGatewayPath(incomingtTransition,processDefinition,HistoricActivityInstanceEntityList,activityImpl);

		for(PvmTransition pvmTransition:realTransition){
			ActivityImpl sourceActivityImpl = (ActivityImpl)pvmTransition.getSource();
			String sourceTaskType = String.valueOf(sourceActivityImpl.getProperty("type"));
			if("userTask".equals(sourceTaskType)){
				if(historyActivityList.contains(sourceActivityImpl.getActivityId())){
					toActSet.add(sourceActivityImpl.getActivityId());
				}
			}
			else{
				findRejectToUserTask(processDefinition,HistoricActivityInstanceEntityList,historyActivityList,sourceActivityImpl,toActSet);
			}

		}
	}
	/**
	 * 网关汇聚端，根据历史表寻找真实的网关路径
	 * @param incomingtTransition
	 * @param processDefinition
	 * @param HistoricActivityInstanceEntityList
	 * @param activityImpl
	 * @return 真实网关路径
	 */
	private List<PvmTransition> getGatewayPath(List<PvmTransition> incomingtTransition,ProcessDefinitionEntity processDefinition,List<HistoricActivityInstanceEntity> HistoricActivityInstanceEntityList,ActivityImpl activityImpl){
		List<PvmTransition> realTransition = new ArrayList<>();
		String taskType = String.valueOf(activityImpl.getProperty("type"));
		Set<String> betweenGatewayActivity = new HashSet<>();
		//Gateway汇聚端
		if(taskType.endsWith("Gateway")&&CollectionUtils.size(incomingtTransition)>1){
			if("parallelGateway".equals(taskType)){
				realTransition=incomingtTransition;
			}
			else{
				int gatewayNestedNum = 0;
				String actId = activityImpl.getActivityId();
				int begin=-1,end=-1;
				for(int i=0;i<HistoricActivityInstanceEntityList.size();i++){
					HistoricActivityInstanceEntity entity = HistoricActivityInstanceEntityList.get(i);
					if(entity.getActivityId().equals(actId)&&entity.getActivityType().equals(taskType)){
						begin = i;
						continue;
					}
					//找分散端
					if(entity.getActivityType().equals(taskType)){
						ActivityImpl gatewayFirst = processDefinition.getChildActivity(entity.getActivityId());
						//如果又找到一个汇聚端
						if(CollectionUtils.size(gatewayFirst.getIncomingTransitions())>1){
							gatewayNestedNum++;
						}
						if(CollectionUtils.size(gatewayFirst.getOutgoingTransitions())>1){
							if(gatewayNestedNum>0){
								gatewayNestedNum--;
							}
							else{
								end = i;
								break;
							}
						}
					}
				}
				for(int i=begin;i<end+1;i++){
					betweenGatewayActivity.add(HistoricActivityInstanceEntityList.get(i).getActivityId());
				}
				for(PvmTransition pvmTransition:incomingtTransition){
					ActivityImpl sourceActivityImpl = (ActivityImpl)pvmTransition.getSource();
					if(betweenGatewayActivity.contains(sourceActivityImpl.getActivityId())){
						realTransition.add(pvmTransition);
					}
				}
			}
		}
		else{
			realTransition=incomingtTransition;
		}
		return realTransition;
	}

	/**
	 * 获取所有的HistoricActivityInstance，并转换成HistoricActivityInstanceEntity
	 * @param processInstanceId 流程实例ID
	 * @return 历史节点实例
	 */
	private List<HistoricActivityInstanceEntity> getHistoricActivityInstanceEntityList(String processInstanceId){
		List<HistoricActivityInstance> instances =historyService
				.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId)
				.finished()
				.orderByHistoricActivityInstanceEndTime()
				.desc()
				.orderByHistoricActivityInstanceStartTime()
				.desc()
				.list();
		List<HistoricActivityInstanceEntity> entities = new ArrayList<>();
		for(HistoricActivityInstance instance:instances){
			HistoricActivityInstanceEntity entity = (HistoricActivityInstanceEntity)instance;
			entities.add(entity);
		}
		return entities;
	}

	/**
	 * 获取历史执行UserTask的activityId数据
	 * @param processInstanceId 流程实例ID
	 * @return 历史执行UserTask的activityId数据
	 */
	private List<String> gethistoricUserTaskActIdList(String processInstanceId){
        /*List<HistoricActivityInstance> instances =historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .activityType("userTask")
                .finished()
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .list();
        List<HistoricActivityInstanceEntity> entities = new ArrayList<>();
        for(HistoricActivityInstance instance:instances){
            HistoricActivityInstanceEntity entity = (HistoricActivityInstanceEntity)instance;
            if(entity.getActivityInstanceState()==4){
                entities.add(entity);
            }
        }*/
		List<HistoricActivityInstanceEntity> entityList = getHistoricActivityInstanceEntityList(processInstanceId);
		return entityList.stream().filter(p->StringUtils.equals(p.getActivityType(),"userTask")).filter(p->p.getActivityInstanceState()==4).map(HistoricActivityInstanceEntity::getActivityId).collect(Collectors.toList());
	}

	/**
	 * 获取ACT_RU_EXECTION数据
	 * @param processInstanceId 流程实例ID
	 * @return ACT_RU_EXECTION数据
	 */
	private List<ExecutionEntity> getExecutionListByProcessInstanceId(String processInstanceId){
		List<Execution> executionList = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).list();
		List<ExecutionEntity> execEntityList = new ArrayList<>();
		for(Execution e:executionList){
			execEntityList.add((ExecutionEntity)e);
		}
		return execEntityList;
	}


	/**
	 * 从需要回退的节点向后找,根据ACT_RU_EXECUTION表，找到所有待执行状态的userTask
	 * @param executionEntityList
	 * @param act
	 * @param cancelSet
	 */
	public void findNeedCanceledUserTask(List<ExecutionEntity> executionEntityList,ActivityImpl act, Set<ExecutionEntity> cancelSet){
		List<PvmTransition> outgoingList = act.getOutgoingTransitions();
		if(CollectionUtils.isEmpty(outgoingList)){
			return;
		}
		for(PvmTransition pvmTransition:outgoingList) {
			ActivityImpl destinationActivityImpl = (ActivityImpl) pvmTransition.getDestination();
			for(ExecutionEntity e:executionEntityList){
				if(StringUtils.equals(destinationActivityImpl.getActivityId(),e.getActivityId())){
					cancelSet.add(e);
				}
			}
			findNeedCanceledUserTask(executionEntityList,destinationActivityImpl,cancelSet);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ProcessDefinition getProcDefByProcessDefinitionKey(String key) {
		return repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion()
				.singleResult();
	}

	@Override
	@Transactional(readOnly = true)
	public ProcessDefinition getProcDefByProcessDefinitionId(String id) {
		return repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
	}

	@Override
	@Transactional(readOnly = true)
	public void handleFormInfo(ProcessDefinition procDef, BpmnResource bpmnResource) {
		String formKey = formService.getStartFormKey(procDef.getId());
		bpmnResource.setStartFormModelDef(formModelService.getFormModelDefByKey(formKey));
	}

	@Override
	@Transactional(readOnly = true)
	public void handleBpmnFile(ProcessDefinition procDef, BpmnResource bpmnResource) {
		String source = null;
		try {
			source = IOUtils.toString(repositoryService.getProcessModel(procDef.getId()), StandardCharsets.UTF_8);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
		bpmnResource.setSource(source);
	}

	@Override
	@Transactional(readOnly = true)
	public List<HistoricProcessInstance> getHistoricProcessInstances() {
		return historyService.createHistoricProcessInstanceQuery().active().orderByProcessInstanceStartTime().desc()
				.list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<HistoricProcessInstance> pagingHistoricProcessInstances(int firstResult, int maxResults, String username, Boolean finished) {
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
		if(username != null){
			query.startedBy(username);
		}

		if(finished == null){

		}else if(finished){
			query.finished();
		}else{
			query.unfinished();
		}
		return query.orderByProcessInstanceStartTime().desc()
				.listPage(firstResult, maxResults);
	}

	@Override
	@Transactional(readOnly = true)
	public long countHistoricProcessInstances(String username, Boolean finished) {
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
		if(username != null){
			query.startedBy(username);
		}
		if(finished == null){

		}else if(finished){
			query.finished();
		}else{
			query.unfinished();
		}
		return query.count();
	}

	@Override
	@Transactional(readOnly = true)
	public List<HistoricTaskInstance> getHistoricTaskListByUser(String loginUsername) {
		return historyService.createHistoricTaskInstanceQuery().unfinished().taskAssignee(loginUsername)
				.orderByProcessInstanceId().desc().list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<HistoricTaskInstance> pagingHistoricTaskListPageByUser(String username, Criteria criteria, Integer pageSize,
			Integer pageNo, boolean finished) {
		HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery().taskAssignee(username);
		if(finished){
			query.finished();
		}else{
			query.unfinished();
		}
		criteria2HistoricTaskInstanceQuery(criteria, query);
		return query.orderByHistoricActivityInstanceStartTime().desc().listPage((pageNo - 1) * pageSize, pageSize);
	}

	@Override
	@Transactional(readOnly = true)
	public List<HistoricTaskInstance> pagingHistoricTaskListPageByCandidateUser(String username, Criteria criteria,
			Integer pageSize, Integer pageNo, boolean finished) {
		HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery().taskHadCandidateUser(username).taskUnassigned();
		if(finished){
			query.finished();
		}else{
			query.unfinished();
		}
		criteria2HistoricTaskInstanceQuery(criteria, query);
		return query.orderByHistoricActivityInstanceStartTime().desc().listPage((pageNo - 1) * pageSize, pageSize);
	}

	@Override
	@Transactional(readOnly = true)
	public long countHistoricTaskListByCandidateUser(String username, Criteria criteria, boolean finished) {
		HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery().taskHadCandidateUser(username).taskUnassigned();
		if(finished){
			query.finished();
		}else{
			query.unfinished();
		}
		criteria2HistoricTaskInstanceQuery(criteria, query);
		return query.count();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Task> pagingCandidateGroupTasks(Set<String> authorities,
			Criteria criteria, Integer pageSize, Integer pageNo) {
        List<String> authoritiesList = new ArrayList<>(authorities);
		TaskQuery taskQuery = taskService.createTaskQuery().active().taskCandidateGroupIn(authoritiesList).taskUnassigned();
		criteria2TaskQuery(criteria, taskQuery);
		return taskQuery.orderByTaskCreateTime().desc().listPage((pageNo - 1) * pageSize, pageSize);
	}

	@Override
	@Transactional(readOnly = true)
	public long countCandidateGroupTasks(Set<String> authorities, Criteria criteria) {
        List<String> authoritiesList = new ArrayList<>(authorities);
		TaskQuery taskQuery = taskService.createTaskQuery().active().taskCandidateGroupIn(authoritiesList).taskUnassigned();
		criteria2TaskQuery(criteria, taskQuery);
		return taskQuery.count();
	}

	@Override
	public List<Task> listActiveTasks(String username) {
		TaskQuery query = taskService.createTaskQuery().active();
		if(StringUtils.isNotEmpty(username)){
			query.taskAssignee(username); // 当前处理人
		}
		return query.orderByTaskCreateTime().desc().list();
	}

	@Override
	public List<Task> pagingActiveTasks(String username, Criteria criteria, Integer pageSize, Integer pageNo) {
		TaskQuery query = taskService.createTaskQuery().active();
		if(StringUtils.isNotEmpty(username)){
			query.taskAssignee(username); // 当前处理人
		}
		criteria2TaskQuery(criteria, query);
		return query.orderByTaskVariable(CamundaConstant.BPMN_SORT_FLAG, ValueType.NUMBER).desc().orderByTaskCreateTime().desc().listPage((pageNo - 1) * pageSize, pageSize);
	}

	@Override
	public long countActiveTasks(String username, Criteria criteria) {
		TaskQuery query = taskService.createTaskQuery().active();
		if(StringUtils.isNotEmpty(username)){
			query.taskAssignee(username); // 当前处理人
		}
		criteria2TaskQuery(criteria, query);
		return query.count();
	}

	@Override
	public List<Task> pagingCandidateTasks(String username, Criteria criteria, Integer pageSize, Integer pageNo) {
		TaskQuery query = taskService.createTaskQuery().taskCandidateUser(username).active().taskUnassigned();

		criteria2TaskQuery(criteria, query);
		return query.orderByTaskCreateTime().desc().listPage((pageNo - 1) * pageSize, pageSize);
	}

	@Override
	public long countCandidateTasks(String username, Criteria criteria) {
		TaskQuery query = taskService.createTaskQuery().taskCandidateUser(username).active().taskUnassigned();
		criteria2TaskQuery(criteria, query);
		return query.count();
	}

	@Override
	@Transactional
	@Cacheable(cacheNames = CamundaConstant.BPMN_TITLE_CACHE_KEY)
	public String getTitleByInstanceId(String id) {
		String title = null;
		try{
			//title = runtimeService.getVariable(id, CamundaConstant.BPMN_TITLE).toString();
			HistoricVariableInstanceQuery hisVarQuery = historyService.createHistoricVariableInstanceQuery().processInstanceId(id).variableName(CamundaConstant.BPMN_TITLE);
			List<HistoricVariableInstance> hisVarList = hisVarQuery.list();
			if(CollectionUtils.isNotEmpty(hisVarList)){
				title = hisVarList.get(0).getValue().toString();
			}

		} catch (Exception ignored){

		}
		return title == null ? "" : title;
	}

	@Override
	public List<Task> getActiveTaskByProcessInstanceId(String processInstanceId) {
		return taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
	}

	@Override
	public String getTaskFormKey(String processDefinitionId, String taskDefinitionKey) {
		return formService.getTaskFormKey(processDefinitionId, taskDefinitionKey);
	}

	@Override
	@Transactional(readOnly = true)
	public long countHistoricTaskListByUser(String username, Criteria criteria, boolean finished) {
		/*HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery();
		if(finished){
			query.finished();
		}else{
			query.unfinished();
		}
		return query.taskAssignee(username).count();*/
		HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery().taskAssignee(username);
		if(finished){
			query.finished();
		}else{
			query.unfinished();
		}
		criteria2HistoricTaskInstanceQuery(criteria, query);
		return query.count();
	}

	@Override
	@Transactional(readOnly = true)
	public List<HistoricTaskInstance> getHistoricTaskByProcessInstanceId(String processInstanceId) {
        return historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId).finished().orderByHistoricTaskInstanceEndTime().asc().list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<HistoricActivityInstance> getHistoricActivityByProcessInstanceId(String processInstanceId) {
        return historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<HistoricActivityInstance> getHistoricUserActivityByProcessInstanceId(String processInstanceId) {
		return historyService
				.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId)
				.activityType("userTask")
				.finished()
				.orderByHistoricActivityInstanceEndTime()
				.asc()
				.list();
	}

	@Override
	@Transactional(readOnly = true)
	public HistoricProcessInstance getHistoricProcessInstanceById(String processInstanceId) {
		return historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
	}

	@Override
	@Transactional(readOnly = true)
	public HistoricTaskInstance getHistoricTaskById(String startActivityId) {
		return historyService.createHistoricTaskInstanceQuery().taskId(startActivityId).singleResult();
	}

	@Override
	@Transactional(readOnly = true)
	public ProcessDefinition getProcDefByProcessInstanceId(String processInstanceId) {
		HistoricProcessInstance historicProcessInstance = getHistoricProcessInstanceById(processInstanceId);
		return getProcDefByProcessDefinitionId(historicProcessInstance.getProcessDefinitionId());
	}

	@Override
	@Transactional(readOnly = true)
	public void handleVariables(String processInstanceId, TaskDetailVO taskDetail) {
		HistoricVariableInstanceQuery query = historyService.createHistoricVariableInstanceQuery()
				.processInstanceId(processInstanceId);
		List<HistoricVariableInstance> variables = query.list();
		Map<String, Object> startDatas = new HashMap<>();
		for(HistoricVariableInstance variable : variables){
			startDatas.put(variable.getName(),variable.getValue());
		}
		taskDetail.setStartDatas(startDatas);
	}

	/**
     * 获取上一节点信息
     * 分两种情况：
     * 1、当前节点不在历史节点里
     * 2、当前节点在历史节点里
     * 比如，resultList={1,2,3}
     *     (1)当前节点是4，表示3是完成节点，4驳回需要回退到3
     *     (2)当前节点是2，表示3是驳回节点，3驳回到当前2节点，2驳回需要回退到1
     * 其他驳回过的情况也都包含在情况2中。
     *
     * @param resultList 历史节点列表
     * @param currentActivityId 当前待办节点ActivityId
     * @return 返回值：上一节点的ActivityId和待办人（toActId, assignee）
     */
    private static Map<String,String> getLastNode(List<HistoricActivityInstance> resultList, String currentActivityId){
        Map<String,String> backNode = new HashMap<>();
        //新建一个有序不重复集合
        LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<String,String>();
        for(HistoricActivityInstance hai : resultList){
            linkedHashMap.put(hai.getActivityId(),hai.getAssignee());
        }
        //分两种情况：当前节点在不在历史节点里面，当前节点在历史节点里
        //情况1、当前节点不在历史节点里
        int originSize = resultList.size();
        //判断历史节点中是否有重复节点
        boolean flag = false;
        for(Map.Entry<String,String> entry: linkedHashMap.entrySet()){
            if(currentActivityId.equals(entry.getKey())){
                flag = true;
                break;
            }
        }
        if(!flag) {
            //当前节点不在历史节点里：最后一个节点是完成节点
            HistoricActivityInstance historicActivityInstance = resultList.get(originSize - 1);
            backNode.put("toActId", historicActivityInstance.getActivityId());
            backNode.put("assignee", historicActivityInstance.getAssignee());
            return backNode;
        }
        //情况2、当前节点在历史节点里（已回退过的）
        return currentNodeInHis(linkedHashMap, currentActivityId);
    }

    private static Map<String,String> currentNodeInHis(LinkedHashMap<String,String> linkedHashMap,String currentActivityId){
        //情况2、当前节点在历史节点里（已回退过的）
        Map<String,String> backNode = new HashMap<>();
        ListIterator<Map.Entry<String,String>> li = new ArrayList<>(linkedHashMap.entrySet()).listIterator();
        //System.out.println("已回退过的");
        while (li.hasNext()){
            Map.Entry<String,String> entry = li.next();
            if(currentActivityId.equals(entry.getKey())){
                li.previous();
                Map.Entry<String,String> previousEntry = li.previous();
                backNode.put("toActId",previousEntry.getKey());
                backNode.put("assignee",previousEntry.getValue());
                return backNode;
            }
        }
        return null;
    }

    private String getInstanceIdForActivity(ActivityInstance activityInstance, String activityId) {
        ActivityInstance instance = getChildInstanceForActivity(activityInstance, activityId);
        if (instance != null) {
            return instance.getId();
        }
        return null;
    }

    private ActivityInstance getChildInstanceForActivity(ActivityInstance activityInstance, String activityId) {
        if (activityId.equals(activityInstance.getActivityId())) {
            return activityInstance;
        }
        for (ActivityInstance childInstance : activityInstance.getChildActivityInstances()) {
            ActivityInstance instance = getChildInstanceForActivity(childInstance, activityId);
            if (instance != null) {
                return instance;
            }
        }
        return null;
    }

	@Override
	@Transactional(readOnly = true)
	public List<Comment> getCommentsByTaskId(String taskId) {
		return taskService.getTaskComments(taskId);
	}

	@Override
	@Transactional(readOnly = true)
	public FormModelDef getTaskFormModelByTaskId(String taskId) {
		String formKey = getTaskFormKeyByTaskId(taskId);
		if(formKey == null){
			return null;
		}
		return JpaUtil.linq(FormModelDef.class).idEqual(formKey).findOne();
	}

	@Override
	@Transactional
	public ResultMessage claim(String taskId, String loginUsername) {
		try{
			taskService.claim(taskId, loginUsername);
		}catch (Exception e) {
			return ResultMessage.error("领取失败！");
		}
		return ResultMessage.success("领取成功！");
	}

	/**
	 * 获取所有任务，包括组任务，候选任务，活动任务
	 * @param username 用户名
	 * @param authorities 组
	 * @param criteria 查询构造器
	 * @return 任务列表
	 */
	@Override
	@Transactional(readOnly = true)
	public AllTaskRetVO getAllTasks(String username, Set<String> authorities, Criteria criteria,Pageable pageAble){

		criteria = criteria.or().addCriterion("assignee",Operator.EQ,username)
				.addCriterion("candidateUser",Operator.EQ,username)
				.addCriterion("candidateGroup",Operator.IN,authorities)
				.end();

		Page<BpmnTask> bpmnTaskList = JpaUtil.linq(BpmnTask.class).where(criteria).paging(pageAble);

		AllTaskRetVO retVO = new AllTaskRetVO();

		List<TaskVO> allTasks = new ArrayList<>();
		for(BpmnTask bpmnTask:bpmnTaskList.getContent()){
			HistoricProcessInstance historicProcessInstance = this.getHistoricProcessInstanceById(bpmnTask.getProcessInstanceId());
			TaskVO taskVO = new TaskVO(bpmnTask);
			if(StringUtils.isNotBlank(bpmnTask.getAssignee())){
				taskVO.setTaskType("active");
			}
			else if(StringUtils.isNotBlank(bpmnTask.getCandidateUser())){
				taskVO.setTaskType("candidateUser");
			}
			else if(StringUtils.isNotBlank(bpmnTask.getCandidateGroup())){
				taskVO.setTaskType("candidateGroup");
			}
			else{
				continue;
			}
			String formKey = this.getTaskFormKey(bpmnTask.getProcessDefinitionId(), bpmnTask.getTaskDefinitionKey());
			//自引用，防止getTitleByInstanceId的@Cacheable方法失效
			taskVO.setTitle(bpmnFlowService.getTitleByInstanceId(historicProcessInstance.getId()));
			taskVO.setHasForm(StringUtils.isNotEmpty(formKey));
			allTasks.add(taskVO);
		}

		retVO.setAllTasks(allTasks);
		retVO.setCount(bpmnTaskList.getTotalElements());
		return retVO;

		/*//活动任务
		TaskQuery activeTaskQuery = taskService.createTaskQuery().active().taskAssignee(username);
		criteria2TaskQuery(criteria, activeTaskQuery);
		List<Task> activeTaskList = activeTaskQuery.list();
		//候选任务
		TaskQuery candidateUserTaskQuery = taskService.createTaskQuery().taskCandidateUser(username).active().taskUnassigned();
		criteria2TaskQuery(criteria, candidateUserTaskQuery);
		List<Task> candidateUserTaskList = candidateUserTaskQuery.list();
		//组任务
		TaskQuery candidateGroupTaskQuery = taskService.createTaskQuery().active().taskCandidateGroupIn(new ArrayList<>(authorities)).taskUnassigned();
		criteria2TaskQuery(criteria, candidateGroupTaskQuery);
		List<Task> candidateGroupTaskList = candidateGroupTaskQuery.list();

		List<TaskVO> allTasks = new ArrayList<>();
		for(Task t:activeTaskList){
			HistoricProcessInstance historicProcessInstance = this.getHistoricProcessInstanceById(t.getProcessInstanceId());
			TaskVO taskVO = new TaskVO(t, historicProcessInstance);
			taskVO.setTaskType("active");
			allTasks.add(taskVO);
		}
		for(Task t:candidateUserTaskList){
			HistoricProcessInstance historicProcessInstance = this.getHistoricProcessInstanceById(t.getProcessInstanceId());
			TaskVO taskVO = new TaskVO(t, historicProcessInstance);
			taskVO.setTaskType("candidateUser");
			allTasks.add(taskVO);
		}
		for(Task t:candidateGroupTaskList){
			HistoricProcessInstance historicProcessInstance = this.getHistoricProcessInstanceById(t.getProcessInstanceId());
			TaskVO taskVO = new TaskVO(t, historicProcessInstance);
			taskVO.setTaskType("candidateGroup");
			allTasks.add(taskVO);
		}

		Comparator<TaskVO> comparator = criteria2Comparator(criteria);
		if(comparator!=null){
			allTasks=allTasks.stream().sorted(comparator).collect(Collectors.toList());
		}
		AllTaskRetVO retVO = new AllTaskRetVO();
		retVO.setAllTasks(allTasks);
		retVO.setCount((long) allTasks.size());
		return retVO;*/
	}

	@Override
	public ResultMessage urgent(String processInstanceId) {
		try{
			runtimeService.setVariable(processInstanceId, CamundaConstant.BPMN_SORT_FLAG, "1");
		}catch (Exception e) {
			return ResultMessage.error("加急失败！");
		}
		return ResultMessage.success("加急成功！");
	}

	private Task getTaskById(String taskId){
		return taskService.createTaskQuery().taskId(taskId).singleResult();
	}

	/**
	 * 通过Criteria构建TaskQuery
	 * @param criteria 查询构造器
	 * @param query Task查询器
	 */
	private void criteria2TaskQuery(Criteria criteria, TaskQuery query){
		if(criteria != null) {
			for (Object criterion : criteria.getCriterions()) {
				if (criterion instanceof SimpleCriterion) {
					switch (((SimpleCriterion) criterion).getFieldName()) {
						case "name":
							if (Operator.EQ == ((SimpleCriterion) criterion).getOperator()) {
								query.taskName(((SimpleCriterion) criterion).getValue() + "");
							} else {
								query.taskNameLike("%" + ((SimpleCriterion) criterion).getValue() + "%");
							}
							break;
						case "processDefinitionName":
							if (Operator.EQ == ((SimpleCriterion) criterion).getOperator()) {
								query.processDefinitionName(((SimpleCriterion) criterion).getValue() + "");
							}
							else if(Operator.LIKE == ((SimpleCriterion) criterion).getOperator()) {
								query.processDefinitionNameLike("%" + ((SimpleCriterion) criterion).getValue() + "%");
							}
							break;
						case "processDefinitionKey":
							if (Operator.EQ == ((SimpleCriterion) criterion).getOperator()) {
								String values = ((SimpleCriterion) criterion).getValue() + "";
								String [] valueArray = values.split(",");
								query.processDefinitionKeyIn(valueArray);
							}
							break;
						case "taskId":
							if (Operator.EQ == ((SimpleCriterion) criterion).getOperator()) {
								query.taskId(((SimpleCriterion) criterion).getValue()+ "");
							}
							break;
						default:
							if (Operator.EQ == ((SimpleCriterion) criterion).getOperator()) {
								query.processVariableValueEquals(((SimpleCriterion) criterion).getFieldName(), ((SimpleCriterion) criterion).getValue());
							} else {
								query.processVariableValueLike(((SimpleCriterion) criterion).getFieldName(), "%" + ((SimpleCriterion) criterion).getValue() + "%");
							}
							break;
					}
				}
			}
		}
	}

	private Comparator<TaskVO>  criteria2Comparator(Criteria criteria){
		List<Order> orderList = criteria.getOrders();
		if(CollectionUtils.isEmpty(orderList)){
			return null;
		}
		Comparator<TaskVO> comparator = null;
		for(Order order:orderList){
			String orderField = order.getFieldName();
			boolean desc = order.isDesc();
			switch (orderField){
				case "id":
					if(desc){
						if(comparator==null){
							comparator = Comparator.comparing(TaskVO::getId,Comparator.reverseOrder());
						}
						else{
							comparator = comparator.thenComparing(TaskVO::getId,Comparator.reverseOrder());
						}
					}else{
						if(comparator==null){
							comparator = Comparator.comparing(TaskVO::getId);
						}
						else{
							comparator = comparator.thenComparing(TaskVO::getId);
						}
					}
					break;
				case "createTime":
					if(desc){
						if(comparator==null){
							comparator = Comparator.comparing(TaskVO::getCreateTime,Comparator.reverseOrder());
						}
						else{
							comparator = comparator.thenComparing(TaskVO::getCreateTime,Comparator.reverseOrder());
						}
					}
					else{
						if(comparator==null){
							comparator = Comparator.comparing(TaskVO::getCreateTime);
						}
						else{
							comparator = comparator.thenComparing(TaskVO::getCreateTime);
						}
					}
					break;
				default:
					break;
			}
		}
		return comparator;
	}



	private void criteria2HistoricTaskInstanceQuery(Criteria criteria, HistoricTaskInstanceQuery query){
		if(criteria != null){
			for(Object criterion : criteria.getCriterions()){
				if(criterion instanceof SimpleCriterion){
					switch(((SimpleCriterion) criterion).getFieldName()){
						case "name":
							if(Operator.EQ == ((SimpleCriterion) criterion).getOperator()){
								query.taskName(((SimpleCriterion) criterion).getValue() + "");
							}else{
								query.taskNameLike("%" + ((SimpleCriterion) criterion).getValue() + "%");
							}
							break;
						case "processDefinitionName":
							if(Operator.EQ == ((SimpleCriterion) criterion).getOperator()){
								query.processDefinitionName(((SimpleCriterion) criterion).getValue() + "");
							}
							break;
						case "procStartUserId":
							if(Operator.EQ == ((SimpleCriterion) criterion).getOperator()){
								query.processVariableValueEquals("createBy", ((SimpleCriterion) criterion).getValue());
							}else{
								query.processVariableValueLike("createBy", "%" + ((SimpleCriterion) criterion).getValue() + "%");
							}
							break;
						default:
							if(Operator.EQ == ((SimpleCriterion) criterion).getOperator()){
								query.processVariableValueEquals(((SimpleCriterion) criterion).getFieldName(), ((SimpleCriterion) criterion).getValue());
							}else{
								query.processVariableValueLike(((SimpleCriterion) criterion).getFieldName(), "%" + ((SimpleCriterion) criterion).getValue() + "%");
							}

							break;
					}
				}
			}
		}
	}
}
