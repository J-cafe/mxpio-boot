package com.mxpioframework.excel;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigurationPackage
@ComponentScan
public class ExcelConfiguration {

	public final static ExcelProperties EXCEL_PROPERTIES = new ExcelProperties();
}
