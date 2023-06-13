package com.mxpioframework.camunda.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.camunda.dto.TaskDto;
import com.mxpioframework.camunda.service.BpmnFlowService;
import com.mxpioframework.common.vo.Result;
import com.mxpioframework.security.util.SecurityUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "TaskController", description = "流程节点管理")
@RestController("mxpio.camunda.TaskController")
@RequestMapping("/camunda/task/")
public class TaskController {
	
	@Autowired
	private BpmnFlowService bpmnFlowService;
	
	@GetMapping("list")
	@Operation(summary = "待办任务列表", description = "待办任务列表", method = "GET")
	public Result<List<TaskDto>> list() {
		List<TaskDto> list = new ArrayList<>();
		List<Task> tasks = bpmnFlowService.getTaskListByUser(SecurityUtils.getLoginUsername());
		for(Task task : tasks){
			list.add(new TaskDto(task));
		}
		return Result.OK(list);
	}

	@GetMapping("page")
	@Operation(summary = "流程列表(分页)", description = "流程列表(分页)", method = "GET")
	public Result<Page<TaskDto>> page(@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) {
		List<TaskDto> list = new ArrayList<>();
		String username = SecurityUtils.getLoginUsername();
		List<Task> tasks = bpmnFlowService.getTaskListPageByUser(username, pageSize, pageNo);
		long total = bpmnFlowService.countTaskListByUser(username);
		for(Task task : tasks){
			list.add(new TaskDto(task));
		}
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Page<TaskDto> page = new PageImpl<TaskDto>(list, pageAble, total);
		return Result.OK(page);
	}
	
	@GetMapping("complete/{taskId}")
	@Operation(summary = "节点签核", description = "节点签核", method = "GET")
	public Result<ProcessInstance> complete(@PathVariable(name = "taskId", required = true) String taskId,
			@RequestBody Map<String, Object> properties){
		boolean b = bpmnFlowService.complete(taskId, properties, SecurityUtils.getLoginUsername());
		if(b){
			return Result.OK("签核完成！", null);
		}else{
			return Result.error("签核失败！");
		}
	}
	
	@GetMapping("form/{taskId}")
	@Operation(summary = "获取节点表单Key", description = "获取节点表单Key", method = "GET")
	public Result<?> form(@PathVariable(name = "taskId", required = true) String taskId) {
		
		String formKey = bpmnFlowService.getTaskFormKeyByTaskId(taskId);
		return Result.OK("查询成功！",formKey);
	}
	
	@GetMapping("form/data/{taskId}")
	@Operation(summary = "获取节点表单数据", description = "获取节点表单数据", method = "GET")
	public Result<Map<String, Object>> formData(@PathVariable(name = "taskId", required = true) String taskId) {
		VariableMap formData = bpmnFlowService.getTaskFormDataByTaskId(taskId);
		return Result.OK("查询成功！",formData);
	}

}
