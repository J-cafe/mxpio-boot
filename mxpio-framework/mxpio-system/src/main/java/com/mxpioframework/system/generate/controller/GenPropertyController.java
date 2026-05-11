package com.mxpioframework.system.generate.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.system.generate.entity.GenProperty;
import com.mxpioframework.system.generate.service.GenPropertyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "GenPropertyController", description = "低代码属性接口")
@RestController("mxpio.system.GenPropertyController")
@RequestMapping("/sys/generator/prop/")
public class GenPropertyController {

	@Autowired
	private GenPropertyService genPropertySerivce;

	@GetMapping("list")
	@Operation(summary = "属性列表", description = "获取属性列表", method = "GET")
	public Result<List<GenProperty>> list(Criteria criteria) throws UnsupportedEncodingException {
		List<GenProperty> items = genPropertySerivce.list(GenProperty.class, criteria);
		return Result.OK(items);
	}
	
	@GetMapping("page")
	@Operation(summary = "属性列表", description = "获取属性列表", method = "GET")
	public Result<Page<GenProperty>> page(Criteria criteria, Integer pageSize, Integer pageNo) throws UnsupportedEncodingException {
		Pageable page = PageRequest.of(pageNo-1, pageSize);
		Page<GenProperty> items = genPropertySerivce.listPage(GenProperty.class, page, criteria);
		return Result.OK(items);
	}
	
	@GetMapping("list/{id}")
	@Operation(summary = "根据id获取属性", description = "根据id获取属性", method = "GET")
	public Result<GenProperty> getById(@PathVariable(name = "id", required = true) String id) {
		GenProperty item = genPropertySerivce.getById(GenProperty.class, id);
		return Result.OK(item);
	}

	@PostMapping("add")
	@Operation(summary = "新增属性", description = "新增属性", method = "POST")
	public Result<GenProperty> add(@RequestBody GenProperty genProperty) {
 		genPropertySerivce.save(genProperty);
		return Result.OK(genProperty);
	}
	
	@PutMapping("edit")
	@Operation(summary = "编辑属性", description = "编辑属性（全量）", method = "PUT")
	public Result<GenProperty> edit(@RequestBody GenProperty genProperty) {
		genPropertySerivce.update(genProperty);
		return Result.OK(genProperty);
	}
	
	@DeleteMapping("remove/{id}")
	@Operation(summary = "删除属性", description = "删除属性", method = "DELETE")
	public Result<GenProperty> remove(@PathVariable(name = "id", required = true) String id) {
		String[] ids = id.split(",");
		for(String key : ids){
			genPropertySerivce.delete(GenProperty.class, key);
		}
		return Result.OK();
	}
}
