package com.mxpioframework.flowable.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.flowable.entity.Flow;
import com.mxpioframework.flowable.service.FlowService;
import com.mxpioframework.jpa.query.Criteria;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "FlowController", description = "流程管理")
@RestController("mxpio.flowable.flowController")
@RequestMapping("/flow/")
public class FlowController {
	
	@Autowired
	private FlowService flowService;
	
	@GetMapping("page")
	@Operation(summary = "流程列表(分页)", description = "流程列表(分页)", method = "GET")
	public Result<Page<Flow>> page(@RequestParam("criteria") Criteria criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Page<Flow> page = flowService.page(criteria, pageAble);
		return Result.OK(page);
	}
	
	@GetMapping("list")
	@Operation(summary = "流程列表", description = "流程列表", method = "GET")
	public Result<List<Flow>> list(@RequestParam("criteria") Criteria criteria) throws Exception {
		List<Flow> flows = flowService.list(criteria);
		return Result.OK(flows);
	}
	
	@PostMapping("add")
	@Operation(summary = "添加流程", description = "添加流程信息", method = "POST")
	public Result<Flow> add(@RequestBody Flow flow) throws Exception {
		flowService.save(flow);
		return Result.OK("添加成功",flow);
	}
	
	@PutMapping("edit")
	@Operation(summary = "更新流程", description = "更新流程信息", method = "PUT")
	public Result<Flow> edit(@RequestBody Flow flow) throws Exception {
		flowService.update(flow);
		return Result.OK("编辑成功",flow);
	}
	
	@DeleteMapping("remove/{flowCodes}")
	@Operation(summary = "删除流程", description = "根据code删除流程信息", method = "DELETE")
	public Result<Flow> remove(@PathVariable(name = "flowCodes", required = true) String flowCodes) throws Exception {
		String[] flowCode = flowCodes.split(",");
        for(String key : flowCode){
        	flowService.remove(key);
        }
        return Result.OK();
	}

}
