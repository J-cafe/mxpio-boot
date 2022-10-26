package com.mxpioframework.excel.export.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.excel.export.entity.ExportColumn;
import com.mxpioframework.excel.export.entity.ExportSolution;
import com.mxpioframework.excel.export.service.ExportSolutionService;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.service.impl.BaseServiceImpl;

@Service
public class ExportSolutionServiceImpl extends BaseServiceImpl<ExportSolution> implements ExportSolutionService {

	@Override
	@Transactional(readOnly = true)
	public Page<ExportSolution> queryByPage(Criteria criteria, Pageable pageAble) {
		return JpaUtil.linq(ExportSolution.class).where(criteria).paging(pageAble);
	}

	@Override
	@Transactional(readOnly = true)
	public ExportSolution getById(String id) {
		ExportSolution exportSolution = JpaUtil.getOne(ExportSolution.class, id);
		if(exportSolution != null){
			exportSolution.setColumns(JpaUtil.linq(ExportColumn.class).equal("solutionId", id).list());
		}
		return exportSolution;
	}

	@Override
	@Transactional(readOnly = true)
	public ExportSolution getByCode(String solutionCode) {
		ExportSolution exportSolution = JpaUtil.linq(ExportSolution.class).equal("code", solutionCode).findOne();
		if(exportSolution != null){
			exportSolution.setColumns(JpaUtil.linq(ExportColumn.class).equal("solutionId", exportSolution.getId()).list());
		}
		return exportSolution;
	}

}
