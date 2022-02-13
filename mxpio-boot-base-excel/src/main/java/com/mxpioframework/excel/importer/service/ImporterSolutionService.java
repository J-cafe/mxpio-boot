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

	void save(ImporterSolution importerSolution);

	void update(ImporterSolution importerSolution);

}
