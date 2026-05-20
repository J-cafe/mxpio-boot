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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.lowcode.entity.LowcodeModel;
import com.mxpioframework.lowcode.service.LowcodeModelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/lowcode/model")
@Tag(name = "LowcodeModel", description = "低代码模型管理")
public class LowcodeModelController {

	@Autowired
	private LowcodeModelService modelService;

	@GetMapping("/list")
	@Operation(summary = "获取模型列表")
	public List<LowcodeModel> list() {
		return modelService.listAll();
	}

	@GetMapping("/{modelCode}")
	@Operation(summary = "获取模型详情（含属性列表）")
	public LowcodeModel get(@PathVariable String modelCode) {
		return modelService.getByCode(modelCode);
	}

	@PostMapping
	@Operation(summary = "创建模型")
	public LowcodeModel save(@RequestBody LowcodeModel model) {
		return modelService.save(model);
	}

	@PutMapping("/{modelCode}")
	@Operation(summary = "更新模型")
	public LowcodeModel update(@PathVariable String modelCode, @RequestBody LowcodeModel model) {
		return modelService.update(modelCode, model);
	}

	@DeleteMapping("/{modelCode}")
	@Operation(summary = "删除模型")
	public void delete(@PathVariable String modelCode) {
		modelService.delete(modelCode);
	}

	@PostMapping("/import")
	@Operation(summary = "从JPA实体导入模型（从可用实体列表中选择）")
	public LowcodeModel importFromEntity(@RequestParam String entityClass) {
		return modelService.importFromEntity(entityClass);
	}

	@GetMapping("/available/entities")
	@Operation(summary = "获取可导入的@Entity类列表（供前端下拉选择）")
	public List<String> getAvailableEntities() {
		return modelService.getAvailableEntities();
	}
}
