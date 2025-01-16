package com.mxpioframework.excel.export.extension;

import java.io.OutputStream;

import com.mxpioframework.excel.export.model.ReportGrid;

/**
 * 自定义生成报表接口
 * 
 */
public interface ReportBuilder {

	/**
	 * 生成报表
	 *
	 * @param out 输出流
	 * @param reportGrid grid数据模型
	 * @throws Exception 如果在生成报表过程中发生错误
	 */
	public void execute(OutputStream out, ReportGrid reportGrid) throws Exception;

	/**
	 * 是否支持当前的文件类型
	 *
	 * @param extension 文件类型
	 * @return 如果支持返回true，否则返回false
	 */
	public boolean support(String extension);

}
