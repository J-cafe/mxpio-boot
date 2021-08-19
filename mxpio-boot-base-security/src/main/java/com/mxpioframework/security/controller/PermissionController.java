package com.mxpioframework.security.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.security.entity.Element;
import com.mxpioframework.security.entity.Permission;
import com.mxpioframework.security.entity.Url;
import com.mxpioframework.security.service.PermissionService;
import com.mxpioframework.security.service.RoleUrlService;
import com.mxpioframework.security.service.UrlService;
import com.mxpioframework.security.util.RouterUtil;
import com.mxpioframework.security.vo.RouterVo;

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
	
	@Autowired
	private UrlService urlService;
	
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
	
	@GetMapping("/loadUrl")
	@ApiOperation(value = "加载权限")
	public Result<List<RouterVo>> loadUrl() {
		List<Url> urls = urlService.findTreeByUsername(null);
		return Result.OK(RouterUtil.buildRouter(urls));
	}
	
	@PostMapping("/save")
	@ApiOperation(value = "保存权限")
	public Result<Object> save(Permission permission) {
		permissionService.save(permission);
		return Result.OK();
	}

}
