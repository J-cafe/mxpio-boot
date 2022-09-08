package com.mxpioframework.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.system.service.CommonService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "CommonController", tags = { "通用接口" })
@RestController("mxpio.system.commonController")
@RequestMapping("/common/")
public class CommonController {
	
	private CommonService commonService;
	
	@GetMapping("duplicate/{tableName}/{column}/{key}")
	@ApiOperation(value = "重复校验", notes = "重复校验", httpMethod = "GET")
	public Result<Long> duplicate(@PathVariable(name = "tableName", required = true) String tableName,
			@PathVariable(name = "column", required = true) String column,
			@PathVariable(name = "key", required = true) String key) {
		
		Long count = commonService.duplicate(tableName, column, key);
		return Result.OK(count);
	}
}
