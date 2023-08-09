package com.mxpioframework.system.controller;

import com.mxpioframework.system.service.SnRuleService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.system.service.CommonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "CommonController", description = "通用接口")
@RestController("mxpio.system.commonController")
@RequestMapping("/common/")
public class CommonController {
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private SnRuleService snRuleService;
	@GetMapping("duplicate/{tableName}/{column}/{key}")
	@Operation(summary = "重复校验", description = "重复校验", method = "GET")
	public Result<Long> duplicate(@PathVariable(name = "tableName", required = true) String tableName,
			@PathVariable(name = "column", required = true) String column,
			@PathVariable(name = "key", required = true) String key,
			@RequestParam(name = "exclude", required = false) String exclude) {
		
		Long count = commonService.duplicate(tableName, column, key,exclude);
		return Result.OK(count);
	}

	@GetMapping("sngenerate/{snExpression}")
	@Operation(summary = "序列号生成器", description = "序列号生成器", method = "GET")
	public Result<Object> snGenerate(@Parameter(description="样例：SN${YYYY}${MM}${DD}####，生成结果：SN202308080001") @PathVariable(name = "snExpression", required = true) String snExpression) {
		Object sn = snRuleService.execute(snExpression, null);
		return Result.OK(sn);
	}
}
