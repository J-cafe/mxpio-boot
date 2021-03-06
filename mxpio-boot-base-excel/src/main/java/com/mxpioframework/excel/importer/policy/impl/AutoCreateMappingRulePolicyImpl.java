package com.mxpioframework.excel.importer.policy.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Transient;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.mxpioframework.common.util.BeanReflectionUtils;
import com.mxpioframework.excel.annotation.Excel;
import com.mxpioframework.excel.importer.model.ImporterSolution;
import com.mxpioframework.excel.importer.model.MappingRule;
import com.mxpioframework.excel.importer.policy.AutoCreateMappingRulePolicy;
import com.mxpioframework.jpa.EntityUtils;
import com.mxpioframework.jpa.JpaUtil;

import io.swagger.annotations.ApiModelProperty;

@Component("importer.autoCreateMappingRulePolicyImpl")
public class AutoCreateMappingRulePolicyImpl implements AutoCreateMappingRulePolicy {

	@Override
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
			ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
			if (excel != null) {
				mappingRule.setName(excel.value());
			}else if(apiModelProperty != null) {
				mappingRule.setName(apiModelProperty.value());
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
