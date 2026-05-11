package com.mxpioframework.security.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.policy.CrudContext;
import com.mxpioframework.jpa.policy.impl.SmartCrudPolicyAdapter;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.cache.SecurityCacheEvict;
import com.mxpioframework.security.entity.DataFilter;
import com.mxpioframework.security.entity.DataResource;
import com.mxpioframework.security.entity.RoleDataFilter;
import com.mxpioframework.security.service.DataFilterService;

@Service("mxpio.security.dataFilterService")
public class DataFilterServiceImpl implements DataFilterService  {
	
	@SecurityCacheEvict
	@Override
	@Transactional(readOnly = false)
	public void save(DataFilter dataFilter) {
		JpaUtil.save(dataFilter, new SmartCrudPolicyAdapter(){
			@Override
			public void afterInsert(CrudContext context) {
				DataFilter dataFilter = context.getEntity();
				JpaUtil.linu(DataResource.class).set("hasFilter", true).equal("id", dataFilter.getDataResourceId()).update();
				super.afterInsert(context);
			}
		});
	}
	
	@SecurityCacheEvict
	@Override
	@Transactional
	public void save(List<DataFilter> dataFilters) {
		JpaUtil.save(dataFilters, new SmartCrudPolicyAdapter(){
			@Override
			public void afterInsert(CrudContext context) {
				DataFilter dataFilter = context.getEntity();
				JpaUtil.linu(DataResource.class).set("hasFilter", true).equal("id", dataFilter.getDataResourceId()).update();
				super.afterInsert(context);
			}
		});
	}
	
	@SecurityCacheEvict
	@Override
	@Transactional(readOnly = false)
	public void delete(DataFilter dataFilter) {
		JpaUtil.delete(dataFilter, new SmartCrudPolicyAdapter(){
			@Override
			public void afterDelete(CrudContext context) {
				DataFilter dataFilter = context.getEntity();
				Long count = JpaUtil.linq(DataFilter.class).equal("dataResourceId", dataFilter.getDataResourceId()).count();
				if(count == 0){
					JpaUtil.linu(DataResource.class).set("hasFilter", false).equal("id", dataFilter.getDataResourceId()).update();
				}
				JpaUtil.lind(RoleDataFilter.class).equal("dataFilterId", dataFilter.getId()).delete();
				super.afterInsert(context);
			}
		});
	}

	@Override
	@Transactional(readOnly = false)
	public DataFilter getById(String id) {
		return JpaUtil.linq(DataFilter.class).idEqual(id).findOne();
	}

	@Override
	@Transactional(readOnly = false)
	public List<DataFilter> list(Criteria criteria) {
		return JpaUtil.linq(DataFilter.class).where(criteria).list();
	}

	@Override
	@Transactional(readOnly = false)
	public List<DataFilter> getByResourceId(String resId) {
		return JpaUtil.linq(DataFilter.class).equal("dataResourceId", resId).list();
	}

}
