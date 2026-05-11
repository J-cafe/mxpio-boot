package com.mxpioframework.excel;

import org.springframework.core.env.Environment;

public class ExcelProperties {
	
	private String pdfToSwfPath;
	private String xpdfPath;
	
	public void init(Environment environment){
		this.setPdfToSwfPath(environment.getProperty("mxpio.excel.swfviewer.pdfToSwf"));
		this.setXpdfPath(environment.getProperty("mxpio.excel.swfviewer.xpdfPath"));
	}
	
	public String getPdfToSwfPath() {
		return pdfToSwfPath;
	}
	public void setPdfToSwfPath(String pdfToSwfPath) {
		this.pdfToSwfPath = pdfToSwfPath;
	}
	public String getXpdfPath() {
		return xpdfPath;
	}
	public void setXpdfPath(String xpdfPath) {
		this.xpdfPath = xpdfPath;
	}

}
