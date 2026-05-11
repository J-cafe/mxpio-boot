package com.mxpioframework.excel.export.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.excel.export.entity.ExportColumn;
import com.mxpioframework.excel.export.service.ExportColumnService;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.service.impl.BaseServiceImpl;

@Service("mxpio.excel.exportColumnService")
public class ExportColumnServiceImpl extends BaseServiceImpl<ExportColumn> implements ExportColumnService {

	@Override
	@Transactional(readOnly = true)
	public List<ExportColumn> queryBySolutionId(String solutionId) {
		return JpaUtil.linq(ExportColumn.class).equal("solutionId", solutionId).list();
	}

	@Override
	@Transactional(readOnly = false)
	public int deleteBatch(String columnIds) {
		Object[] keys = columnIds.split(",");
		return JpaUtil.lind(ExportColumn.class).in("id", keys).delete();
	}

}
