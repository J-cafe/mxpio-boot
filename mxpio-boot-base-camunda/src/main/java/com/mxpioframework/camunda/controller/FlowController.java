package com.mxpioframework.camunda.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.camunda.entity.BpmnFlow;
import com.mxpioframework.camunda.service.BpmnFlowService;
import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.query.Criteria;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "FlowController", description = "流程管理")
@RestController("mxpio.camunda.FlowController")
@RequestMapping("/camunda/flow/")
public class FlowController {
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private BpmnFlowService bpmnFlowService;

	@GetMapping("deploy/{code}")
	@Operation(summary = "部署流程", description = "部署流程", method = "GET")
	public Result<Deployment> deploy(@PathVariable(name = "code", required = true) String code){
		BpmnFlow bpmnFlow = bpmnFlowService.findByID(code);
		Deployment deployment = repositoryService.createDeployment().addString(bpmnFlow.getCode() + ".bpmn", bpmnFlow.getXml()).deploy();
		return Result.OK(deployment);
	}
	
	@GetMapping("list")
	@Operation(summary = "流程列表", description = "获取流程列表", method = "GET")
	public Result<List<BpmnFlow>> list(Criteria criteria) throws UnsupportedEncodingException {
		List<BpmnFlow> bpmnFlows = bpmnFlowService.list(criteria);
		return Result.OK(bpmnFlows);
	}
	
	@GetMapping("page")
	@Operation(summary = "流程列表（分页）", description = "获取流程列表（分页）", method = "GET")
	public Result<Page<BpmnFlow>> page(Criteria criteria, Integer pageSize, Integer pageNo) throws UnsupportedEncodingException {
		Pageable page = PageRequest.of(pageNo-1, pageSize);
		Page<BpmnFlow> bpmnFlows = bpmnFlowService.listPage(page, criteria);
		return Result.OK(bpmnFlows);
	}
	
	@PostMapping("add")
	@Operation(summary = "新增流程", description = "新增流程", method = "POST")
	public Result<BpmnFlow> add(@RequestBody BpmnFlow bpmnFlow) {
		bpmnFlowService.save(bpmnFlow);
		return Result.OK(bpmnFlow);
	}
	
	@PutMapping("edit")
	@Operation(summary = "编辑流程", description = "编辑流程（全量）", method = "PUT")
	public Result<BpmnFlow> edit(@RequestBody BpmnFlow bpmnFlow) {
		bpmnFlowService.update(bpmnFlow);
		return Result.OK(bpmnFlow);
	}
	
	@DeleteMapping("remove/{bpmnFlowCode}")
	@Operation(summary = "删除流程", description = "删除流程", method = "DELETE")
	public Result<BpmnFlow> remove(@PathVariable(name = "bpmnFlowCode", required = true) String bpmnFlowCode) {
		String bpmnFlowCodes[] = bpmnFlowCode.split(",");
		for(String key : bpmnFlowCodes){
			bpmnFlowService.delete(key);
		}
		return Result.OK();
	}
	
}
