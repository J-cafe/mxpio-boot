package com.mxpioframework.excel.importer.processor.impl;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.Constants;
import com.mxpioframework.excel.importer.converter.TypeConverter;
import com.mxpioframework.excel.importer.exception.DataFormatException;
import com.mxpioframework.excel.importer.model.MappingRule;
import com.mxpioframework.excel.importer.parser.CellPostParser;
import com.mxpioframework.excel.importer.parser.CellPreParser;
import com.mxpioframework.excel.importer.policy.Context;
import com.mxpioframework.excel.importer.processor.CellProcessor;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanMap;

@Slf4j
@Component("importer.defaultCellProcessor")
public class DefaultCellProcessor implements CellProcessor, ApplicationContextAware {

	private Collection<TypeConverter> typeConverters;
	
	private ApplicationContext applicationContext;
	
	
	@Override
	public void process(Context context) {
		cellPreParse(context);
		cellParse(context);
		cellPostParse(context);
	}
	
	protected void cellPreParse(Context context) {
		MappingRule mappingRule = context.getCurrentMappingRule();
		String cellPreParserBean = mappingRule.getCellPreParserBean();
		CellPreParser cellPreParser = (CellPreParser) applicationContext.getBean(cellPreParserBean);
		cellPreParser.parse(context);
	}
	
	protected void cellParse(Context context) {
		MappingRule mappingRule = context.getCurrentMappingRule();
		BeanMap beanMap = BeanMap.create(context.getCurrentEntity());
		String propertyName = mappingRule.getPropertyName();
		Class<?> type = beanMap.getPropertyType(propertyName);
		Object value = context.getValue();
		value = value == null ? null : value.toString();
		for (TypeConverter typeConverter : typeConverters) {
			if (typeConverter.support(type)) {
				try {
					value = typeConverter.fromText(type, mappingRule.getMappingValueIfNeed((String) value));
				} catch (Exception e) {
					if (mappingRule.isIgnoreErrorFormatData()) {
						log.debug(e.getMessage());
						value = Constants.IGNORE_ERROR_FORMAT_DATA;
					} else {
						throw new DataFormatException(context.getCurrentCell().getRow(), context.getCurrentCell().getCol(), (String) value);
					}
				}
				break;
			}
		}
		context.setValue(value);
	}
	
	protected void cellPostParse(Context context) {
		MappingRule mappingRule = context.getCurrentMappingRule();
		String cellPostParserBean = mappingRule.getCellPostParserBean();
		CellPostParser cellPostParser = (CellPostParser) applicationContext.getBean(cellPostParserBean);
		cellPostParser.parse(context);
	}

	@Override
	public boolean support(Context context) {
		return true;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		typeConverters = applicationContext.getBeansOfType(TypeConverter.class).values();
	}

}
