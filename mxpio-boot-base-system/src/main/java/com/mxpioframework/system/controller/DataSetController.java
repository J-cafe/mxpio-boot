package com.mxpioframework.system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.ds.DataSet;
import com.mxpioframework.common.vo.Result;
import com.mxpioframework.system.service.DataSetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "DataSetController", description = "数据集接口")
@RestController("mxpio.system.dataSetController")
@RequestMapping("/sys/dataset/")
public class DataSetController {
	
	@Autowired
	private DataSetService dataSetService;

	@GetMapping("list")
	@Operation(summary = "数据集列表", description = "获取数据集列表", method = "GET")
	public Result<Map<String, List<? extends DataSet>>> list() {
		return Result.OK(dataSetService.list());
	}
	
	@GetMapping("{type}/{code}/data")
	@Operation(summary = "数据集数据", description = "获取数据集数据", method = "GET")
	public Result<Object> data(@PathVariable(name = "type", required = true) String type,
			@PathVariable(name = "code", required = true) String code) {
		return Result.OK(dataSetService.getData(type, code));
	}
	
	@GetMapping("{type}/{code}/page")
	@Operation(summary = "数据集分页数据", description = "获取数据集分页数据", method = "GET")
	public Result<Page<?>> page(@PathVariable(name = "type", required = true) String type,
			@PathVariable(name = "code", required = true) String code,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		return Result.OK(dataSetService.getData(type, code, pageAble));
	}
	
}
