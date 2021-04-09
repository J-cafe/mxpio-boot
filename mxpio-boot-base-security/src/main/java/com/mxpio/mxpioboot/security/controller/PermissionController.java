package com.mxpio.mxpioboot.security.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.mxpio.mxpioboot.common.vo.Result;
import com.mxpio.mxpioboot.security.entity.Element;
import com.mxpio.mxpioboot.security.entity.Permission;
import com.mxpio.mxpioboot.security.service.PermissionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年3月6日
 */
@Api(value = "PermissionController", tags = {"权限管理"})
@Controller("/permiss")
public class PermissionController {
	
	@Autowired
	private PermissionService permissionService;
	
	@GetMapping("/loadElements")
	@ApiOperation(value = "加载页面组件")
	public Result<Collection<Element>> loadElements(String pageId) throws Exception {
		return Result.OK(permissionService.loadElements(pageId));
	}
	
	@GetMapping("/loadPermissions")
	@ApiOperation(value = "加载权限")
	public Result<List<Permission>> loadPermissions(String roleId, String urlId) {
		return Result.OK(permissionService.loadPermissions(roleId, urlId));
	}
	
	@PostMapping("/save")
	@ApiOperation(value = "保存权限")
	public Result<Object> save(Permission permission) {
		permissionService.save(permission);
		return Result.OK();
	}

}
