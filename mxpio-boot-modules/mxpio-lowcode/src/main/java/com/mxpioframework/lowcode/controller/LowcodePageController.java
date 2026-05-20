package com.mxpioframework.lowcode.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.lowcode.entity.LowcodePage;
import com.mxpioframework.lowcode.service.LowcodePageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/lowcode/page")
@Tag(name = "LowcodePage", description = "低代码页面管理")
public class LowcodePageController {

	@Autowired
	private LowcodePageService pageService;

	@GetMapping("/list")
	@Operation(summary = "获取页面列表")
	public List<LowcodePage> list() {
		return pageService.listAll();
	}

	@GetMapping("/{pageCode}")
	@Operation(summary = "获取完整页面配置")
	public LowcodePage get(@PathVariable String pageCode) {
		return pageService.getByCode(pageCode);
	}

	@PostMapping
	@Operation(summary = "创建页面")
	public LowcodePage save(@RequestBody LowcodePage page) {
		return pageService.save(page);
	}

	@PutMapping("/{pageCode}")
	@Operation(summary = "更新页面")
	public LowcodePage update(@PathVariable String pageCode,
			@RequestBody LowcodePage page) {
		return pageService.update(pageCode, page);
	}

	@DeleteMapping("/{pageCode}")
	@Operation(summary = "删除页面")
	public void delete(@PathVariable String pageCode) {
		pageService.delete(pageCode);
	}

	@PostMapping("/{pageCode}/publish")
	@Operation(summary = "发布页面")
	public void publish(@PathVariable String pageCode) {
		pageService.publish(pageCode);
	}
}
