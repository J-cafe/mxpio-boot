package com.mxpioframework.system.controller;

import com.mxpioframework.system.entity.SNExpression;
import com.mxpioframework.system.service.SnRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

	//@GetMapping("sngenerate/{snExpression}")
	@RequestMapping("sngenerate")
	@Operation(summary = "序列号生成器", description = "序列号生成器", method = "POST")
	public Result<Object> snGenerate(@RequestBody SNExpression snExpression) {
		Object sn = snRuleService.execute(snExpression.getSnExpression(), null);
		return Result.OK(sn);
	}
}
