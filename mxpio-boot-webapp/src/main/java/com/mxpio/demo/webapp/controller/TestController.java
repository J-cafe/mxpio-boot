package com.mxpio.demo.webapp.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxpio.mxpioboot.common.vo.Result;
import com.mxpio.mxpioboot.jpa.JpaUtil;
import com.mxpio.mxpioboot.security.entity.Permission;
import com.mxpio.mxpioboot.security.entity.Role;
import com.mxpio.mxpioboot.security.entity.RoleGrantedAuthority;
import com.mxpio.mxpioboot.security.entity.Url;
import com.mxpio.mxpioboot.security.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "测试接口")
@RestController
@RequestMapping("/test")
public class TestController {
	
	@ApiOperation(value = "测试接口-添加URL", notes = "测试接口-添加URL")
	@PostMapping(value = "/addUrl")
	@Transactional(readOnly = false)
	public Result<?> addUrl(Url url) {
		JpaUtil.save(url);
		return Result.OK();
	}
	
	@ApiOperation(value = "测试接口-添加User", notes = "测试接口-添加User")
	@PostMapping(value = "/addUser")
	@Transactional(readOnly = false)
	public Result<?> addUser(User user) {
		JpaUtil.save(user);
		return Result.OK();
	}
	
	@ApiOperation(value = "测试接口-添加Role", notes = "测试接口-添加Role")
	@PostMapping(value = "/addRole")
	@Transactional(readOnly = false)
	public Result<?> addRole(Role role) {
		JpaUtil.save(role);
		return Result.OK();
	}
	
	@ApiOperation(value = "测试接口-添加addRoleGrantedAuthority", notes = "测试接口-添加addRoleGrantedAuthority")
	@PostMapping(value = "/addRoleGrantedAuthority")
	@Transactional(readOnly = false)
	public Result<?> addRoleGrantedAuthority(RoleGrantedAuthority rga) {
		JpaUtil.save(rga);
		return Result.OK();
	}
	
	@ApiOperation(value = "测试接口-添加addPermission", notes = "测试接口-添加addPermission")
	@PostMapping(value = "/addPermission")
	@Transactional(readOnly = false)
	public Result<?> addPermission(Permission permission) {
		JpaUtil.save(permission);
		return Result.OK();
	}
	
	@ApiOperation(value = "测试接口-登录", notes = "测试接口-登录")
	@PostMapping(value = "/login",produces = "application/json")
	public Result<?> login(String username,String password) {
		return Result.OK();
	}

}
