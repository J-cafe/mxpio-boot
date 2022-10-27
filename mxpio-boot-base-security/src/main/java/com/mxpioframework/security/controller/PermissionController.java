package com.mxpioframework.security.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.security.entity.DataResource;
import com.mxpioframework.security.entity.Element;
import com.mxpioframework.security.entity.Permission;
import com.mxpioframework.security.entity.Url;
import com.mxpioframework.security.service.DataResourceService;
import com.mxpioframework.security.service.PermissionService;
import com.mxpioframework.security.service.RoleUrlService;
import com.mxpioframework.security.service.UrlService;
import com.mxpioframework.security.util.RouterUtil;
import com.mxpioframework.security.vo.RouterVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "PermissionController", description = "授权管理")
@RestController("mxpio.security.permissionController")
@RequestMapping("/permiss/")
public class PermissionController {
	
	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private RoleUrlService roleUrlService;
	
	@Autowired
	private UrlService urlService;
	
	@Autowired
	private DataResourceService dataResourceService;
	
	@GetMapping("list")
	@Operation(summary = "授权信息", description = "根据登录用户获取权限信息", method = "GET")
	public Result<List<Permission>> loadPermissions(
			@RequestParam(value = "roleId", required = false) String roleId) {
		return Result.OK(roleUrlService.load(roleId));
	}
	
	@PostMapping("add")
	@Operation(summary = "保存权限", description = "新增权限信息", method = "POST")
	public Result<Object> save(@RequestBody Permission permission) {
		permissionService.save(permission);
		return Result.OK(permission);
	}
	
	@PostMapping("add/batch")
	@Operation(summary = "保存权限", description = "新增权限信息", method = "POST")
	public Result<Object> saveBatch(@RequestBody List<Permission> permissions) {
		permissionService.save(permissions);
		return Result.OK(permissions);
	}
	
	@GetMapping("element/list")
	@Operation(summary = "加载页面组件", description = "根据pageId获取已授权的组件清单", method = "GET")
	public Result<Collection<Element>> loadElements(String pageId) throws Exception {
		return Result.OK(permissionService.loadElements(pageId));
	}
	
	@GetMapping("url/list")
	@Operation(summary = "加载已授权路由", description = "根据登录用户获取已授权的路由信息", method = "GET")
	public Result<List<RouterVo>> loadUrl() {
		List<Url> urls = urlService.findTreeByUsername(null);
		return Result.OK(RouterUtil.buildRouter(urls));
	}
	
	@GetMapping("data/list")
	@Operation(summary = "加载已授权数据权限", description = "根据登录用户获取已授权的数据权限", method = "GET")
	public Result<Map<String, List<DataResource>>> loadDataResource(){
		Map<String, List<DataResource>> result = new HashMap<>();
		result.put("datas", dataResourceService.findByUsername(null));
		result.put("allDatas", dataResourceService.findAll());
		return Result.OK(result);
	}
	
}
