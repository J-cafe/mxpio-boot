package com.mxpioframework.security.access.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mxpioframework.security.service.*;
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
import com.mxpioframework.jpa.query.Junction;
import com.mxpioframework.jpa.query.JunctionType;
import com.mxpioframework.jpa.query.Operator;
import com.mxpioframework.security.access.datascope.provider.DataScapeProvider;
import com.mxpioframework.security.access.provider.CriteriaFilterPreProcessor;
import com.mxpioframework.security.entity.DataFilter;
import com.mxpioframework.security.entity.DataResource;
import com.mxpioframework.security.entity.RoleGrantedAuthority;
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

	@Autowired
	private RbacCacheService rbacCacheService;

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
			List<DataResource> datas = rbacCacheService.findAllDataResource();
			
			Map<String, DataResource> dataResourceMap = new HashMap<>();
			for (DataResource obj : datas) {
				String key = obj.getKey();
				dataResourceMap.put(key, obj);
			}
			
			DataResource dataResource = dataResourceMap.get(urlKey+"_"+classRequestMapping.value()[0] + requestMapping.value()[0]);
			
			if (dataResource != null && dataResource.isHasFilter()) {
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
								Set<String> deptCodes = deptService.getDeptKeysByUser(SecurityUtils.getLoginUsername(), "code");
								juntion.addCriterion("createDept", Operator.IN, deptCodes);
							} else if (com.mxpioframework.security.Constants.DatascopeEnum.USER.getCode()
									.equals(dataFilter.getDataScope())) {
								juntion.addCriterion("createBy", Operator.EQ, SecurityUtils.getLoginUsername());
							} else if (com.mxpioframework.security.Constants.DatascopeEnum.DEPT_AND_CHILD.getCode()
									.equals(dataFilter.getDataScope())) {
								Set<String> deptCodes = deptService.getDeptKeysByUser(SecurityUtils.getLoginUsername(), "code");
								if(!deptCodes.isEmpty()){
									String deptCode = (String) deptCodes.toArray()[0];
									//获取部门及子部门
									List<String> deptCodeWithSubByCode = deptService.getDeptCodeWithSubByCode(deptCode);
									if (deptCodeWithSubByCode.isEmpty()||deptCodeWithSubByCode.size()==1){
										juntion.addCriterion("createDept", Operator.EQ, deptCode);
									}else{
										juntion.addCriterion("createDept", Operator.IN, deptCodeWithSubByCode);
									}
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
		}
		return c;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterName().equals("criteria");
	}
}
