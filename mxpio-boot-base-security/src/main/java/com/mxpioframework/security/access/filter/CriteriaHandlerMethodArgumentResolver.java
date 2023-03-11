package com.mxpioframework.security.access.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.CriteriaUtils;
import com.mxpioframework.jpa.query.Criterion;
import com.mxpioframework.jpa.query.Operator;
import com.mxpioframework.security.access.datascope.provider.DataScapeProvider;
import com.mxpioframework.security.access.provider.CriteriaFilterPreProcessor;
import com.mxpioframework.security.entity.DataFilter;
import com.mxpioframework.security.entity.DataResource;
import com.mxpioframework.security.entity.RoleGrantedAuthority;
import com.mxpioframework.security.service.DataResourceService;
import com.mxpioframework.security.service.DeptService;
import com.mxpioframework.security.service.GrantedAuthorityService;
import com.mxpioframework.security.service.RoleDataFilterService;
import com.mxpioframework.security.util.SecurityUtils;

@Component
public class CriteriaHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private DataResourceService dataResourceService;
	
	@Autowired
	private RoleDataFilterService roleDataFilterService;
	
	@Autowired
	private GrantedAuthorityService grantedAuthorityService;
	
	@Autowired
	private DeptService deptService;

	@Autowired(required = false)
	private Map<String, DataScapeProvider> dataScapeProviderMap;
	
	@Autowired(required = false)
	private Map<String, CriteriaFilterPreProcessor> criteriaFilterPreProcessors;

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request,
			WebDataBinderFactory webDataBinderFactory) throws Exception {
		String criteria = request.getParameter("criteria");
		String urlKey = request.getParameter("_key");
		Criteria c = CriteriaUtils.json2Criteria(criteria);

		RequestMapping requestMapping = parameter.getMethodAnnotation(RequestMapping.class);
		RequestMapping classRequestMapping = parameter.getContainingClass().getDeclaredAnnotation(RequestMapping.class);

		if (requestMapping != null) {
			List<DataResource> datas = dataResourceService.findAll();
			
			Map<String, DataResource> dataResourceMap = new HashMap<>();
			for (DataResource obj : datas) {
				String key = obj.getKey();
				dataResourceMap.put(key, obj);
			}
			
			DataResource dataResource = dataResourceMap.get(urlKey+"_"+classRequestMapping.value()[0] + requestMapping.value()[0]);
			
			if (dataResource != null && dataResource.isHasFilter()) {
				Map<String, List<DataFilter>> roleDataFilterMap = roleDataFilterService.findAll();
				Collection<? extends GrantedAuthority> roleGrantedAuthorities = grantedAuthorityService.getGrantedAuthorities(SecurityUtils.getLoginUser());
				List<DataFilter> dataFilters = new ArrayList<>();
				for(GrantedAuthority grantedAuthority : roleGrantedAuthorities){
					if(grantedAuthority instanceof RoleGrantedAuthority){
						dataFilters.addAll(roleDataFilterMap.get(((RoleGrantedAuthority) grantedAuthority).getRoleId()));
					}
				}
				
				Criteria filterCriteria = Criteria.create().or();
				for(DataFilter dataFilter : dataFilters){
					boolean filterAble = true;
					if(criteriaFilterPreProcessors != null && dataFilter.getPreProcess() != null){
						CriteriaFilterPreProcessor processor = criteriaFilterPreProcessors.get(dataFilter.getPreProcess());
						if(processor != null){
							filterAble = processor.process();
						}
					}
					
					if(filterAble){
						Criteria subCriteria = Criteria.create();
						if (com.mxpioframework.security.Constants.DatascopeEnum.DEPT.getCode()
								.equals(dataFilter.getDataScope())) {
							Set<String> deptCodes = deptService.getDeptKeysByUser(SecurityUtils.getLoginUsername(), "code");
							subCriteria.addCriterion("createDept", Operator.IN, deptCodes);
						} else if (com.mxpioframework.security.Constants.DatascopeEnum.USER.getCode()
								.equals(dataFilter.getDataScope())) {
							subCriteria.addCriterion("createBy", Operator.EQ, SecurityUtils.getLoginUsername());
						} else if (com.mxpioframework.security.Constants.DatascopeEnum.DEPT_AND_CHILD.getCode()
								.equals(dataFilter.getDataScope())) {
							Set<String> deptCodes = deptService.getDeptKeysByUser(SecurityUtils.getLoginUsername(), "code");
							if(deptCodes.size()>0){
								subCriteria.addCriterion("createDept", Operator.LIKE_START, deptCodes.toArray()[0]);
							}else{
								subCriteria.addCriterion("createDept", Operator.EQ, "");
							}
							
						} else if (com.mxpioframework.security.Constants.DatascopeEnum.SERVICE.getCode()
								.equals(dataFilter.getDataScope()) && dataScapeProviderMap != null) {
							for (Entry<String, DataScapeProvider> entry : dataScapeProviderMap.entrySet()) {
								if (entry.getKey().equals(dataFilter.getService())) {
									List<Criterion> criterions = entry.getValue().provide();
									for (Criterion criterion : criterions) {
										subCriteria.addCriterion(criterion);
									}
									break;
								}
							}
						}
						filterCriteria.addCriterion(subCriteria);
					}
				}
				filterCriteria.end();
				c.addCriterion(filterCriteria);
				/*if (dataResource.getDataScope() != null) {
					boolean filterAble = true;
					if(criteriaFilterPreProcessors != null && dataResource.getPreProcess() != null){
						CriteriaFilterPreProcessor processor = criteriaFilterPreProcessors.get(dataResource.getPreProcess());
						if(processor != null){
							filterAble = processor.process();
						}
					}
					if(filterAble){
						if (com.mxpioframework.security.Constants.DatascopeEnum.DEPT.getCode()
								.equals(dataResource.getDataScope())) {
							Set<String> deptCodes = deptService.getDeptKeysByUser(SecurityUtils.getLoginUsername(), "code");
							c.addCriterion("createDept", Operator.IN, deptCodes);
						} else if (com.mxpioframework.security.Constants.DatascopeEnum.USER.getCode()
								.equals(dataResource.getDataScope())) {
							c.addCriterion("createBy", Operator.EQ, SecurityUtils.getLoginUsername());
						} else if (com.mxpioframework.security.Constants.DatascopeEnum.DEPT_AND_CHILD.getCode()
								.equals(dataResource.getDataScope())) {
							Set<String> deptCodes = deptService.getDeptKeysByUser(SecurityUtils.getLoginUsername(), "code");
							if(deptCodes.size()>0){
								c.addCriterion("createDept", Operator.LIKE_START, deptCodes.toArray()[0]);
							}else{
								c.addCriterion("createDept", Operator.EQ, "");
							}
							
						} else if (com.mxpioframework.security.Constants.DatascopeEnum.SERVICE.getCode()
								.equals(dataResource.getDataScope()) && dataScapeProviderMap != null) {
							for (Entry<String, DataScapeProvider> entry : dataScapeProviderMap.entrySet()) {
								if (entry.getKey().equals(dataResource.getService())) {
									List<Criterion> criterions = entry.getValue().provide();
									for (Criterion criterion : criterions) {
										c.addCriterion(criterion);
									}
									break;
								}
							}
						}
					}
					
				}*/
			}
		}
		return c;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterName().equals("criteria");
	}
}
