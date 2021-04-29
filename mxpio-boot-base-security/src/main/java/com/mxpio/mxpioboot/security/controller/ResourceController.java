package com.mxpio.mxpioboot.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxpio.mxpioboot.common.vo.Result;
import com.mxpio.mxpioboot.security.entity.Element;
import com.mxpio.mxpioboot.security.entity.Url;
import com.mxpio.mxpioboot.security.service.ElementService;
import com.mxpio.mxpioboot.security.service.UrlService;

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
	public Result<List<Url>> loadAllUrl() {
		return Result.OK(urlService.findAll());
	}
	
	@PostMapping("/saveUrl")
	@ApiOperation(value = "保存菜单")
	public Result<Object> saveUrl(Url url) {
		urlService.save(url);
		return Result.OK();
	}
	
	@PutMapping("/updateUrl")
	@ApiOperation(value = "更新菜单")
	public Result<Object> updateUrl(Url url) {
		urlService.update(url);
		return Result.OK();
	}
	
	@DeleteMapping("/deleteUrl")
	@ApiOperation(value = "删除菜单")
	public Result<Object> deleteUrl(Url url) {
		urlService.delete(url);
		return Result.OK();
	}
	
	@PostMapping("/saveElement")
	@ApiOperation(value = "保存菜单")
	public Result<Object> saveElement(Element element) {
		elementService.save(element);
		return Result.OK();
	}
	
	@PutMapping("/updateElement")
	@ApiOperation(value = "更新菜单")
	public Result<Object> updateElement(Element element) {
		elementService.update(element);
		return Result.OK();
	}
	
	@DeleteMapping("/deleteElement")
	@ApiOperation(value = "删除菜单")
	public Result<Object> deleteElement(Element element) {
		elementService.delete(element);
		return Result.OK();
	}

}
