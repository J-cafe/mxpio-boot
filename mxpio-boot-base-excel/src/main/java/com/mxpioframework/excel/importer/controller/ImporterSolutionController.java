package com.mxpioframework.excel.importer.controller;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Type;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.excel.importer.converter.filter.EntityManagerFactoryFilter;
import com.mxpioframework.excel.importer.converter.filter.EntityTypeFilter;
import com.mxpioframework.excel.importer.model.ImporterSolution;
import com.mxpioframework.excel.importer.model.MappingRule;
import com.mxpioframework.excel.importer.parser.CellPostParser;
import com.mxpioframework.excel.importer.parser.CellPreParser;
import com.mxpioframework.excel.importer.policy.AutoCreateMappingRulePolicy;
import com.mxpioframework.excel.importer.policy.Context;
import com.mxpioframework.excel.importer.policy.ExcelPolicy;
import com.mxpioframework.excel.importer.service.ImporterSolutionService;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.CriteriaUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@SuppressWarnings({"rawtypes","unchecked"})
@Tag(name = "ImporterSolutionController", description = "Excel导入接口")
@RestController("mxpio.excel.importerSolutionController")
@RequestMapping("/excel/import/")
public class ImporterSolutionController implements ApplicationContextAware {
	
	private Collection<String> entityManagerFactoryNames;
	private Map<String, List<String>> entityClassNameMap = new HashMap<>();
	private Collection<Map<String,String>> cellPreParsers = new ArrayList<>();
	private Collection<Map<String,String>> cellPostParsers = new ArrayList<>();
	
	@Autowired
	private AutoCreateMappingRulePolicy autoCreateMappingRulePolicy;
	
	@Autowired
	private ImporterSolutionService importerSolutionService;
	
	@Autowired
	private Collection<ExcelPolicy> excelPolicies;


	@GetMapping("list")
	@Operation(summary = "导入方案", description = "获取导入方案列表", method = "GET")
	public Result<Page<ImporterSolution>> loadImporterSolutions(String criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws UnsupportedEncodingException {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		
		Page<ImporterSolution> page = importerSolutionService.listPage(c, pageAble);
		return Result.OK(page);
	}
	
	@GetMapping("list/{importerSolutionId}/rules")
	@Operation(summary = "方案规则", description = "获取方案规则列表", method = "GET")
	public Result<Page<MappingRule>> loadMappingRules(String criteria, 
			@PathVariable(value="importerSolutionId") String importerSolutionId,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws UnsupportedEncodingException {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		
		Page<MappingRule> page = importerSolutionService.ruleListPage(c, importerSolutionId, pageAble);
		return Result.OK(page);
	}
	
	@DeleteMapping("rule/remove/{ruleId}")
	@Operation(summary = "删除规则", description = "删除规则", method = "DELETE")
	public Result<MappingRule> removeRule(@PathVariable(name = "ruleId", required = true) String ruleId) {
		String ids[] = ruleId.split(",");
		importerSolutionService.deleteRule(ids);
		return Result.OK("删除成功！",null);
	}
	
	@PostMapping("rule/add")
	@Operation(summary = "新增规则", description = "新增规则", method = "POST")
	public Result<MappingRule> addRule(@RequestBody MappingRule mappingRule) throws UnsupportedEncodingException {
		importerSolutionService.saveRule(mappingRule);
		return Result.OK(mappingRule);
	}
	
	@PutMapping("rule/edit")
	@Operation(summary = "更新规则", description = "更新规则", method = "PUT")
	public Result<MappingRule> editRule(@RequestBody MappingRule mappingRule) throws UnsupportedEncodingException {
		importerSolutionService.updateRule(mappingRule);
		return Result.OK(mappingRule);
	}
	
	@GetMapping("factory/list")
	@Operation(summary = "数据源", description = "获取数据源", method = "GET")
	public Result<Collection<String>> loadEntityManagerFactoryNames() {
		return Result.OK(entityManagerFactoryNames);
	}
	
	@GetMapping("factory/list/{entityManagerFactoryName}/list")
	@Operation(summary = "领域类", description = "根据数据源获取领域类", method = "GET")
	public Result<Collection<String>> loadEntityClassNames(@PathVariable("entityManagerFactoryName") String entityManagerFactoryName) {
		return Result.OK(entityClassNameMap.get(entityManagerFactoryName));
	}
	
	@GetMapping("parser/pre/list")
	@Operation(summary = "前置处理器", description = "获取前置处理器", method = "GET")
	public Result<Collection<Map<String, String>>> loadCellPreParsers() {
		return Result.OK(cellPreParsers);
	}
	
	@GetMapping("parser/post/list")
	@Operation(summary = "后置处理器", description = "获取后置处理器", method = "GET")
	public Result<Collection<Map<String, String>>> loadCellPostParsers() {
		return Result.OK(cellPostParsers);
	}
	
	@PostMapping("/add")
	@Operation(summary = "添加导入方案", description = "添加导入方案", method = "POST")
	public Result<ImporterSolution> saveImporterSolutions(@RequestBody ImporterSolution importerSolution) {
		importerSolutionService.save(importerSolution);
		return Result.OK(importerSolution);
	}
	
	@PutMapping("/edit")
	@Operation(summary = "修改导入方案", description = "修改导入方案", method = "PUT")
	public Result<ImporterSolution> edit(@RequestBody ImporterSolution importerSolution) {
		importerSolutionService.update(importerSolution);
		return Result.OK(importerSolution);
	}
	
	@DeleteMapping("/remove/{id}")
	@Operation(summary = "删除方案", description = "删除方案信息", method = "DELETE")
	public Result<ImporterSolution> delete(@PathVariable(name = "id", required = true) String id) throws Exception {
		String ids[] = id.split(",");
		importerSolutionService.deleteBatch(ids);
		return Result.OK("删除成功",null);
	}
	
	@PostMapping("/rule/mapping/create")
	@Operation(summary = "生成规则映射", description = "生成规则映射", method = "POST")
	public Result<ImporterSolution> autoCreateMappingRules(@RequestBody ImporterSolution importerSolution) {
		return Result.OK(autoCreateMappingRulePolicy.apply(importerSolution));
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		Map<String, EntityManagerFactory> entityManagerFactoryMap = 
				new HashMap<String, EntityManagerFactory>(applicationContext.getBeansOfType(EntityManagerFactory.class));
				
		Collection<EntityManagerFactoryFilter> sessionFactoryFilters = applicationContext
				.getBeansOfType(EntityManagerFactoryFilter.class).values();
		
		for (EntityManagerFactoryFilter sessionFactoryFilter : sessionFactoryFilters) {
			sessionFactoryFilter.filter(entityManagerFactoryMap);
		}
		
		Collection<EntityTypeFilter> entityTypeFilters = applicationContext
				.getBeansOfType(EntityTypeFilter.class).values();
		
		for (Entry<String, EntityManagerFactory> entry : entityManagerFactoryMap.entrySet()) {
			List<EntityType<?>> entityTypes = new ArrayList<EntityType<?>>(entry.getValue().getMetamodel().getEntities());
			for (EntityTypeFilter entityTypeFilter : entityTypeFilters) {
				entityTypeFilter.filter(entityTypes);
			}
			List<String> entityClassNames = new ArrayList<String>();
			for (EntityType<?> entityType : entityTypes) {
				if (entityType instanceof Type) {
					entityClassNames.add(entityType.getJavaType().getName());
				}
			}
			entityClassNameMap.put(entry.getKey(), entityClassNames);
		}
		entityManagerFactoryNames = new ArrayList<String>(entityManagerFactoryMap.keySet());
		
		Map<String, CellPreParser> cellPreParserMap = applicationContext.getBeansOfType(CellPreParser.class);
		for (Entry<String, CellPreParser> entry : cellPreParserMap.entrySet()) {
			Map<String, String> map = new HashMap<>();
			map.put("beanId", entry.getKey());
			map.put("parserName", entry.getValue().getName());
			cellPreParsers.add(map);
		}
		
		Map<String, CellPostParser> cellPostParserMap = applicationContext.getBeansOfType(CellPostParser.class);
		for (Entry<String, CellPostParser> entry : cellPostParserMap.entrySet()) {
			Map<String, String> map = new HashMap<>();
			map.put("beanId", entry.getKey());
			map.put("parserName", entry.getValue().getName());
			cellPostParsers.add(map);
		}
		
	}
	
	@PostMapping("upload/{importerSolutionCode}")
	@Operation(summary = "导入", description = "上传导入", method = "POST")
	public Result<Object> upload(@RequestParam("file") MultipartFile multipartFile,
			@PathVariable("importerSolutionCode") String importerSolutionCode) throws Exception {
		String name = multipartFile.getOriginalFilename();
		Assert.hasLength(importerSolutionCode, "Excel导入方案编码必须配置。");
		
		InputStream inpuStream = multipartFile.getInputStream();
		int count = 0;
		try {			
			for (ExcelPolicy excelPolicy : excelPolicies) {
				if (excelPolicy.support(name)) {
					Context context = excelPolicy.createContext();
					context.setInpuStream(inpuStream);
					// context.setStartRow(startRow);
					context.setFileName(name);
					context.setFileSize(multipartFile.getSize());
					context.setImporterSolutionCode(importerSolutionCode);
					// context.setParams(parameter);
					excelPolicy.apply(context);
					count = context.getRecords().size() - context.getStartRow();
					break;
				}
			}
		}catch (Exception e) {
			return Result.error("导入失败");
		} finally {
			IOUtils.closeQuietly(inpuStream);
		}
		return Result.OK("成功导入" + count + "条数据");
	}
	
}
