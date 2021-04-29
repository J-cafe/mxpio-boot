package com.mxpio.mxpioboot.security.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mxpio.mxpioboot.common.vo.Result;
import com.mxpio.mxpioboot.security.entity.Element;
import com.mxpio.mxpioboot.security.entity.Permission;
import com.mxpio.mxpioboot.security.entity.Url;
import com.mxpio.mxpioboot.security.service.PermissionService;
import com.mxpio.mxpioboot.security.service.RoleUrlService;
import com.mxpio.mxpioboot.security.service.UrlService;
import com.mxpio.mxpioboot.security.vo.RouterMetaVo;
import com.mxpio.mxpioboot.security.vo.RouterVo;

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
		return Result.OK(buildRouter(urls));
	}
	
	private List<RouterVo> buildRouter(List<Url> urls){
		List<RouterVo> routers = new ArrayList<>();
		for (Url url : urls) {
			RouterMetaVo meta = new RouterMetaVo();
			meta.setHidden(url.isNavigable());
			meta.setIcon(url.getIcon());
			meta.setKeepAlive(url.isKeepAlive());
			meta.setTitle(url.getTitle());
			RouterVo router = RouterVo.builder().name(url.getName()).meta(meta).component(url.getComponent())
					.path(url.getPath()).build();
			if (CollectionUtils.isNotEmpty(url.getChildren())) {
				router.setChildren(buildRouter(url.getChildren()));
			}
			routers.add(router);
		}
		return routers;
	}
	
	@PostMapping("/save")
	@ApiOperation(value = "保存权限")
	public Result<Object> save(Permission permission) {
		permissionService.save(permission);
		return Result.OK();
	}

}
