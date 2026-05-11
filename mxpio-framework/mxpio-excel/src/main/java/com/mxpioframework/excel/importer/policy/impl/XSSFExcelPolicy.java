package com.mxpioframework.excel.importer.policy.impl;

import com.mxpioframework.common.exception.MBootException;
import com.mxpioframework.excel.importer.model.ImporterSolution;
import com.mxpioframework.excel.importer.model.MappingRule;
import com.mxpioframework.excel.importer.policy.*;
import com.mxpioframework.excel.importer.processor.AfterImportProcessor;
import com.mxpioframework.excel.importer.processor.BeforeImportProcessor;
import com.mxpioframework.jpa.JpaUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

@Component("importer.xssfExcelPolicy")
public class XSSFExcelPolicy implements ExcelPolicy<XSSFContext>, ApplicationContextAware {

	@Autowired
	private SheetPolicy<XSSFContext> sheetPolicy;

	@Autowired
	private ParseRecordPolicy parseRecordPolicy;

	private ClassLoader classLoader;

	private Collection<BeforeImportProcessor> beforeImportProcessors;

	private Collection<AfterImportProcessor> afterImportProcessors;

	@Override
	@Transactional(readOnly = false)
	public void apply(XSSFContext context) throws Exception {
        OPCPackage xlsxPackage = OPCPackage.open(context.getInpuStream());
        XSSFReader xssfReader = new XSSFReader(xlsxPackage);
        context.setStyles(xssfReader.getStylesTable());
        context.setStrings(new ReadOnlySharedStringsTable(xlsxPackage));

        initContext(context);

        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader
                .getSheetsData();
        while (iter.hasNext()) {
            InputStream stream = iter.next();
            context.setInpuStream(stream);
            String sheetName = context.getImporterSolution().getExcelSheetName();
            if (StringUtils.isNotEmpty(sheetName)) {
				if (sheetName.equals(iter.getSheetName())) {
					sheetPolicy.apply(context);
					break;
				}
			} else {
	            sheetPolicy.apply(context);
			}
            stream.close();
        }

		for(BeforeImportProcessor beforeImportProcessor:beforeImportProcessors){
			if(beforeImportProcessor.support(context)){
				boolean beforeRes = beforeImportProcessor.process(context);
				if(!beforeRes){
					throw new MBootException("导入校验失败");
				}
			}
		}

        parseRecordPolicy.apply(context);

		for(AfterImportProcessor afterImportProcessor:afterImportProcessors){
			if(afterImportProcessor.support(context)){
				afterImportProcessor.process(context);
			}
		}

	}

	protected void initContext(Context context) throws ClassNotFoundException {
		ImporterSolution importerSolution = getImporterSolution(context.getImporterSolutionCode());
		context.setStartRow(importerSolution.getStartRow()-1);
		Class<?> entityClass =  this.classLoader.loadClass(importerSolution.getEntityClassName());
		List<MappingRule> mappingRules = importerSolution.getMappingRules();
		Assert.notEmpty(mappingRules, "mappingRules can not be empty.");

		context.setImporterSolution(importerSolution);
		context.setMappingRules(mappingRules);
		context.setEntityClass(entityClass);
	}

	private ImporterSolution getImporterSolution(String importerSolutionCode) {
		ImporterSolution importerSolution = JpaUtil.linq(ImporterSolution.class).equal("code", importerSolutionCode).findOne();
		List<MappingRule> mappingRules = JpaUtil
				.linq(MappingRule.class)
				.equal("importerSolutionId", importerSolution.getId())
				.list();
		importerSolution.setMappingRules(mappingRules);
		return importerSolution;

	}

	@Override
	public boolean support(String fileName) {
		return fileName.endsWith(".xlsx");
	}

	public void setSheetPolicy(SheetPolicy<XSSFContext> sheetPolicy) {
		this.sheetPolicy = sheetPolicy;
	}

	@Override
	public XSSFContext createContext() {
		return new XSSFContext();
	}

	public void setParseRecordPolicy(ParseRecordPolicy parseRecordPolicy) {
		this.parseRecordPolicy = parseRecordPolicy;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.classLoader = applicationContext.getClassLoader();
		this.beforeImportProcessors = applicationContext.getBeansOfType(BeforeImportProcessor.class).values();
		this.afterImportProcessors = applicationContext.getBeansOfType(AfterImportProcessor.class).values();
	}

}
