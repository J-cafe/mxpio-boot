package com.mxpioframework.camunda.service.impl;

import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.camunda.dto.ProcessDefDto;
import com.mxpioframework.camunda.entity.BpmnFlow;
import com.mxpioframework.camunda.enums.BpmnEnums;
import com.mxpioframework.camunda.service.BpmnFlowService;
import com.mxpioframework.camunda.service.FormModelService;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;

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
		Deployment deployment = repositoryService.createDeployment().addString(bpmnFlow.getCode() + ".bpmn", bpmnFlow.getXml()).deploy();
		ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
		bpmnFlow.setStatus(BpmnEnums.DeployStatusEnums.DEPLOY.getCode());
		bpmnFlow.setLastDefId(procDef.getId());
		bpmnFlow.setLastDefVersion(procDef.getVersion());
		this.update(bpmnFlow);
		return bpmnFlow;
	}

	@Override
	@Transactional(readOnly = false)
	public ProcessInstance startWithFormByKey(String key, String loginUsername, Map<String, Object> properties) {
		ProcessInstance procInst = null;
		ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult();
		identityService.setAuthenticatedUserId(loginUsername);
		if(procDef.hasStartFormKey()){
			procInst = formService.submitStartForm(procDef.getId(), properties);
		}else{
			procInst = runtimeService.startProcessInstanceById(procDef.getId());
		}
		return procInst;
	}

	@Override
	@Transactional(readOnly = true)
	public String getStartFormKeyByProcessDefinitionKey(String key) {
		String formKey = "";
		ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult();
		if(procDef.hasStartFormKey()){
			formKey = formService.getStartFormKey(procDef.getId());
		}
		return formKey;
	}

	@Override
	@Transactional(readOnly = true)
	public String getTaskFormKeyByTaskId(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		return task.getFormKey();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Task> getTaskListByUser(String loginUsername) {
		return taskService.createTaskQuery().taskAssignee(loginUsername).active().orderByTaskCreateTime().desc().list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Task> pagingTaskListPageByUser(String username, Integer pageSize, Integer pageNo) {
		return taskService.createTaskQuery().taskAssignee(username).active().orderByTaskCreateTime().desc().listPage((pageNo-1) * pageSize, pageNo * pageSize - 1);
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
	public boolean complete(String taskId, Map<String, Object> properties, String loginUsername) {
		boolean result = false;
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if(task.getAssignee().equals(loginUsername)){
			taskService.complete(taskId, properties);
			result = true;
		}
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public ProcessDefinition getProcDefByProcessDefinitionKey(String key) {
		return repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult();
	}
	
	@Override
	@Transactional(readOnly = true)
	public ProcessDefinition getProcDefByProcessDefinitionId(String id) {
		return repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
	}

	@Override
	@Transactional(readOnly = true)
	public void handleFormInfo(ProcessDefinition procDef, ProcessDefDto procDefDto) {
		String formKey = formService.getStartFormKey(procDef.getId());
		procDefDto.setFormModelDef(formModelService.getFormModelDefByKey(formKey));
	}

	@Override
	@Transactional(readOnly = true)
	public void handleBpmnFile(ProcessDefinition procDef, ProcessDefDto procDefDto) {
		Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(procDef.getDeploymentId()).singleResult();
		procDefDto.setSource(deployment.getSource());
	}

	@Override
	@Transactional(readOnly = true)
	public List<HistoricProcessInstance> getHistoricProcessInstances() {
		return historyService.createHistoricProcessInstanceQuery().active().orderByProcessInstanceStartTime().desc().list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<HistoricProcessInstance> pagingHistoricProcessInstances(int firstResult, int maxResults) {
		return historyService.createHistoricProcessInstanceQuery().active().orderByProcessInstanceStartTime().desc().listPage(firstResult, maxResults);
	}

	@Override
	@Transactional(readOnly = true)
	public long countHistoricProcessInstances() {
		return historyService.createHistoricProcessInstanceQuery().active().count();
	}

	@Override
	@Transactional(readOnly = true)
	public List<HistoricTaskInstance> getHistoricTaskListByUser(String loginUsername) {
		return historyService.createHistoricTaskInstanceQuery().unfinished().taskAssignee(loginUsername).orderByProcessInstanceId().desc().list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<HistoricTaskInstance> pagingHistoricTaskListPageByUser(String username, Integer pageSize,
			Integer pageNo) {
		return historyService.createHistoricTaskInstanceQuery().unfinished().taskAssignee(username).orderByProcessInstanceId().desc().listPage((pageNo-1) * pageSize, pageNo * pageSize - 1);
	}

	@Override
	@Transactional(readOnly = true)
	public long countHistoricTaskListByUser(String username) {
		return historyService.createHistoricTaskInstanceQuery().unfinished().taskAssignee(username).count();
	}

}
