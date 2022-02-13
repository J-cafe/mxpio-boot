package com.mxpioframework.excel.importer.policy.impl;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.mxpioframework.common.util.BeanReflectionUtils;
import com.mxpioframework.excel.importer.model.Cell;
import com.mxpioframework.excel.importer.model.MappingRule;
import com.mxpioframework.excel.importer.model.Record;
import com.mxpioframework.excel.importer.policy.Context;
import com.mxpioframework.excel.importer.policy.ParseRecordPolicy;
import com.mxpioframework.excel.importer.processor.CellPostprocessor;
import com.mxpioframework.excel.importer.processor.CellPreprocessor;
import com.mxpioframework.excel.importer.processor.CellProcessor;
import com.mxpioframework.jpa.JpaUtil;

import net.sf.cglib.beans.BeanMap;

@Component("importer.parseRecordPolicyImpl")
public class ParseRecordPolicyImpl implements ParseRecordPolicy, ApplicationContextAware {

	private Collection<CellPreprocessor> cellPreprocessors;
	private Collection<CellProcessor> cellProcessors;
	private Collection<CellPostprocessor> cellPostprocessors;
	
	@Override
	public void apply(Context context) throws ClassNotFoundException {
		List<Record> records = context.getRecords();
		
		for (int i = context.getStartRow(); i < records.size(); i++) {
			Record record = records.get(i);
			Object entity = BeanReflectionUtils.newInstance(context.getEntityClass());
			context.setCurrentEntity(entity);
			context.setCurrentRecord(record);
			String idProperty = JpaUtil.getIdName(context.getEntityClass());
			BeanMap beanMap = BeanMap.create(context.getCurrentEntity());
			if (beanMap.getPropertyType(idProperty) == String.class) {
				beanMap.put(idProperty, UUID.randomUUID().toString());
			}
			for (MappingRule mappingRule : context.getMappingRules()) {
				Cell cell = record.getCell(mappingRule.getExcelColumn());

				context.setCurrentMappingRule(mappingRule);
				context.setCurrentCell(cell);
				cellPreprocess(context);
				cellProcess(context);
				cellPostprocess(context);
				
			}
			JpaUtil.persist(entity);
			if (i % 100 == 0) {
				JpaUtil.persistAndFlush(entity);
				JpaUtil.getEntityManager(entity).clear();
			} else {
				JpaUtil.persist(entity);
			}
		}

	}
	
	
	protected void cellPreprocess(Context context) {
		for (CellPreprocessor cellPreProcessor : cellPreprocessors) {
			if (cellPreProcessor.support(context)) {
				cellPreProcessor.process(context);
			}
		}
	}
	
	protected void cellProcess(Context context) {
		for (CellProcessor cellProcessor : cellProcessors) {
			if (cellProcessor.support(context)) {
				cellProcessor.process(context);
			}
		}
	}
	
	protected void cellPostprocess(Context context) {
		for (CellPostprocessor cellPostprocessor : cellPostprocessors) {
			if (cellPostprocessor.support(context)) {
				cellPostprocessor.process(context);
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		cellPreprocessors = applicationContext.getBeansOfType(CellPreprocessor.class).values();
		cellProcessors = applicationContext.getBeansOfType(CellProcessor.class).values();
		cellPostprocessors = applicationContext.getBeansOfType(CellPostprocessor.class).values();

	}

}
