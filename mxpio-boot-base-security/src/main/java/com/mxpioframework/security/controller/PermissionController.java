package com.mxpioframework.security.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

@Api(value = "PermissionController", tags = {"授权管理"})
@RestController
@RequestMapping("/permiss/")
public class PermissionController {
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private RoleUrlService roleUrlService;
	
	@Autowired
	private UrlService urlService;
	
	@GetMapping("list")
	@ApiOperation(value = "授权信息", notes = "根据登录用户获取权限信息", httpMethod = "GET")
	public Result<List<Permission>> loadPermissions(
			@RequestParam(value = "roleId", required = false) String roleId) {
		return Result.OK(roleUrlService.load(roleId));
	}
	
	@PostMapping("add")
	@ApiOperation(value = "保存权限", notes = "新增权限信息", httpMethod = "POST")
	public Result<Object> save(@RequestBody Permission permission) {
		permissionService.save(permission);
		return Result.OK(permission);
	}
	
	@PostMapping("add/batch")
	@ApiOperation(value = "保存权限", notes = "新增权限信息", httpMethod = "POST")
	public Result<Object> saveBatch(@RequestBody List<Permission> permissions) {
		permissionService.save(permissions);
		return Result.OK(permissions);
	}
	
	@GetMapping("element/list")
	@ApiOperation(value = "加载页面组件", notes = "根据pageId获取已授权的组件清单", httpMethod = "GET")
	public Result<Collection<Element>> loadElements(String pageId) throws Exception {
		return Result.OK(permissionService.loadElements(pageId));
	}
	
	@GetMapping("url/list")
	@ApiOperation(value = "加载已授权路由", notes = "根据登录用户获取已授权的路由信息", httpMethod = "GET")
	public Result<List<RouterVo>> loadUrl() {
		List<Url> urls = urlService.findTreeByUsername(null);
		return Result.OK(RouterUtil.buildRouter(urls));
	}
	
}
