package com.mxpioframework.system.generate.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

import com.mxpioframework.common.util.FolderToZipUtil;
import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.Operator;
import com.mxpioframework.system.generate.entity.GenModel;
import com.mxpioframework.system.generate.entity.GenProperty;
import com.mxpioframework.system.generate.service.GenModelService;
import com.mxpioframework.system.generate.service.GenPropertyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "GenModelController", description = "低代码模型接口")
@RestController("mxpio.system.GenModelController")
@RequestMapping("/sys/generator/model/")
public class GenModelController {

	@Autowired
	private GenModelService genModelSerivce;

	@Autowired
	private GenPropertyService genPropertySerivce;

	@GetMapping("list")
	@Operation(summary = "模型列表", description = "获取模型列表", method = "GET")
	public Result<List<GenModel>> list(Criteria criteria) throws UnsupportedEncodingException {
		List<GenModel> items = genModelSerivce.list(GenModel.class, criteria);
		return Result.OK(items);
	}

	@GetMapping("page")
	@Operation(summary = "模型列表", description = "获取模型列表", method = "GET")
	public Result<Page<GenModel>> page(Criteria criteria, Integer pageSize, Integer pageNo) throws UnsupportedEncodingException {
		Pageable page = PageRequest.of(pageNo-1, pageSize);
		Page<GenModel> items = genModelSerivce.listPage(GenModel.class, page, criteria);
		return Result.OK(items);
	}

	@GetMapping("list/{id}")
	@Operation(summary = "根据id获取模型", description = "根据id获取模型", method = "GET")
	public Result<GenModel> getById(@PathVariable(name = "id", required = true) String id) {
		GenModel item = genModelSerivce.getById(GenModel.class, id);
		Criteria criteria = Criteria.create();
		criteria.addCriterion("modelId", Operator.EQ, item.getId());
		item.setGenProperties(genPropertySerivce.list(GenProperty.class, criteria));
		return Result.OK(item);
	}

	@PostMapping("add")
	@Operation(summary = "新增模型", description = "新增模型", method = "POST")
	public Result<GenModel> add(@RequestBody GenModel genModel) {
 		genModelSerivce.save(genModel);
		return Result.OK(genModel);
	}

	@PutMapping("edit")
	@Operation(summary = "编辑模型", description = "编辑模型（全量）", method = "PUT")
	public Result<GenModel> edit(@RequestBody GenModel genModel) {
		genModelSerivce.update(genModel);
		return Result.OK(genModel);
	}

	@DeleteMapping("remove/{id}")
	@Operation(summary = "删除模型", description = "删除模型", method = "DELETE")
	public Result<GenModel> remove(@PathVariable(name = "id", required = true) String id) {
		String ids[] = id.split(",");
		for(String key : ids){
			genModelSerivce.delete(GenModel.class, key);
		}
		return Result.OK();
	}

	@GetMapping("generate/{modelCode}")
	@Operation(summary = "生成代码", description = "生成代码", method = "GET")
	public void generateReportFile(@PathVariable("modelCode") String modelCode,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		String path = genModelSerivce.generateFilesByModelCode(modelCode);
        try {
            FolderToZipUtil.zip(path,response);
        } catch (Exception e) {
           e.printStackTrace();
        }
	}
}
