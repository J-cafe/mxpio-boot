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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "ResourceController", tags = {"资源管理"})
@RestController
@RequestMapping("/res/")
public class ResourceController {
	
	@Autowired
	private UrlService urlService;
	
	@Autowired
	private ElementService elementService;
	
	@GetMapping("url/list")
	@ApiOperation(value = "获取全部菜单", notes = "获取全部菜单", httpMethod = "GET")
	public Result<List<Url>> loadAllUrl() {
		List<Url> urls = urlService.findAllTree();
		return Result.OK(urls);
	}
	
	@PostMapping("url/add")
	@ApiOperation(value = "新增菜单", notes = "新增菜单", httpMethod = "POST")
	@SecurityCacheEvict
	public Result<Object> saveUrl(@RequestBody Url url) {
		url.setId(null);
		urlService.save(url);
		return Result.OK();
	}
	
	@PutMapping("url/edit")
	@ApiOperation(value = "更新菜单", notes = "更新菜单信息", httpMethod = "PUT")
	@SecurityCacheEvict
	public Result<Object> updateUrl(@RequestBody Url url) {
		urlService.update(url);
		return Result.OK(url);
	}
	
	@DeleteMapping("url/remove")
	@ApiOperation(value = "删除菜单", notes = "删除菜单信息", httpMethod = "DELETE")
	@SecurityCacheEvict
	public Result<Object> deleteUrl(@RequestParam("id") String id) {
		boolean b = urlService.deleteBundleById(id);
		if(b) {
			return Result.OK();
		}else {
			return Result.error("请先删除菜单下的组件");
		}
		
	}
	
	@PostMapping("element/add")
	@ApiOperation(value = "新增组件", notes = "新增组件信息", httpMethod = "POST")
	@SecurityCacheEvict
	public Result<Object> saveElement(@RequestBody Element element) {
		elementService.save(element);
		return Result.OK();
	}
	
	@PutMapping("element/edit")
	@ApiOperation(value = "更新组件", notes = "更新组件信息", httpMethod = "PUT")
	@SecurityCacheEvict
	public Result<Object> updateElement(@RequestBody Element element) {
		elementService.update(element);
		return Result.OK();
	}
	
	@DeleteMapping("element/remove")
	@ApiOperation(value = "删除组件", notes = "删除组件信息", httpMethod = "DELETE")
	@SecurityCacheEvict
	public Result<Object> deleteElement(String id) {
		elementService.delete(id, Element.class);
		return Result.OK();
	}

}
