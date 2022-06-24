package com.mxpioframework.quartz.controller;

import java.io.IOException;
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
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.CriteriaUtils;
import com.mxpioframework.quartz.entity.QuartzJob;
import com.mxpioframework.quartz.service.QuartzService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "QuartzController", tags = { "任务调度接口" })
@RestController
@RequestMapping("/quartz/")
public class QuartzController {

	@Autowired
	private QuartzService quartzService;
	
	@GetMapping("list")
	@ApiOperation(value = "任务列表", notes = "获取任务列表", httpMethod = "GET")
	public Result<List<QuartzJob>> list(String criteria) throws IOException {
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		List<QuartzJob> jobs = quartzService.list(c);
		return Result.OK(jobs);
	}
	
	@GetMapping("page")
	@ApiOperation(value = "任务列表(分页)", notes = "获取任务列表(分页)", httpMethod = "GET")
	public Result<Page<QuartzJob>> page(String criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws IOException {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		Page<QuartzJob> jobs = quartzService.page(pageAble, c);
		return Result.OK(jobs);
	}
	
	@PostMapping("add")
	@ApiOperation(value = "新增任务", notes = "新增任务", httpMethod = "POST")
	public Result<QuartzJob> add(@RequestBody QuartzJob job) {
		quartzService.addJob(job);
		return Result.OK(job);
	}
	
	@PutMapping("edit")
	@ApiOperation(value = "编辑任务", notes = "编辑任务", httpMethod = "PUT")
	public Result<QuartzJob> edit(@RequestBody QuartzJob job) {
		quartzService.editJob(job);
		return Result.OK(job);
	}
	
	@PostMapping("run/{id}")
	@ApiOperation(value = "启动任务", notes = "启动任务", httpMethod = "POST")
	public Result<QuartzJob> run(@PathVariable(value = "id") String id) {
		boolean result = quartzService.resume(id);
		if(result) {
			return Result.OK("启动成功", null);
		}else {
			return Result.error("启动失败");
		}
	}
	
	@PostMapping("pause/{id}")
	@ApiOperation(value = "暂停任务", notes = "暂停任务", httpMethod = "POST")
	public Result<QuartzJob> pause(@PathVariable(value = "id") String id) {
		boolean result = quartzService.pause(id);
		if(result) {
			return Result.OK("暂停成功", null);
		}else {
			return Result.error("暂停失败");
		}
		
	}
	
	@DeleteMapping("delete/{id}")
	@ApiOperation(value = "删除任务", notes = "删除任务", httpMethod = "DELETE")
	public Result<QuartzJob> delete(@PathVariable(value = "id") String id) {
		quartzService.deleteJob(id);
		return Result.OK();
	}
}
