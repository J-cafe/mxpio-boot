package com.mxpioframework.quartz.controller;

import java.io.IOException;
import java.util.List;

import com.mxpioframework.jpa.query.Operator;
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
import com.mxpioframework.quartz.entity.QuartzJob;
import com.mxpioframework.quartz.service.QuartzService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "QuartzController", description = "任务调度接口")
@RestController("mxpio.quartz.quartzController")
@RequestMapping("/quartz/")
public class QuartzController {

	@Autowired
	private QuartzService quartzService;
	
	@GetMapping("list")
	@Operation(summary = "任务列表", description = "获取任务列表", method = "GET")
	public Result<List<QuartzJob>> list(Criteria criteria) throws IOException {
		List<QuartzJob> jobs = quartzService.list(criteria);
		return Result.OK(jobs);
	}
	
	@GetMapping("page")
	@Operation(summary = "任务列表(分页)", description = "获取任务列表(分页)", method = "GET")
	public Result<Page<QuartzJob>> page(Criteria criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws IOException {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Page<QuartzJob> jobs = quartzService.page(pageAble, criteria);
		return Result.OK(jobs);
	}
	
	@PostMapping("add")
	@Operation(summary = "新增任务", description = "新增任务", method = "POST")
	public Result<QuartzJob> add(@RequestBody QuartzJob job) {
		quartzService.addJob(job);
		return Result.OK(job);
	}
	
	@PutMapping("edit")
	@Operation(summary = "编辑任务", description = "编辑任务", method = "PUT")
	public Result<QuartzJob> edit(@RequestBody QuartzJob job) {
		quartzService.editJob(job);
		return Result.OK(job);
	}
	
	@PostMapping("run/{id}")
	@Operation(summary = "启动任务", description = "启动任务", method = "POST")
	public Result<QuartzJob> run(@PathVariable(value = "id") String id) {
		boolean result = quartzService.resume(id);
		if(result) {
			return Result.OK("启动成功", null);
		}else {
			return Result.error("启动失败");
		}
	}
	
	@PostMapping("pause/{id}")
	@Operation(summary = "暂停任务", description = "暂停任务", method = "POST")
	public Result<QuartzJob> pause(@PathVariable(value = "id") String id) {
		boolean result = quartzService.pause(id);
		if(result) {
			return Result.OK("暂停成功", null);
		}else {
			return Result.error("暂停失败");
		}
		
	}
	
	@DeleteMapping("delete/{id}")
	@Operation(summary = "删除任务", description = "删除任务", method = "DELETE")
	public Result<QuartzJob> delete(@PathVariable(value = "id") String id) {
		quartzService.deleteJob(id);
		return Result.OK();
	}

	/**
	 * 立即执行
	 * @param id 任务ID
	 * @return 执行结果
	 */
	@GetMapping("execute")
	@Operation(summary = "立即执行", description = "立即执行", method = "GET")
	public Result<?> execute(@RequestParam(name = "id", required = true) String id) {
		QuartzJob quartzJob = quartzService.getById(id);
		if (quartzJob == null) {
			return Result.error("未找到对应实体");
		}
		try {
			quartzService.execute(quartzJob);
		} catch (Exception e) {
			//e.printStackTrace();
			return Result.error("执行失败!");
		}
		return Result.OK("执行成功!",null);
	}
	@GetMapping("executeByJobClassName")
	@Operation(summary = "根据JobClassName立即执行", description = "根据JobClassName立即执行", method = "GET")
	public Result<?> executeByJobClassName(@RequestParam(name = "jobClassName", required = true) String jobClassName) {
		Criteria criteria = Criteria.create();
		criteria.addCriterion("jobClassName", Operator.EQ,jobClassName);
		List<QuartzJob> quartzJobList = quartzService.list(criteria);
		if(quartzJobList==null|| quartzJobList.isEmpty()) {
			return Result.error("未找到对应实体");
		}
		if(quartzJobList.size() > 1) {
			return Result.error("找到多个实体 [ " + jobClassName + " ]");
		}
		QuartzJob quartzJob = quartzJobList.get(0);
		try {
			quartzService.execute(quartzJob);
		} catch (Exception e) {
			//e.printStackTrace();
			return Result.error("执行失败!");
		}
		return Result.OK("执行成功!",null);
	}
}
