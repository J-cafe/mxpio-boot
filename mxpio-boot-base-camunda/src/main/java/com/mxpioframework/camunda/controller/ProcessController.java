package com.mxpioframework.camunda.controller;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
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
	private IdentityService identityService;

	@GetMapping("list")
	@Operation(summary = "流程列表", description = "流程列表", method = "GET")
	public Result<List<ProcessInstance>> list() {
		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
		List<ProcessInstance> list = processInstanceQuery.orderByProcessInstanceId().desc().list();
		return Result.OK(list);
	}

	@GetMapping("page")
	@Operation(summary = "流程列表(分页)", description = "流程列表(分页)", method = "GET")
	public Result<Page<ProcessInstance>> page(@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) {
		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		long total = processInstanceQuery.count();
		List<ProcessInstance> list = processInstanceQuery.orderByProcessInstanceId().desc().listPage((pageNo-1) * pageSize, pageNo * pageSize - 1);
		Page<ProcessInstance> page = new PageImpl<ProcessInstance>(list, pageAble, total);
		return Result.OK(page);
	}

	@GetMapping("start/{key}")
	@Operation(summary = "启动流程", description = "启动流程", method = "GET")
	public Result<?> start(@PathVariable(name = "key", required = true) String key) {
		identityService.setAuthenticatedUserId(SecurityUtils.getLoginUsername());
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key);
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
		runtimeService.restartProcessInstances(processDefinitionId).processInstanceIds(instanceId).execute();
		return Result.OK();
	}

}
