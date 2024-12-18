package com.mxpioframework.security.controller;

import java.util.List;
import java.util.Set;

import com.mxpioframework.security.util.SecurityUtils;
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
import com.mxpioframework.security.entity.DataFilter;
import com.mxpioframework.security.service.DataFilterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "DataFilterController", description = "数据过滤管理")
@RestController("mxpio.security.dataFilterController")
@RequestMapping("/datafilter/")
public class DataFilterController {
	
	@Autowired
	private DataFilterService dataFilterService;
	
	@GetMapping("list")
	@Operation(summary = "数据过滤列表", description = "获取数据过滤列表", method = "GET")
	public Result<List<DataFilter>> page(Criteria criteria) throws Exception {
		List<DataFilter> dataFilters = dataFilterService.list(criteria);
		return Result.OK(dataFilters);
	}
	
	@GetMapping("list/{id}")
	@Operation(summary = "获取数据过滤", description = "根据ID获取数据过滤", method = "GET")
	public Result<DataFilter> getById(@PathVariable(name = "id", required = true) String id) throws Exception {
		DataFilter dataFilter = dataFilterService.getById(id);
		return Result.OK(dataFilter);
	}
	
	@GetMapping("res/list/{resId}")
	@Operation(summary = "获取数据过滤", description = "根据resId获取数据过滤", method = "GET")
	public Result<List<DataFilter>> getByResourceId(@PathVariable(name = "resId", required = true) String resId) throws Exception {
		List<DataFilter> dataFilters = dataFilterService.getByResourceId(resId);
		return Result.OK(dataFilters);
	}
	
	@PostMapping("add")
	@Operation(summary = "添加数据过滤", description = "添加数据过滤信息", method = "POST")
	public Result<DataFilter> add(@RequestBody DataFilter dataFilter) throws Exception {
		dataFilterService.save(dataFilter);
		return Result.OK("添加成功",dataFilter);
	}
	
	@PutMapping("edit")
	@Operation(summary = "更新数据过滤", description = "更新数据过滤信息", method = "PUT")
	public Result<DataFilter> edit(@RequestBody DataFilter dataFilter) throws Exception {
		dataFilterService.save(dataFilter);
		return Result.OK("编辑成功",dataFilter);
	}
	
	@DeleteMapping("remove/{id}")
	@Operation(summary = "删除数据过滤", description = "根据数据过滤名id删除数据过滤信息", method = "DELETE")
	public Result<DataFilter> delete(@PathVariable(name = "id", required = true) String id) throws Exception {
		String[] ids = id.split(",");
		for(String key : ids){
			dataFilterService.delete(dataFilterService.getById(key));
		}
		return Result.OK("删除成功",null);
	}

	@GetMapping("datascope/servers")
	@Operation(summary = "服务列表", description = "数据权限过滤服务列表", method = "GET")
	public Result<Set<String>> servers() {
		Set<String> servers = SecurityUtils.getDataScapeProviderMap().keySet();
		return Result.OK(servers);
	}
	
}
