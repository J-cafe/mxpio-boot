package com.mxpioframework.excel.export.service.impl;

import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.excel.export.entity.ExportColumn;
import com.mxpioframework.excel.export.entity.ExportSolution;
import com.mxpioframework.excel.export.service.ExportSolutionService;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.service.DataResourceService;
import com.mxpioframework.security.service.impl.BaseServiceImpl;
import com.mxpioframework.security.vo.DataVo;

@Service
public class ExportSolutionServiceImpl extends BaseServiceImpl<ExportSolution> implements ExportSolutionService {
	
	@Autowired
	public DataResourceService dataResourceService;

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
			exportSolution.setColumns(JpaUtil.linq(ExportColumn.class).equal("solutionId", exportSolution.getId()).asc("sort").list());
		}
		return exportSolution;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean createColumnsByCode(String solutionCode) {
		ExportSolution exportSolution = getByCode(solutionCode);
		List<DataVo> dataResourceVos = dataResourceService.findAllApi(true, exportSolution.getApi());
		DataVo dataResource = null;
		if(CollectionUtils.isNotEmpty(dataResourceVos)){
			dataResource = dataResourceVos.get(0);
		}
		if(dataResource != null){
			Class<?> returnType = dataResource.getMethod().getReturnType();
			if(returnType == null){
				return false;
			}else if(returnType.equals(Result.class)){
			}
		}
		
		return true;
	}

	@Override
	@Transactional(readOnly = false)
	public int deleteBatch(String solutionIds) {
		Object[] keys = solutionIds.split(",");
		JpaUtil.lind(ExportColumn.class).in("solutionId", keys).delete();
		return JpaUtil.lind(ExportSolution.class).in("id", keys).delete();
	}

}
