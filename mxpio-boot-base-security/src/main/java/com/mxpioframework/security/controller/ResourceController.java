package com.mxpioframework.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.security.cache.SecurityCacheEvict;
import com.mxpioframework.security.entity.Element;
import com.mxpioframework.security.entity.Url;
import com.mxpioframework.security.service.ElementService;
import com.mxpioframework.security.service.UrlService;
import com.mxpioframework.security.util.RouterUtil;
import com.mxpioframework.security.vo.RouterVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "ResourceController", tags = {"资源管理"})
@RestController
@RequestMapping("/res")
public class ResourceController {
	
	@Autowired
	private UrlService urlService;
	
	@Autowired
	private ElementService elementService;
	
	@GetMapping("/loadAllUrl")
	@ApiOperation(value = "获取全部菜单")
	public Result<List<RouterVo>> loadAllUrl() {
		List<Url> urls = urlService.findAllTree();
		return Result.OK(RouterUtil.buildRouter(urls));
	}
	
	@PostMapping("/saveUrl")
	@ApiOperation(value = "保存菜单")
	@SecurityCacheEvict
	public Result<Object> saveUrl(@RequestBody RouterVo router) {
		Url url = RouterUtil.router2Url(router);
		url.setId(null);
		urlService.save(url);
		return Result.OK();
	}
	
	@PutMapping("/updateUrl")
	@ApiOperation(value = "更新菜单")
	@SecurityCacheEvict
	public Result<Object> updateUrl(@RequestBody RouterVo router) {
		urlService.update(RouterUtil.router2Url(router));
		return Result.OK();
	}
	
	@DeleteMapping("/deleteUrl")
	@ApiOperation(value = "删除菜单")
	@SecurityCacheEvict
	public Result<Object> deleteUrl(@RequestParam("id") String id) {
		boolean b = urlService.deleteBundleById(id);
		if(b) {
			return Result.OK();
		}else {
			return Result.error("请先删除菜单下的组件");
		}
		
	}
	
	@PostMapping("/saveElement")
	@ApiOperation(value = "保存组件")
	@SecurityCacheEvict
	public Result<Object> saveElement(Element element) {
		elementService.save(element);
		return Result.OK();
	}
	
	@PutMapping("/updateElement")
	@ApiOperation(value = "更新组件")
	@SecurityCacheEvict
	public Result<Object> updateElement(Element element) {
		elementService.update(element);
		return Result.OK();
	}
	
	@DeleteMapping("/deleteElement")
	@ApiOperation(value = "删除组件")
	@SecurityCacheEvict
	public Result<Object> deleteElement(String id) {
		elementService.delete(id, Element.class);
		return Result.OK();
	}

}
