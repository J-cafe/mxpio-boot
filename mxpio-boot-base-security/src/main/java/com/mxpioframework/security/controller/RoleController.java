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
import com.mxpioframework.security.entity.Role;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "RoleController", description = "角色管理")
@RestController("mxpio.security.roleController")
@RequestMapping("/role/")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@GetMapping("page")
	@Operation(summary = "角色列表(分页)", description = "获取角色列表(分页)", method = "GET")
	public Result<Page<Role>> page(Criteria criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Page<Role> page = roleService.listPage(criteria, pageAble);
		return Result.OK(page);
	}
	
	@GetMapping("list")
	@Operation(summary = "角色列表", description = "获取角色列表", method = "GET")
	public Result<List<Role>> page(Criteria criteria) throws Exception {
		List<Role> roles = roleService.list(criteria);
		return Result.OK(roles);
	}
	
	@GetMapping("list/{id}")
	@Operation(summary = "获取角色", description = "根据ID获取角色", method = "GET")
	public Result<Role> getById(@PathVariable(name = "id", required = true) String id) throws Exception {
		Role role = roleService.getById(id);
		return Result.OK(role);
	}
	
	@PostMapping("add")
	@Operation(summary = "添加角色", description = "添加角色信息", method = "POST")
	public Result<Role> add(@RequestBody Role role) throws Exception {
		roleService.save(role);
		return Result.OK("添加成功",role);
	}
	
	@PutMapping("edit")
	@Operation(summary = "更新角色", description = "更新角色信息", method = "PUT")
	public Result<Role> edit(@RequestBody Role role) throws Exception {
		roleService.update(role);
		return Result.OK("编辑成功",role);
	}
	
	@DeleteMapping("remove/{id}")
	@Operation(summary = "删除角色", description = "根据角色名rolename删除角色信息", method = "DELETE")
	public Result<Role> delete(@PathVariable(name = "id", required = true) String id) throws Exception {
		String ids[] = id.split(",");
		for(String key : ids){
			roleService.delete(key);
		}
		return Result.OK("删除成功",null);
	}
	
	@GetMapping("list/{id}/without")
	@Operation(summary = "未绑定用户", description = "获取未绑定用户列表", method = "GET")
	public Result<Page<User>> getUsersWithout(
			@PathVariable(name = "id",required = true) String id,
			Criteria criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Page<User> users = roleService.getUsersWithout(pageAble, criteria, id);
		return Result.OK(users);
	}
	
	@GetMapping("list/{id}/within")
	@Operation(summary = "获取绑定用户", description = "获取绑定用户列表", method = "GET")
	public Result<Page<User>> getUsersWithin(
			@PathVariable(name = "id",required = true) String id,
			Criteria criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Page<User> users = roleService.getUsersWithin(pageAble, criteria, id);
		return Result.OK(users);
	}
	
	@PostMapping("list/{id}/add/actors")
	@Operation(summary = "添加绑定权限演员", description = "获取绑定权限演员列表", method = "POST")
	public Result<?> addActors(
			@PathVariable(name = "id",required = true) String id,
			@RequestBody List<String> actorIds) throws Exception {
		roleService.addActors(id, actorIds);
		return Result.OK();
	}
	
	@PostMapping("list/{id}/remove/actors")
	@Operation(summary = "添加绑定权限演员", description = "获取绑定权限演员列表", method = "POST")
	public Result<?> removeActors(
			@PathVariable(name = "id",required = true) String id,
			@RequestBody List<String> actorIds) throws Exception {
		roleService.removeActors(id, actorIds);
		return Result.OK();
	}
	
}
