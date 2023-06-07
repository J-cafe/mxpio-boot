package com.mxpioframework.camunda.controller;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "TaskController", description = "流程节点管理")
@RestController("mxpio.camunda.TaskController")
@RequestMapping("/camunda/task/")
public class TaskController {
	
	@GetMapping("complete/{taskId}")
	@Operation(summary = "节点签核", description = "节点签核", method = "GET")
	public Result<ProcessInstance> complete(@PathVariable(name = "taskId", required = true) String taskId){
		return Result.OK();
	}

}
