package com.mxpioframework.excel.importer.policy.impl;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.mxpioframework.excel.importer.handler.XSSFSheetHandler;
import com.mxpioframework.excel.importer.policy.SheetPolicy;
import com.mxpioframework.excel.importer.policy.XSSFContext;

@Component("importer.xssfSheetPolicy")
public class XSSFSheetPolicy implements SheetPolicy<XSSFContext>, ApplicationContextAware{
	
	private ApplicationContext applicationContext;

	@Override
	public void apply(XSSFContext context) throws Exception {
		InputSource sheetSource = new InputSource(context.getInpuStream());
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader sheetParser = saxParser.getXMLReader();
        XSSFSheetHandler sheetHandler = getSheetHandler();
        sheetHandler.setContext(context);
        sheetParser.setContentHandler(sheetHandler);
        sheetParser.parse(sheetSource);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public XSSFSheetHandler getSheetHandler() {
		return applicationContext.getBean(XSSFSheetHandler.class);
	}

}
