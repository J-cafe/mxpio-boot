package com.mxpioframework.camunda.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Comment;
import org.camunda.bpm.engine.variable.VariableMap;
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

import com.mxpioframework.camunda.dto.HistoricTaskDto;
import com.mxpioframework.camunda.dto.TaskDetailDto;
import com.mxpioframework.camunda.dto.TaskDto;
import com.mxpioframework.camunda.dto.TaskFormDto;
import com.mxpioframework.camunda.entity.FormModelDef;
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
		
		List<HistoricTaskInstance> tasks = bpmnFlowService.getHistoricTaskListByUser(SecurityUtils.getLoginUsername());
		for(HistoricTaskInstance task : tasks){
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
		List<HistoricTaskInstance> tasks = bpmnFlowService.pagingHistoricTaskListPageByUser(username, pageSize, pageNo);
		long total = bpmnFlowService.countHistoricTaskListByUser(username);
		for(HistoricTaskInstance task : tasks){
			HistoricProcessInstance historicProcessInstance = bpmnFlowService.getHistoricProcessInstanceById(task.getProcessInstanceId());
			list.add(new TaskDto(task, historicProcessInstance));
		}
		
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Page<TaskDto> page = new PageImpl<TaskDto>(list, pageAble, total);
		return Result.OK(page);
	}
	
	@PostMapping("complete/{taskId}")
	@Operation(summary = "节点签核", description = "节点签核", method = "POST")
	public Result<ProcessInstance> complete(@PathVariable(name = "taskId", required = true) String taskId,
			@RequestBody Map<String, Object> properties){
		boolean b = bpmnFlowService.complete(taskId, properties, SecurityUtils.getLoginUsername());
		if(b){
			return Result.OK("签核完成！", null);
		}else{
			return Result.error("签核失败！");
		}
	}
	
	@PostMapping("reject/first/{taskId}")
	@Operation(summary = "节点驳回开始节点", description = "节点驳回开始节点", method = "POST")
	public Result<ProcessInstance> rejectToFirst(@PathVariable(name = "taskId", required = true) String taskId,
			@RequestBody Map<String, Object> properties){
		boolean b = bpmnFlowService.rejectToFirst(taskId, properties, SecurityUtils.getLoginUsername());
		if(b){
			return Result.OK("签核完成！", null);
		}else{
			return Result.error("签核失败！");
		}
	}
	
	@PostMapping("reject/last/{taskId}")
	@Operation(summary = "节点驳回上一节点", description = "节点上一节点", method = "POST")
	public Result<ProcessInstance> rejectToLast(@PathVariable(name = "taskId", required = true) String taskId,
			@RequestBody Map<String, Object> properties){
		boolean b = bpmnFlowService.rejectToLast(taskId, properties, SecurityUtils.getLoginUsername());
		if(b){
			return Result.OK("签核完成！", null);
		}else{
			return Result.error("签核失败！");
		}
	}
	
	@GetMapping("details/{processInstanceId}/{taskId}")
	@Operation(summary = "获取节点详情", description = "获取节点详情", method = "GET")
	public Result<TaskDetailDto> detail(@PathVariable(name = "processInstanceId", required = true) String processInstanceId,
			@PathVariable(name = "taskId", required = true) String taskId) {
		TaskDetailDto taskDetail = new TaskDetailDto(taskId);
		//获取流程信息
		ProcessDefinition procDef = bpmnFlowService.getProcDefByProcessInstanceId(processInstanceId);
		if(procDef.hasStartFormKey()){
			bpmnFlowService.handleFormInfo(procDef, taskDetail);
		}
		bpmnFlowService.handleBpmnFile(procDef, taskDetail);
		bpmnFlowService.handleVariables(processInstanceId, taskDetail);
		return Result.OK("查询成功！",taskDetail);
	}
	
	@GetMapping("historics/{processInstanceId}")
	@Operation(summary = "获取历史节点信息", description = "获取历史节点信息", method = "GET")
	public Result<List<HistoricTaskDto>> historics(@PathVariable(name = "processInstanceId", required = true) String processInstanceId) {
		List<HistoricTaskDto> list = new ArrayList<>();
		List<HistoricActivityInstance> activitis = bpmnFlowService.getHistoricActivityByProcessInstanceId(processInstanceId);
		for(HistoricActivityInstance activity : activitis){
			if(activity.getTaskId() == null){
				continue;
			}
			HistoricTaskDto historicTaskDto = new HistoricTaskDto(activity);
			List<Comment> comments = bpmnFlowService.getCommentsByTaskId(activity.getTaskId());
			StringBuffer sb = new StringBuffer("");
			for(Comment comment : comments){
				sb.append(comment.getFullMessage() + ";");
			}
			historicTaskDto.setComment(sb.toString());
			list.add(historicTaskDto);
		}
		return Result.OK("查询成功！",list);
	}
	
	@GetMapping("form/{taskId}")
	@Operation(summary = "获取节点表单Key", description = "获取节点表单Key", method = "GET")
	public Result<?> form(@PathVariable(name = "taskId", required = true) String taskId) {
		
		String formKey = bpmnFlowService.getTaskFormKeyByTaskId(taskId);
		return Result.OK("查询成功！",formKey);
	}
	
	@GetMapping("form/data/{taskId}")
	@Operation(summary = "获取节点表单模型及数据", description = "获取节点表单模型及数据", method = "GET")
	public Result<TaskFormDto> formData(@PathVariable(name = "taskId", required = true) String taskId) {
		VariableMap formData = bpmnFlowService.getTaskFormDataByTaskId(taskId);
		FormModelDef formModelDef = bpmnFlowService.getTaskFormModelByTaskId(taskId);
		TaskFormDto dto = new TaskFormDto(formData, formModelDef);
		return Result.OK("查询成功！",dto);
	}

}
