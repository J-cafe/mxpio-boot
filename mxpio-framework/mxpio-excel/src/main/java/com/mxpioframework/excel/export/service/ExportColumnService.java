package com.mxpioframework.excel.export.service;

import java.util.List;

import com.mxpioframework.excel.export.entity.ExportColumn;
import com.mxpioframework.security.service.BaseService;

public interface ExportColumnService extends BaseService<ExportColumn> {

	/**
	 * 根据导出方案ID查询导出列列表
	 *
	 * @param solutionId 导出方案ID
	 * @return 导出列列表
	 */
	List<ExportColumn> queryBySolutionId(String solutionId);

	/**
	 * 批量删除导出列
	 *
	 * @param columnIds 导出列ID列表，多个ID之间用逗号分隔
	 * @return 删除的行数
	 */
	int deleteBatch(String columnIds);

}
