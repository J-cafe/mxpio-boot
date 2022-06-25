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
import com.mxpioframework.jpa.query.CriteriaUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "FlowController", tags = {"流程管理"})
@RestController
@RequestMapping("/flow/")
public class FlowController {
	
	@Autowired
	private FlowService flowService;
	
	@GetMapping("/page")
	@ApiOperation(value = "流程列表(分页)", notes = "流程列表(分页)", httpMethod = "GET")
	public Result<Page<Flow>> page(@RequestParam("criteria") String criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		Page<Flow> page = flowService.page(c, pageAble);
		return Result.OK(page);
	}
	
	@GetMapping("/list")
	@ApiOperation(value = "流程列表", notes = "流程列表", httpMethod = "GET")
	public Result<List<Flow>> list(@RequestParam("criteria") String criteria) throws Exception {
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		List<Flow> flows = flowService.list(c);
		return Result.OK(flows);
	}
	
	@PostMapping("/add")
	@ApiOperation(value = "添加流程", notes = "添加流程信息", httpMethod = "POST")
	public Result<Flow> add(@RequestBody Flow flow) throws Exception {
		flowService.save(flow);
		return Result.OK("添加成功",flow);
	}
	
	@PutMapping("/edit")
	@ApiOperation(value = "更新流程", notes = "更新流程信息", httpMethod = "PUT")
	public Result<Flow> edit(@RequestBody Flow flow) throws Exception {
		flowService.update(flow);
		return Result.OK("编辑成功",flow);
	}
	
	@DeleteMapping("/remove/{flowCodes}")
	@ApiOperation(value = "删除流程", notes = "根据code删除流程信息", httpMethod = "DELETE")
	public Result<Flow> remove(@PathVariable(name = "flowCodes", required = true) String flowCodes) throws Exception {
		String[] flowCode = flowCodes.split(",");
        for(String key : flowCode){
        	flowService.remove(key);
        }
        return Result.OK();
	}

}
