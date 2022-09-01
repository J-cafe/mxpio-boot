package com.mxpioframework.excel.importer.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.excel.importer.model.Entry;
import com.mxpioframework.excel.importer.model.ImporterSolution;
import com.mxpioframework.excel.importer.model.MappingRule;
import com.mxpioframework.excel.importer.service.ImporterSolutionService;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.policy.CrudContext;
import com.mxpioframework.jpa.policy.impl.SmartCrudPolicy;
import com.mxpioframework.jpa.query.Criteria;

@Service
public class ImporterSolutionServiceImpl implements ImporterSolutionService {

	@Override
	@Transactional(readOnly = false)
	public void delete(String key) {
		ImporterSolution importerSolution = getById(key);
		JpaUtil.remove(importerSolution);		
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteBatch(String... ids) {
		for(String key : ids) {
			ImporterSolution importerSolution = getById(key);
			JpaUtil.remove(importerSolution);	
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ImporterSolution> listPage(Criteria c, Pageable pageAble) {
		return JpaUtil.linq(ImporterSolution.class)
			.where(c)
			.paging(pageAble);
	}

	@Override
	public Page<MappingRule> ruleListPage(Criteria c, String importerSolutionId, Pageable pageAble) {
		Page<MappingRule> page = JpaUtil.linq(MappingRule.class)
				.where(c)
				.equal("importerSolutionId", importerSolutionId)
				.asc("excelColumn")
				.paging(pageAble);
		return page;
	}

	@Override
	@Transactional(readOnly = false)
	public ImporterSolution save(ImporterSolution importerSolution) {
		
		JpaUtil.save(importerSolution, new SmartCrudPolicy() {
			@Override
			public void apply(CrudContext context) {
				if (context.getEntity() instanceof com.mxpioframework.excel.importer.model.Entry) {
					MappingRule mappingRule = context.getParent();
					com.mxpioframework.excel.importer.model.Entry entry = context.getEntity();
					entry.setMappingRuleId(mappingRule.getId());
				}
				super.apply(context);
			}
		});
		
		return importerSolution;
	}

	@Override
	@Transactional(readOnly = false)
	public ImporterSolution update(ImporterSolution importerSolution) {
		
		JpaUtil.update(importerSolution, new SmartCrudPolicy() {
			@Override
			public void apply(CrudContext context) {
				if (context.getEntity() instanceof com.mxpioframework.excel.importer.model.Entry) {
					MappingRule mappingRule = context.getParent();
					com.mxpioframework.excel.importer.model.Entry entry = context.getEntity();
					entry.setMappingRuleId(mappingRule.getId());
				}
				super.apply(context);
			}
		});
		
		return importerSolution;
	}

	@Override
	public ImporterSolution getById(String id) {
		return JpaUtil.linq(ImporterSolution.class).idEqual(id).findOne();
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteRule(String... ids) {
		for(String key : ids) {
			MappingRule rule = JpaUtil.linq(MappingRule.class).idEqual(key).findOne();
			JpaUtil.remove(rule);
		}
		
	}

	@Override
	@Transactional(readOnly = false)
	public MappingRule saveRule(MappingRule mappingRule) {
		JpaUtil.save(mappingRule, new SmartCrudPolicy() {
			@Override
			public void apply(CrudContext context) {
				if (context.getEntity() instanceof Entry) {
					MappingRule mappingRule = context.getParent();
					Entry entry = context.getEntity();
					entry.setMappingRuleId(mappingRule.getId());
				}
				super.apply(context);
			}
		});
		return mappingRule;
	}

	@Override
	@Transactional(readOnly = false)
	public MappingRule updateRule(MappingRule mappingRule) {
		// JpaUtil.lind(Entry.class).equal("mappingRuleId", mappingRule.getId()).delete();
		// JpaUtil.update(mappingRule);
		JpaUtil.update(mappingRule, new SmartCrudPolicy() {
			@Override
			public void apply(CrudContext context) {
				if (context.getEntity() instanceof Entry) {
					MappingRule mappingRule = context.getParent();
					Entry entry = context.getEntity();
					if(entry.getMappingRuleId() == null){
						entry.setMappingRuleId(mappingRule.getId());
					}
				}
				super.apply(context);
			}
		});
		return mappingRule;
	}

}
