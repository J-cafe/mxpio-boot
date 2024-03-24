package com.mxpioframework.camunda.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.camunda.service.BpmnFlowService;
import com.mxpioframework.camunda.vo.ProcessDefVO;
import com.mxpioframework.camunda.vo.ProcessInstanceVO;
import com.mxpioframework.camunda.vo.TaskDetailVO;
import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.util.SecurityUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "ProcessController", description = "流程实例管理")
@RestController("mxpio.camunda.ProcessController")
@RequestMapping("/camunda/process/")
public class ProcessController {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private BpmnFlowService bpmnFlowService;

	@GetMapping("list")
	@Operation(summary = "流程列表", description = "流程列表", method = "GET")
	public Result<List<ProcessInstanceVO>> list() {
		
		List<ProcessInstanceVO> list = new ArrayList<>();
		List<HistoricProcessInstance> procInsts = bpmnFlowService.getHistoricProcessInstances();
		for(HistoricProcessInstance procInst : procInsts){
			list.add(new ProcessInstanceVO(procInst));
		}
		return Result.OK(list);
	}

	@GetMapping("page")
	@Operation(summary = "流程列表(分页)", description = "流程列表(分页)", method = "GET")
	public Result<Page<ProcessInstanceVO>> page(@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) {
		List<ProcessInstanceVO> list = new ArrayList<>();
		
		List<HistoricProcessInstance> procInsts = bpmnFlowService.pagingHistoricProcessInstances((pageNo-1) * pageSize, pageSize, null, false);
		long total = bpmnFlowService.countHistoricProcessInstances(null, false);
		
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		for(HistoricProcessInstance procInst : procInsts){
			list.add(new ProcessInstanceVO(procInst));
		}
		Page<ProcessInstanceVO> page = new PageImpl<ProcessInstanceVO>(list, pageAble, total);
		return Result.OK(page);
	}

	@GetMapping("list/{processInstanceId}")
	@Operation(summary = "根据processInstanceId获取流程", description = "根据processInstanceId获取流程", method = "GET")
	public Result<ProcessInstanceVO> getById(@PathVariable String processInstanceId) {
		HistoricProcessInstance procInst =  bpmnFlowService.getHistoricProcessInstanceById(processInstanceId);
		return Result.OK(new ProcessInstanceVO(procInst));
	}
	
	@GetMapping("page/my")
	@Operation(summary = "我发起的流程列表(分页)", description = "我发起的流程列表(分页)", method = "GET")
	public Result<Page<ProcessInstanceVO>> page(Criteria criteria,
			@RequestParam(value="finished",required = false) Boolean finished,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) {
		List<ProcessInstanceVO> list = new ArrayList<>();
		String username = SecurityUtils.getLoginUsername();
		
		List<HistoricProcessInstance> procInsts = bpmnFlowService.pagingHistoricProcessInstances((pageNo-1) * pageSize, pageSize, username, finished);
		long total = bpmnFlowService.countHistoricProcessInstances(username, finished);
		
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		for(HistoricProcessInstance procInst : procInsts){
			list.add(new ProcessInstanceVO(procInst));
		}
		Page<ProcessInstanceVO> page = new PageImpl<ProcessInstanceVO>(list, pageAble, total);
		return Result.OK(page);
	}
	
	@GetMapping("form/{key}")
	@Operation(summary = "获取流程开始表单Key", description = "获取流程开始表单Key", method = "GET")
	public Result<?> form(@PathVariable(name = "key", required = true) String key) {
		
		String formKey = bpmnFlowService.getStartFormKeyByProcessDefinitionKey(key);
		return Result.OK("查询成功！",formKey);
	}
	
	@GetMapping("def/{key}")
	@Operation(summary = "获取流程定义", description = "根据key获取最新流程定义", method = "GET")
	public Result<ProcessDefVO> def(@PathVariable(name = "key", required = true) String key) {
		
		ProcessDefinition procDef = bpmnFlowService.getProcDefByProcessDefinitionKey(key);
		ProcessDefVO procDefDto = new ProcessDefVO(procDef);
		if(procDef.hasStartFormKey()){
			bpmnFlowService.handleFormInfo(procDef, procDefDto);
		}
		bpmnFlowService.handleBpmnFile(procDef, procDefDto);
		return Result.OK("查询成功！",procDefDto);
	}
	
	@GetMapping("details/{processInstanceId}")
	@Operation(summary = "获取流程详情", description = "获取流程详情", method = "GET")
	public Result<TaskDetailVO> detail(@PathVariable(name = "processInstanceId", required = true) String processInstanceId) {
		TaskDetailVO taskDetail = new TaskDetailVO();
		//获取流程信息
		ProcessDefinition procDef = bpmnFlowService.getProcDefByProcessInstanceId(processInstanceId);
		if(procDef.hasStartFormKey()){
			bpmnFlowService.handleFormInfo(procDef, taskDetail);
		}
		bpmnFlowService.handleBpmnFile(procDef, taskDetail);
		bpmnFlowService.handleVariables(processInstanceId, taskDetail);
		return Result.OK("查询成功！",taskDetail);
	}

	@PostMapping("start/{key}")
	@Operation(summary = "启动流程", description = "启动流程", method = "POST")
	public Result<?> start(@PathVariable(name = "key", required = true) String key,
			@RequestParam(value="businessKey", required = false) String businessKey,
			@RequestBody Map<String, Object> properties) {
		
		ProcessInstance processInstance = bpmnFlowService.startWithFormByKey(key, SecurityUtils.getLoginUsername(), businessKey, properties);
		return Result.OK("启动成功！", processInstance.getId());
	}
	
	@GetMapping("suspend/{instanceId}")
	@Operation(summary = "暂停流程", description = "暂停流程", method = "GET")
	public Result<?> suspend(@PathVariable(name = "instanceId", required = true) String instanceId) {
		
		runtimeService.suspendProcessInstanceById(instanceId);
		return Result.OK();
	}

	@GetMapping("restart/{processDefinitionId}/{instanceId}")
	@Operation(summary = "重启流程", description = "重启流程", method = "GET")
	public Result<?> restart(@PathVariable(name = "processDefinitionId", required = true) String processDefinitionId,
			@PathVariable(name = "instanceId", required = true) String instanceId) {
		runtimeService.activateProcessInstanceById(instanceId);
		return Result.OK();
	}

}
