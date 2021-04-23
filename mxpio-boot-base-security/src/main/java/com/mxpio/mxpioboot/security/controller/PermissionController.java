package com.mxpio.mxpioboot.security.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mxpio.mxpioboot.common.vo.Result;
import com.mxpio.mxpioboot.security.entity.Element;
import com.mxpio.mxpioboot.security.entity.Permission;
import com.mxpio.mxpioboot.security.service.PermissionService;
import com.mxpio.mxpioboot.security.service.RoleUrlService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "PermissionController", tags = {"权限管理"})
@RestController
@RequestMapping("/permiss")
public class PermissionController {
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private RoleUrlService roleUrlService;
	
	@GetMapping("/loadElements")
	@ApiOperation(value = "加载页面组件")
	public Result<Collection<Element>> loadElements(String pageId) throws Exception {
		return Result.OK(permissionService.loadElements(pageId));
	}
	
	@GetMapping("/loadPermissions")
	@ResponseBody
	@ApiOperation(value = "加载权限")
	public Result<List<Permission>> loadPermissions() {
		return Result.OK(roleUrlService.load());
	}
	
	@PostMapping("/save")
	@ApiOperation(value = "保存权限")
	public Result<Object> save(Permission permission) {
		permissionService.save(permission);
		return Result.OK();
	}

}
