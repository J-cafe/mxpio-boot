package com.mxpioframework.excel.importer.policy.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Transient;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.common.util.BeanReflectionUtils;
import com.mxpioframework.excel.annotation.Excel;
import com.mxpioframework.excel.importer.model.ImporterSolution;
import com.mxpioframework.excel.importer.model.MappingRule;
import com.mxpioframework.excel.importer.policy.AutoCreateMappingRulePolicy;
import com.mxpioframework.jpa.EntityUtils;
import com.mxpioframework.jpa.JpaUtil;

import io.swagger.v3.oas.annotations.media.Schema;

@Component("importer.autoCreateMappingRulePolicyImpl")
public class AutoCreateMappingRulePolicyImpl implements AutoCreateMappingRulePolicy {

	@Override
	@Transactional(readOnly = false)
	public ImporterSolution apply(ImporterSolution importerSolution) {
		
		Class<?> entityClass = BeanReflectionUtils.getClassByName(importerSolution.getEntityClassName());
		List<Field> fields = BeanReflectionUtils.loadClassFields(entityClass);
		List<String> propertyNames = getPropertyNames(importerSolution);
		int col = propertyNames.size();

		for (Field field : fields) {
			Transient t = field.getAnnotation(Transient.class);
			String propertyName = field.getName();
			if (t != null || BeanUtils.getPropertyDescriptor(entityClass, propertyName) == null
					|| !EntityUtils.isSimpleType(field.getType()) || contains(propertyNames, propertyName)) {
				continue;
			}
			MappingRule mappingRule = new MappingRule();
			mappingRule.setId(UUID.randomUUID().toString());
			mappingRule.setImporterSolutionId(importerSolution.getId());
			mappingRule.setName(propertyName);
			mappingRule.setPropertyName(propertyName);
			mappingRule.setExcelColumn(col);

			Excel excel = field.getAnnotation(Excel.class);
			Column column = field.getAnnotation(Column.class);
			Schema schema = field.getAnnotation(Schema.class);
			if (excel != null) {
				mappingRule.setName(excel.value());
			}else if(schema != null) {
				mappingRule.setName(schema.description());
			}
			if (column != null) {

			}
			JpaUtil.persist(mappingRule);
			importerSolution.getMappingRules().add(mappingRule);
			col++;
		}
		return importerSolution;
	}

	private List<String> getPropertyNames(ImporterSolution importerSolution) {
		return JpaUtil.linq(MappingRule.class, String.class).select("propertyName")
				.equal("importerSolutionId", importerSolution.getId()).list();
	}

	private boolean contains(List<String> propertyNames, String propertyName) {
		for (String p : propertyNames) {
			if (p.equals(propertyName)) {
				return true;
			}
		}
		return false;
	}

}
