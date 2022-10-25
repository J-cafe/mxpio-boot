package com.mxpioframework.excel.export.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.excel.export.entity.ExportColumn;
import com.mxpioframework.excel.export.entity.ExportSolution;
import com.mxpioframework.excel.export.service.ExportColumnService;
import com.mxpioframework.excel.export.service.ExportSolutionService;
import com.mxpioframework.jpa.query.Criteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "ExportSolutionController", description = "导出管理")
@RestController(ExportSolutionController.BEAN_ID)
@RequestMapping("/excel/export/solution/")
public class ExportSolutionController {
	
	public static final String BEAN_ID = "mxpio.ExportSolutionController";
	
	@Autowired
	private ExportSolutionService exportSolutionService;
	
	@Autowired
	private ExportColumnService exportColumnService;
	
	@GetMapping("page")
	@Operation(summary = "导出方案列表", description = "获取导出方案列表", method = "GET")
	public Result<Page<ExportSolution>> page(Criteria criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Page<ExportSolution> page = exportSolutionService.queryByPage(criteria, pageAble);
		return Result.OK(page);
	}
	
	@GetMapping("columns/{solutionId}")
	@Operation(summary = "导出字段列表", description = "获取导出字段列表", method = "GET")
	public Result<List<ExportColumn>> columns(@PathVariable("solutionId") String solutionId) throws Exception {
		return Result.OK(exportColumnService.queryBySolutionId(solutionId));
	}
	
	@PostMapping("column/add")
	@Operation(summary = "添加导出字段", description = "添加导出字段", method = "POST")
	public Result<ExportColumn> addColumn(@RequestBody ExportColumn exportColumn) throws Exception {
		exportColumnService.save(exportColumn);
		return Result.OK("添加导出字段",exportColumn);
	}
	
	@PutMapping("column/edit")
	@Operation(summary = "更新导出字段", description = "更新导出字段", method = "PUT")
	public Result<ExportColumn> editColumn(@RequestBody ExportColumn exportColumn) throws Exception {
		exportColumnService.update(exportColumn);
		return Result.OK("更新导出字段",exportColumn);
	}
	
	@PostMapping("add")
	@Operation(summary = "添加导出方案", description = "添加导出方案", method = "POST")
	public Result<ExportSolution> add(@RequestBody ExportSolution exportSolution) throws Exception {
		exportSolutionService.save(exportSolution);
		return Result.OK("添加导出方案",exportSolution);
	}
	
	@PutMapping("edit")
	@Operation(summary = "更新导出方案", description = "更新导出方案", method = "PUT")
	public Result<ExportSolution> edit(@RequestBody ExportSolution exportSolution) throws Exception {
		exportSolutionService.update(exportSolution);
		return Result.OK("更新导出方案",exportSolution);
	}
	
	@DeleteMapping("remove/{solutionIds}")
	@Operation(summary = "删除导出方案", description = "删除导出方案", method = "DELETE")
	public Result<List<ExportSolution>> remove(@PathVariable(name = "solutionIds", required = true) String solutionIds) throws Exception {
		String[] solutionId = solutionIds.split(",");
		for(String key : solutionId){
			exportSolutionService.delete(key, ExportSolution.class);
		}
		
		return Result.OK("删除导出方案成功",null);
	}

}
