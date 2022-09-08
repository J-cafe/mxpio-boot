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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@SuppressWarnings({"rawtypes","unchecked"})
@Api(value = "ImporterSolutionController", tags = { "Excel导入接口" })
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
	@ApiOperation(value = "导入方案", notes = "获取导入方案列表", httpMethod = "GET")
	public Result<Page<ImporterSolution>> loadImporterSolutions(String criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws UnsupportedEncodingException {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		
		Page<ImporterSolution> page = importerSolutionService.listPage(c, pageAble);
		return Result.OK(page);
	}
	
	@GetMapping("list/{importerSolutionId}/rules")
	@ApiOperation(value = "方案规则", notes = "获取方案规则列表", httpMethod = "GET")
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
	@ApiOperation(value = "删除规则", notes = "删除规则", httpMethod = "DELETE")
	public Result<MappingRule> removeRule(@PathVariable(name = "ruleId", required = true) String ruleId) {
		String ids[] = ruleId.split(",");
		importerSolutionService.deleteRule(ids);
		return Result.OK("删除成功！",null);
	}
	
	@PostMapping("rule/add")
	@ApiOperation(value = "新增规则", notes = "新增规则", httpMethod = "POST")
	public Result<MappingRule> addRule(@RequestBody MappingRule mappingRule) throws UnsupportedEncodingException {
		importerSolutionService.saveRule(mappingRule);
		return Result.OK(mappingRule);
	}
	
	@PutMapping("rule/edit")
	@ApiOperation(value = "更新规则", notes = "更新规则", httpMethod = "PUT")
	public Result<MappingRule> editRule(@RequestBody MappingRule mappingRule) throws UnsupportedEncodingException {
		importerSolutionService.updateRule(mappingRule);
		return Result.OK(mappingRule);
	}
	
	@GetMapping("factory/list")
	@ApiOperation(value = "数据源", notes = "获取数据源", httpMethod = "GET")
	public Result<Collection<String>> loadEntityManagerFactoryNames() {
		return Result.OK(entityManagerFactoryNames);
	}
	
	@GetMapping("factory/list/{entityManagerFactoryName}/list")
	@ApiOperation(value = "领域类", notes = "根据数据源获取领域类", httpMethod = "GET")
	public Result<Collection<String>> loadEntityClassNames(@PathVariable("entityManagerFactoryName") String entityManagerFactoryName) {
		return Result.OK(entityClassNameMap.get(entityManagerFactoryName));
	}
	
	@GetMapping("parser/pre/list")
	@ApiOperation(value = "前置处理器", notes = "获取前置处理器", httpMethod = "GET")
	public Result<Collection<Map<String, String>>> loadCellPreParsers() {
		return Result.OK(cellPreParsers);
	}
	
	@GetMapping("parser/post/list")
	@ApiOperation(value = "后置处理器", notes = "获取后置处理器", httpMethod = "GET")
	public Result<Collection<Map<String, String>>> loadCellPostParsers() {
		return Result.OK(cellPostParsers);
	}
	
	@PostMapping("/add")
	@ApiOperation(value = "添加导入方案", notes = "添加导入方案", httpMethod = "POST")
	public Result<ImporterSolution> saveImporterSolutions(@RequestBody ImporterSolution importerSolution) {
		importerSolutionService.save(importerSolution);
		return Result.OK(importerSolution);
	}
	
	@PutMapping("/edit")
	@ApiOperation(value = "修改导入方案", notes = "修改导入方案", httpMethod = "PUT")
	public Result<ImporterSolution> edit(@RequestBody ImporterSolution importerSolution) {
		importerSolutionService.update(importerSolution);
		return Result.OK(importerSolution);
	}
	
	@DeleteMapping("/remove/{id}")
	@ApiOperation(value = "删除方案", notes = "删除方案信息", httpMethod = "DELETE")
	public Result<ImporterSolution> delete(@PathVariable(name = "id", required = true) String id) throws Exception {
		String ids[] = id.split(",");
		importerSolutionService.deleteBatch(ids);
		return Result.OK("删除成功",null);
	}
	
	@PostMapping("/rule/mapping/create")
	@ApiOperation(value = "生成规则映射", notes = "生成规则映射", httpMethod = "POST")
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
	@ApiOperation(value = "导入", notes = "上传导入", httpMethod = "POST")
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
