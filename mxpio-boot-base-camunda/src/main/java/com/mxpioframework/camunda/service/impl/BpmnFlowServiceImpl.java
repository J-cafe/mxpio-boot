package com.mxpioframework.camunda.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import com.mxpioframework.camunda.vo.ProcessInstanceVO;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.*;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Comment;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.mxpioframework.camunda.vo.BpmnResource;
import com.mxpioframework.camunda.vo.TaskDetailVO;
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
	@Transactional(readOnly = false)
	public void save(BpmnFlow bpmnFlow) {
		JpaUtil.save(bpmnFlow);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(String key) {
		JpaUtil.lind(BpmnFlow.class).idEqual(key).delete();
	}

	@Override
	@Transactional(readOnly = false)
	public void update(BpmnFlow bpmnFlow) {
		JpaUtil.update(bpmnFlow);
	}

	@Override
	@Transactional(readOnly = true)
	public BpmnFlow findByID(String code) {
		return JpaUtil.linq(BpmnFlow.class).idEqual(code).findOne();
	}

	@Override
	@Transactional(readOnly = false)
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
	@Transactional(readOnly = false)
	public ProcessInstance startWithFormByKey(String key, String loginUsername, String businessKey, Map<String, Object> properties) {
		ProcessInstance procInst = null;
		ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key)
				.latestVersion().singleResult();
		identityService.setAuthenticatedUserId(loginUsername);
		if (procDef.hasStartFormKey()) {
			procInst = formService.submitStartForm(procDef.getId(), businessKey, properties);
		} else {
			procInst = runtimeService.startProcessInstanceById(procDef.getId());
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
	@Transactional(readOnly = false)
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
	@Transactional(readOnly = false)
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
	@Transactional(readOnly = false)
	public ResultMessage rejectToFirst(String taskId, Map<String, Object> properties, String loginUsername) {
		Task task = getTaskById(taskId);
		String processInstanceId = task.getProcessInstanceId();
		ActivityInstance tree = runtimeService.getActivityInstance(processInstanceId);
		List<HistoricActivityInstance> resultList = getHistoricActivityByProcessInstanceId(processInstanceId);
		if(null == resultList || resultList.size()<1){
			log.error("第一个用户节点无法驳回！");
            return ResultMessage.error("第一个用户节点无法驳回！");
        }
		if(!task.getAssignee().equals(loginUsername)){
			return ResultMessage.error("非当前审批人！");
		}
		
		//得到第一个任务节点的id
        HistoricActivityInstance historicActivityInstance = resultList.get(0);
        String toActId = historicActivityInstance.getActivityId();
        if(properties.get(CamundaConstant.BPMN_COMMENT) != null){
        	taskService.createComment(task.getId(), processInstanceId, "驳回原因:" + properties.get(CamundaConstant.BPMN_COMMENT));
		}
        
        runtimeService.createProcessInstanceModification(processInstanceId)
                .cancelActivityInstance(getInstanceIdForActivity(tree, task.getTaskDefinitionKey()))//关闭相关任务
                .setAnnotation("进行了驳回到第一个任务节点操作")
                .startBeforeActivity(toActId)//启动目标活动节点
                .execute();
		
		return ResultMessage.success("进行了驳回到第一个任务节点操作");
	}
	
	@Override
	@Transactional(readOnly = false)
	public ResultMessage rejectToLast(String taskId, Map<String, Object> properties, String loginUsername) {
		Task task = getTaskById(taskId);
		String processInstanceId = task.getProcessInstanceId();
		ActivityInstance tree = runtimeService.getActivityInstance(processInstanceId);
		List<HistoricActivityInstance> resultList = getHistoricActivityByProcessInstanceId(processInstanceId);
		if(null == resultList || resultList.size()<1){
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
			source = IOUtils.toString(repositoryService.getProcessModel(procDef.getId()), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
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
		List<String> authoritiesList = new ArrayList<>();
		authoritiesList.addAll(authorities);
		TaskQuery taskQuery = taskService.createTaskQuery().active().taskCandidateGroupIn(authoritiesList).taskUnassigned();
		criteria2TaskQuery(criteria, taskQuery);
		return taskQuery.orderByTaskCreateTime().desc().listPage((pageNo - 1) * pageSize, pageSize);
	}
	
	@Override
	@Transactional(readOnly = true)
	public long countCandidateGroupTasks(Set<String> authorities, Criteria criteria) {
		List<String> authoritiesList = new ArrayList<>();
		authoritiesList.addAll(authorities);
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
		return query.orderByTaskCreateTime().desc().listPage((pageNo - 1) * pageSize, pageSize);
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
		TaskQuery query = taskService.createTaskQuery().taskCandidateUser(username).active().taskUnassigned();;

		criteria2TaskQuery(criteria, query);
		return query.orderByTaskCreateTime().desc().listPage((pageNo - 1) * pageSize, pageSize);
	}

	@Override
	public long countCandidateTasks(String username, Criteria criteria) {
		TaskQuery query = taskService.createTaskQuery().taskCandidateUser(username).active().taskUnassigned();;
		criteria2TaskQuery(criteria, query);
		return query.count();
	}

	@Override
	@Transactional(readOnly = true)
	public long countHistoricTaskListByUser(String username, boolean finished) {
		HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery();
		if(finished){
			query.finished();
		}else{
			query.unfinished();
		}
		return query.taskAssignee(username).count();
	}

	@Override
	@Transactional(readOnly = true)
	public List<HistoricTaskInstance> getHistoricTaskByProcessInstanceId(String processInstanceId) {
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId).finished().orderByHistoricTaskInstanceEndTime().asc().list();
		return list;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<HistoricActivityInstance> getHistoricActivityByProcessInstanceId(String processInstanceId) {
		List<HistoricActivityInstance> list = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .list();
		return list;
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
	@Transactional(readOnly = false)
	public ResultMessage claim(String taskId, String loginUsername) {
		try{
			taskService.claim(taskId, loginUsername);
		}catch (Exception e) {
			return ResultMessage.error("领取失败！");
		}
		return ResultMessage.success("领取成功！");
	}

	private Task getTaskById(String taskId){
		return taskService.createTaskQuery().taskId(taskId).singleResult();
	}

	/**
	 * 通过Criteria构建TaskQuery
	 * @param criteria
	 * @param query
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
