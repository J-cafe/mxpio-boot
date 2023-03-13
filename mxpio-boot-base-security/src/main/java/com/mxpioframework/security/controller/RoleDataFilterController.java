package com.mxpioframework.security.controller;

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
import com.mxpioframework.security.entity.RoleDataFilter;
import com.mxpioframework.security.service.RoleDataFilterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "RoleDataFilterController", description = "角色与数据过滤关系管理")
@RestController("mxpio.security.roleDataFilterController")
@RequestMapping("/datafilter/role")
public class RoleDataFilterController {
	
	@Autowired
	private RoleDataFilterService roleDataFilterService;
	
	@GetMapping("list")
	@Operation(summary = "角色与数据过滤关系列表", description = "获取角色与数据过滤关系列表", method = "GET")
	public Result<List<RoleDataFilter>> page(Criteria criteria) throws Exception {
		List<RoleDataFilter> roleDataFilters = roleDataFilterService.list(criteria);
		return Result.OK(roleDataFilters);
	}
	
	@GetMapping("list/{id}")
	@Operation(summary = "获取角色与数据过滤关系", description = "根据ID获取角色与数据过滤关系", method = "GET")
	public Result<RoleDataFilter> getById(@PathVariable(name = "id", required = true) String id) throws Exception {
		RoleDataFilter roleDataFilter = roleDataFilterService.getById(id);
		return Result.OK(roleDataFilter);
	}
	
	@PostMapping("add")
	@Operation(summary = "添加角色与数据过滤关系", description = "添加角色与数据过滤关系信息", method = "POST")
	public Result<RoleDataFilter> add(@RequestBody RoleDataFilter roleDataFilter) throws Exception {
		roleDataFilterService.save(roleDataFilter);
		return Result.OK("添加成功",roleDataFilter);
	}
	
	@PutMapping("edit")
	@Operation(summary = "更新角色与数据过滤关系", description = "更新角色与数据过滤关系信息", method = "PUT")
	public Result<RoleDataFilter> edit(@RequestBody RoleDataFilter roleDataFilter) throws Exception {
		roleDataFilterService.save(roleDataFilter);
		return Result.OK("编辑成功",roleDataFilter);
	}
	
	@DeleteMapping("remove/{id}")
	@Operation(summary = "删除角色与数据过滤关系", description = "根据角色与数据过滤关系名id删除角色与数据过滤关系信息", method = "DELETE")
	public Result<RoleDataFilter> delete(@PathVariable(name = "id", required = true) String id) throws Exception {
		String ids[] = id.split(",");
		for(String key : ids){
			roleDataFilterService.delete(roleDataFilterService.getById(key));
		}
		return Result.OK("删除成功",null);
	}
	
}
