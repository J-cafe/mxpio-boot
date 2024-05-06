package com.mxpioframework.multitenant.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.multitenant.domain.DataSourceInfo;
import com.mxpioframework.multitenant.manager.service.DataSourceInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "DataSourceInfoController", description = "多租户数据源接口")
@RestController("mxpio.multitenant.dataSourceInfoController")
@RequestMapping("/multitenant/ds/")
public class DataSourceInfoController {
	
	@Autowired
	private DataSourceInfoService dataSourceInfoService;
	
	@GetMapping("list")
	@Operation(summary = "数据源列表", description = "数据源列表", method = "GET")
	public Result<List<DataSourceInfo>> list() {
		return Result.OK(dataSourceInfoService.load());
	}
	
	@PostMapping("add")
	@Operation(summary = "新增数据源", description = "新增数据源", method = "POST")
	public Result<List<DataSourceInfo>> save(List<DataSourceInfo> organizations) {
		dataSourceInfoService.save(organizations);
		return Result.OK(organizations);
	}
	
	@GetMapping("check/{dataSourceInfoId}")
	@Operation(summary = "查询数据源是否存在", description = "查询数据源是否存在", method = "GET")
	public Result<Object> isExist(@PathVariable String dataSourceInfoId) {
		if (dataSourceInfoService.isExist(dataSourceInfoId)) {
			return Result.error("数据源ID已经存在。");
		}
		return Result.OK();
	}

}
