package com.mxpioframework.system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public Result<Map<String, List<DataSet>>> list() {
		return Result.OK(dataSetService.list());
	}
	
	@GetMapping("data")
	@Operation(summary = "数据集数据", description = "获取数据集数据", method = "GET")
	public Result<Object> data(@RequestBody DataSet dataSet) {
		return Result.OK(dataSetService.getData(dataSet));
	}
	
}
