package com.mxpioframework.camunda.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.camunda.entity.FormModel;
import com.mxpioframework.camunda.entity.FormModelDef;
import com.mxpioframework.camunda.enums.BpmnEnums;
import com.mxpioframework.camunda.service.FormModelService;
import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.query.Criteria;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "FormModelController", description = "表单管理")
@RestController("mxpio.camunda.FormModelController")
@RequestMapping("/camunda/form/")
public class FormModelController {
	
	@Autowired
	private FormModelService formModelService;
	
	@GetMapping("list")
	@Operation(summary = "表单列表", description = "获取表单列表", method = "GET")
	public Result<List<FormModel>> list(Criteria criteria) {
		List<FormModel> formModels = formModelService.list(criteria);
		return Result.OK(formModels);
	}
	
	@GetMapping("page")
	@Operation(summary = "表单列表（分页）", description = "获取表单列表（分页）", method = "GET")
	public Result<Page<FormModel>> page(Criteria criteria, Integer pageSize, Integer pageNo) {
		Pageable page = PageRequest.of(pageNo-1, pageSize);
		Page<FormModel> formModels = formModelService.listPage(page, criteria);
		return Result.OK(formModels);
	}
	
	@GetMapping("def/list/{key}")
	@Operation(summary = "获取表单定义", description = "根据Key获取表单定义", method = "GET")
	public Result<FormModelDef> defList(@PathVariable(name = "key", required = true) String key) {
		FormModelDef formModel = formModelService.getFormModelDefByKey(key);
		return Result.OK(formModel);
	}
	
	@GetMapping("deploy/{code}")
	@Operation(summary = "发布表单", description = "发布表单", method = "GET")
	public Result<FormModel> deploy(@PathVariable(name = "code", required = true) String code){
		
		FormModel formModel = formModelService.deploy(code);
		return Result.OK("部署成功！", formModel);
	}
	
	@PostMapping("add")
	@Operation(summary = "新增表单", description = "新增表单", method = "POST")
	public Result<FormModel> add(@RequestBody FormModel formModel) {
		formModel.setStatus(BpmnEnums.DeployStatusEnums.NEW.getCode());
		formModelService.save(formModel);
		return Result.OK(formModel);
	}
	
	@PutMapping("edit")
	@Operation(summary = "编辑表单", description = "编辑表单（全量）", method = "PUT")
	public Result<FormModel> edit(@RequestBody FormModel formModel) {
		formModel.setStatus(BpmnEnums.DeployStatusEnums.UPDATE.getCode());
		formModelService.update(formModel);
		return Result.OK(formModel);
	}

}
