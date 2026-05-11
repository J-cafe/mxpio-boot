package com.mxpioframework.excel.export.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.excel.export.entity.ExportSolution;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.service.BaseService;

public interface ExportSolutionService extends BaseService<ExportSolution> {

	/**
	 * 根据分页参数和查询条件分页查询导出方案列表
	 *
	 * @param criteria 查询条件
	 * @param pageAble 分页参数
	 * @return 分页查询结果
	 */
	Page<ExportSolution> queryByPage(Criteria criteria, Pageable pageAble);

	/**
	 * 根据ID查询导出方案
	 *
	 * @param id 导出方案ID
	 * @return 导出方案对象
	 */
	ExportSolution getById(String id);

	/**
	 * 根据方案编码查询导出方案
	 *
	 * @param solutionCode 方案编码
	 * @return 导出方案对象
	 */
	ExportSolution getByCode(String solutionCode);

	/**
	 * 根据方案编码创建导出列
	 *
	 * @param solutionCode 方案编码
	 * @return 如果创建成功返回true，否则返回false
	 */
	boolean createColumnsByCode(String solutionCode);

	/**
	 * 批量删除导出方案
	 *
	 * @param solutionIds 导出方案ID列表，多个ID之间用逗号分隔
	 * @return 删除的行数
	 */
	int deleteBatch(String solutionIds);

	/**
	 * 根据查询条件查询导出方案列表
	 *
	 * @param criteria 查询条件
	 * @return 导出方案列表
	 */
	List<ExportSolution> list(Criteria criteria);

}
