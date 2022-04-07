package com.mxpioframework.excel.importer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.excel.importer.model.ImporterSolution;
import com.mxpioframework.excel.importer.model.MappingRule;
import com.mxpioframework.jpa.query.Criteria;

public interface ImporterSolutionService {
	
	ImporterSolution getById(String id);

	void delete(String key);

	Page<ImporterSolution> listPage(Criteria c, Pageable pageAble);

	Page<MappingRule> ruleListPage(Criteria c, String importerSolutionId, Pageable pageAble);

	ImporterSolution save(ImporterSolution importerSolution);

	ImporterSolution update(ImporterSolution importerSolution);

	void deleteRule(String ruleId);

	MappingRule saveRule(MappingRule mappingRule);

	MappingRule updateRule(MappingRule mappingRule);

}
