package com.mxpioframework.excel.export.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.excel.export.entity.ExportSolution;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.service.BaseService;

public interface ExportSolutionService extends BaseService<ExportSolution> {

	Page<ExportSolution> queryByPage(Criteria criteria, Pageable pageAble);

	ExportSolution getById(String id);

	ExportSolution getByCode(String solutionCode);

	boolean createColumnsByCode(String solutionCode);

	int deleteBatch(String solutionIds);

}
