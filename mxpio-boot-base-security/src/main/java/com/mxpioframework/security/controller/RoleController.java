package com.mxpioframework.security.controller;

import java.util.List;

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
import com.mxpioframework.jpa.query.CriteriaUtils;
import com.mxpioframework.security.entity.Role;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "RoleController", tags = {"角色管理"})
@RestController("mxpio.security.roleController")
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@GetMapping("/page")
	@ApiOperation(value = "角色列表(分页)", notes = "获取角色列表(分页)", httpMethod = "GET")
	public Result<Page<Role>> page(String criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		Page<Role> page = roleService.listPage(c, pageAble);
		return Result.OK(page);
	}
	
	@GetMapping("/list")
	@ApiOperation(value = "角色列表", notes = "获取角色列表", httpMethod = "GET")
	public Result<List<Role>> page(String criteria) throws Exception {
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		List<Role> roles = roleService.list(c);
		return Result.OK(roles);
	}
	
	@GetMapping("/list/{id}")
	@ApiOperation(value = "获取角色", notes = "根据ID获取角色", httpMethod = "GET")
	public Result<Role> getById(@PathVariable(name = "id", required = true) String id) throws Exception {
		Role role = roleService.getById(id);
		return Result.OK(role);
	}
	
	@PostMapping("/add")
	@ApiOperation(value = "添加角色", notes = "添加角色信息", httpMethod = "POST")
	public Result<Role> add(@RequestBody Role role) throws Exception {
		roleService.save(role);
		return Result.OK("添加成功",role);
	}
	
	@PutMapping("/edit")
	@ApiOperation(value = "更新角色", notes = "更新角色信息", httpMethod = "PUT")
	public Result<Role> edit(@RequestBody Role role) throws Exception {
		roleService.update(role);
		return Result.OK("编辑成功",role);
	}
	
	@DeleteMapping("/remove/{id}")
	@ApiOperation(value = "删除角色", notes = "根据角色名rolename删除角色信息", httpMethod = "DELETE")
	public Result<Role> delete(@PathVariable(name = "id", required = true) String id) throws Exception {
		String ids[] = id.split(",");
		for(String key : ids){
			roleService.delete(key);
		}
		return Result.OK("删除成功",null);
	}
	
	@GetMapping("/list/{id}/without")
	@ApiOperation(value = "未绑定用户", notes = "获取未绑定用户列表", httpMethod = "GET")
	public Result<Page<User>> getUsersWithout(
			@PathVariable(name = "id",required = true) String id,
			String criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		Page<User> users = roleService.getUsersWithout(pageAble, c, id);
		return Result.OK(users);
	}
	
	@GetMapping("/list/{id}/within")
	@ApiOperation(value = "获取绑定用户", notes = "获取绑定用户列表", httpMethod = "GET")
	public Result<Page<User>> getUsersWithin(
			@PathVariable(name = "id",required = true) String id,
			String criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		Page<User> users = roleService.getUsersWithin(pageAble, c, id);
		return Result.OK(users);
	}
	
	@PostMapping("/list/{id}/add/actors")
	@ApiOperation(value = "添加绑定权限演员", notes = "获取绑定权限演员列表", httpMethod = "POST")
	public Result<?> addActors(
			@PathVariable(name = "id",required = true) String id,
			@RequestBody List<String> actorIds) throws Exception {
		roleService.addActors(id, actorIds);
		return Result.OK();
	}
	
	@PostMapping("/list/{id}/remove/actors")
	@ApiOperation(value = "添加绑定权限演员", notes = "获取绑定权限演员列表", httpMethod = "POST")
	public Result<?> removeActors(
			@PathVariable(name = "id",required = true) String id,
			@RequestBody List<String> actorIds) throws Exception {
		roleService.removeActors(id, actorIds);
		return Result.OK();
	}
	
}
