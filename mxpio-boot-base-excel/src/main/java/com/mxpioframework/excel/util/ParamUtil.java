package com.mxpioframework.excel.util;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mxpioframework.security.service.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.CriteriaUtils;
import com.mxpioframework.jpa.query.Criterion;
import com.mxpioframework.jpa.query.Junction;
import com.mxpioframework.jpa.query.JunctionType;
import com.mxpioframework.jpa.query.Operator;
import com.mxpioframework.security.access.datascope.provider.DataScapeProvider;
import com.mxpioframework.security.access.provider.CriteriaFilterPreProcessor;
import com.mxpioframework.security.decision.manager.SecurityDecisionManager;
import com.mxpioframework.security.entity.DataFilter;
import com.mxpioframework.security.entity.DataResource;
import com.mxpioframework.security.entity.RoleGrantedAuthority;
import com.mxpioframework.security.util.ApplicationContextProvider;
import com.mxpioframework.security.util.SecurityUtils;

public class ParamUtil {
	public static Object getParamValue(Parameter methodParam, String[] values, String path, String urlKey){
		Object value = null;
		if("criteria".equals(methodParam.getName())){
			boolean decide = false;
			Criteria c = CriteriaUtils.json2Criteria(values[0]);
			RbacCacheService rbacCacheService = ApplicationContextProvider.getBean(RbacCacheService.class);
			SecurityDecisionManager securityDecisionManager = ApplicationContextProvider.getBean(SecurityDecisionManager.class);
			List<DataResource> datas = rbacCacheService.findAllDataResource();
			
			Map<String, DataResource> dataResourceMap = new HashMap<>();
			for (DataResource obj : datas) {
				String key = obj.getKey();
				dataResourceMap.put(key, obj);
			}
			
			DataResource dataResource = dataResourceMap.get(urlKey+"_"+path);
			
			if(dataResource != null){
				decide = true;
			}
			
			if (securityDecisionManager.decide(dataResource)) {
				if (dataResource != null && dataResource.isHasFilter()) {
					Map<String, CriteriaFilterPreProcessor> criteriaFilterPreProcessors = ApplicationContextProvider.getBeansOfType(CriteriaFilterPreProcessor.class);
					Map<String, DataScapeProvider> dataScapeProviderMap = ApplicationContextProvider.getApplicationContextSpring().getBeansOfType(DataScapeProvider.class);
					GrantedAuthorityService grantedAuthorityService = ApplicationContextProvider.getApplicationContextSpring().getBean(GrantedAuthorityService.class);
					Map<String, List<DataFilter>> roleDataFilterMap = rbacCacheService.findAllDataFilter();
					Collection<? extends GrantedAuthority> roleGrantedAuthorities = grantedAuthorityService.getGrantedAuthorities(SecurityUtils.getLoginUser());
					List<DataFilter> dataFilters = new ArrayList<>();
					for(GrantedAuthority grantedAuthority : roleGrantedAuthorities){
						if(grantedAuthority instanceof RoleGrantedAuthority){
							List<DataFilter> filters = roleDataFilterMap.get(((RoleGrantedAuthority) grantedAuthority).getRoleId());
							if(filters != null){
								dataFilters.addAll(filters);
							}
						}
					}
					if(!dataFilters.isEmpty()){
						Junction juntion = new Junction(JunctionType.OR);
						for(DataFilter dataFilter : dataFilters){
							if(!dataResource.getId().equals(dataFilter.getDataResourceId())){
								continue;
							}
							boolean filterAble = true;
							if(criteriaFilterPreProcessors != null && dataFilter.getPreProcess() != null){
								CriteriaFilterPreProcessor processor = criteriaFilterPreProcessors.get(dataFilter.getPreProcess());
								if(processor != null){
									filterAble = processor.process();
								}
							}
							
							if(filterAble){
								if (com.mxpioframework.security.Constants.DatascopeEnum.DEPT.getCode()
										.equals(dataFilter.getDataScope())) {
									DeptService deptService = ApplicationContextProvider.getBean(DeptService.class);
									Set<String> deptCodes = deptService.getDeptKeysByUser(SecurityUtils.getLoginUsername(), "code");
									juntion.addCriterion("createDept", Operator.IN, deptCodes);
								} else if (com.mxpioframework.security.Constants.DatascopeEnum.USER.getCode()
										.equals(dataFilter.getDataScope())) {
									juntion.addCriterion("createBy", Operator.EQ, SecurityUtils.getLoginUsername());
								} else if (com.mxpioframework.security.Constants.DatascopeEnum.DEPT_AND_CHILD.getCode()
										.equals(dataFilter.getDataScope())) {
									DeptService deptService = ApplicationContextProvider.getBean(DeptService.class);
									Set<String> deptCodes = deptService.getDeptKeysByUser(SecurityUtils.getLoginUsername(), "code");
									if(!deptCodes.isEmpty()){
										juntion.addCriterion("createDept", Operator.LIKE_START, deptCodes.toArray()[0]);
									}else{
										juntion.addCriterion("createDept", Operator.EQ, "");
									}
									
								} else if (com.mxpioframework.security.Constants.DatascopeEnum.SERVICE.getCode()
										.equals(dataFilter.getDataScope()) && dataScapeProviderMap != null) {
									for (Entry<String, DataScapeProvider> entry : dataScapeProviderMap.entrySet()) {
										if (entry.getKey().equals(dataFilter.getService())) {
											List<Criterion> criterions = entry.getValue().provide();
											Junction subJunction = new Junction(JunctionType.AND);
											for (Criterion criterion : criterions) {
												subJunction.addCriterion(criterion);
											}
											juntion.add(subJunction);
											break;
										}
									}
								}
							}
						}
						c.addCriterion(juntion);
					}
				}
				/*if(dataResource.getDataScope() != null){
					Map<String, CriteriaFilterPreProcessor> criteriaFilterPreProcessors = ApplicationContextProvider.getBeansOfType(CriteriaFilterPreProcessor.class);
					Map<String, DataScapeProvider> dataScapeProviderMap = ApplicationContextProvider.getApplicationContextSpring().getBeansOfType(DataScapeProvider.class);
					boolean filterAble = true;
					if(criteriaFilterPreProcessors != null && dataResource.getPreProcess() != null){
						CriteriaFilterPreProcessor processor = criteriaFilterPreProcessors.get(dataResource.getPreProcess());
						if(processor != null){
							filterAble = processor.process();
						}
					}
					
					if(filterAble){
						if(com.mxpioframework.security.Constants.DatascopeEnum.DEPT.getCode().equals(dataResource.getDataScope())){
							DeptService deptService = ApplicationContextProvider.getBean(DeptService.class);
							Set<String> deptCodes = deptService.getDeptKeysByUser(SecurityUtils.getLoginUsername(),"code");
							c.addCriterion("createDept", Operator.IN, deptCodes);
						}else if(com.mxpioframework.security.Constants.DatascopeEnum.USER.getCode().equals(dataResource.getDataScope())) {
							c.addCriterion("createBy", Operator.EQ, SecurityUtils.getLoginUsername());
						}else if(com.mxpioframework.security.Constants.DatascopeEnum.DEPT_AND_CHILD.getCode().equals(dataResource.getDataScope())){
							DeptService deptService = ApplicationContextProvider.getBean(DeptService.class);
							Set<String> deptCodes = deptService.getDeptKeysByUser(SecurityUtils.getLoginUsername(), "code");
							if(deptCodes.size()>0){
								c.addCriterion("createDept", Operator.LIKE_START, deptCodes.toArray()[0]);
							}else{
								c.addCriterion("createDept", Operator.EQ, "");
							}
						}else if(com.mxpioframework.security.Constants.DatascopeEnum.SERVICE.getCode().equals(dataResource.getDataScope())&&dataScapeProviderMap!=null){
							for(Entry<String, DataScapeProvider> entry : dataScapeProviderMap.entrySet()){
								if(entry.getKey().equals(dataResource.getService())){
									List<Criterion> criterions = entry.getValue().provide();
									for(Criterion criterion : criterions){
										c.addCriterion(criterion);
									}
									break;
								}
							}
						}
					}
				}*/
				decide = true;
			}
			if(!decide){
				c.addCriterion("createTime", Operator.EQ, new Date());
			}
			value = c;
		}else if(Integer.class.equals(methodParam.getType())){
			value = Integer.parseInt(values[0]);
		}else{
			value = values[0];
		}
		return value;
	}
}
