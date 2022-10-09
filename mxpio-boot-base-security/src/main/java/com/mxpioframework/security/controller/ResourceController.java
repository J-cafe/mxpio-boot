package com.mxpioframework.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.mxpioframework.security.cache.SecurityCacheEvict;
import com.mxpioframework.security.entity.DataResource;
import com.mxpioframework.security.entity.Element;
import com.mxpioframework.security.entity.Url;
import com.mxpioframework.security.service.DataResourceService;
import com.mxpioframework.security.service.ElementService;
import com.mxpioframework.security.service.UrlService;
import com.mxpioframework.security.vo.DataVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "ResourceController", description = "资源管理")
@RestController("mxpio.security.resourceController")
@RequestMapping("/res/")
public class ResourceController {
	
	@Autowired
	private UrlService urlService;
	
	@Autowired
	private ElementService elementService;
	
	@Autowired
	private DataResourceService dataResourceService;
	
	@GetMapping("url/list")
	@Operation(summary = "获取全部菜单", description = "获取全部菜单", method = "GET")
	public Result<List<Url>> loadAllUrl() {
		List<Url> urls = urlService.findAllTree();
		return Result.OK(urls);
	}
	
	@GetMapping("data/api/list")
	@Operation(summary = "获取全部接口", description = "获取全部接口", method = "GET")
	public Result<List<DataVo>> loadAllData() {
		List<DataVo> datas = dataResourceService.findAll();
		return Result.OK(datas);
	}
	
	@GetMapping("data/list/{urlId}")
	@Operation(summary = "根据菜单获取数据资源", description = "根据菜单获取数据资源", method = "GET")
	public Result<List<DataResource>> loadData(@PathVariable(name = "urlId") String urlId) {
		List<DataResource> datas = dataResourceService.getByUrlId(urlId);
		return Result.OK(datas);
	}
	
	@PostMapping("data/add")
	@Operation(summary = "新增数据资源", description = "新增数据资源", method = "POST")
	@SecurityCacheEvict
	public Result<Object> saveData(@RequestBody DataResource data) {
		data.setId(null);
		dataResourceService.save(data);
		return Result.OK(data);
	}
	
	@PutMapping("data/edit")
	@Operation(summary = "更新数据资源", description = "更新数据资源", method = "PUT")
	@SecurityCacheEvict
	public Result<Object> updateData(@RequestBody DataResource data) {
		dataResourceService.update(data);
		return Result.OK(data);
	}
	
	@DeleteMapping("data/remove/{id}")
	@Operation(summary = "删除数据资源", description = "删除数据资源", method = "DELETE")
	@SecurityCacheEvict
	public Result<Object> deleteData(@PathVariable(name = "id") String id) {
		dataResourceService.delete(id, DataResource.class);
		return Result.OK();
	}
	
	@PostMapping("url/add")
	@Operation(summary = "新增菜单", description = "新增菜单", method = "POST")
	@SecurityCacheEvict
	public Result<Object> saveUrl(@RequestBody Url url) {
		url.setId(null);
		urlService.save(url);
		return Result.OK(url);
	}
	
	@PutMapping("url/edit")
	@Operation(summary = "更新菜单", description = "更新菜单信息", method = "PUT")
	@SecurityCacheEvict
	public Result<Object> updateUrl(@RequestBody Url url) {
		urlService.update(url);
		return Result.OK(url);
	}
	
	@DeleteMapping("url/remove")
	@Operation(summary = "删除菜单", description = "删除菜单信息", method = "DELETE")
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
	@Operation(summary = "新增组件", description = "新增组件信息", method = "POST")
	@SecurityCacheEvict
	public Result<Object> saveElement(@RequestBody Element element) {
		elementService.save(element);
		return Result.OK();
	}
	
	@PutMapping("element/edit")
	@Operation(summary = "更新组件", description = "更新组件信息", method = "PUT")
	@SecurityCacheEvict
	public Result<Object> updateElement(@RequestBody Element element) {
		elementService.update(element);
		return Result.OK();
	}
	
	@DeleteMapping("element/remove")
	@Operation(summary = "删除组件", description = "删除组件信息", method = "DELETE")
	@SecurityCacheEvict
	public Result<Object> deleteElement(String id) {
		elementService.delete(id, Element.class);
		return Result.OK();
	}

}
