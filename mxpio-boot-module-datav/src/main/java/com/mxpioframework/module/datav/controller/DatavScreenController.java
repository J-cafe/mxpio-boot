package com.mxpioframework.module.datav.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.module.datav.entity.DatavScreen;
import com.mxpioframework.module.datav.service.DatavScreenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "DatavScreenController", description = "datav接口")
@RestController("mxpio.datav.datavScreenController")
@RequestMapping("/datav/")
public class DatavScreenController {
	
	@Autowired
	private DatavScreenService datavScreenService;

	@GetMapping("list")
	@Operation(summary = "datav大屏列表", description = "获取datav大屏列表", method = "GET")
	public Result<List<DatavScreen>> page(Criteria criteria) throws Exception {
		List<DatavScreen> datavScreens = datavScreenService.list(criteria);
		return Result.OK(datavScreens);
	}
	
	@GetMapping("list/{id}")
	@Operation(summary = "获取datav大屏", description = "根据ID获取datav大屏", method = "GET")
	public Result<DatavScreen> getById(@PathVariable(name = "id", required = true) String id) throws Exception {
		DatavScreen datavScreen = datavScreenService.getById(id);
		return Result.OK(datavScreen);
	}
	
	@PostMapping("add")
	@Operation(summary = "添加datav大屏", description = "添加datav大屏信息", method = "POST")
	public Result<DatavScreen> add(@RequestBody DatavScreen datavScreen) throws Exception {
		datavScreenService.save(datavScreen);
		return Result.OK("添加成功",datavScreen);
	}
	
	@PutMapping("edit")
	@Operation(summary = "更新datav大屏", description = "更新datav大屏信息", method = "PUT")
	public Result<DatavScreen> edit(@RequestBody DatavScreen datavScreen) throws Exception {
		datavScreenService.save(datavScreen);
		return Result.OK("编辑成功",datavScreen);
	}
	
	@DeleteMapping("remove/{id}")
	@Operation(summary = "删除datav大屏", description = "根据datav大屏id删除datav大屏信息", method = "DELETE")
	public Result<DatavScreen> delete(@PathVariable(name = "id", required = true) String id) throws Exception {
		String ids[] = id.split(",");
		for(String key : ids){
			datavScreenService.delete(datavScreenService.getById(key));
		}
		return Result.OK("删除成功",null);
	}
}
