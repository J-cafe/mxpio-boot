package com.mxpioframework.excel.export.service;

import java.util.List;

import com.mxpioframework.excel.export.entity.ExportColumn;
import com.mxpioframework.security.service.BaseService;

public interface ExportColumnService extends BaseService<ExportColumn> {

	List<ExportColumn> queryBySolutionId(String solutionId);

}
