package com.mxpioframework.excel.export.extension;

import com.mxpioframework.excel.export.excel.ExcelReportModelGenerater;
import org.springframework.stereotype.Component;

@Component(ReportGenerater.BEAN_ID)
public class ReportGenerater extends ExcelReportModelGenerater {

	public static final String BEAN_ID = "mxpio.ReportGenerater";

}
