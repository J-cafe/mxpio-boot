package com.mxpioframework.security.access.filter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
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
import com.mxpioframework.security.entity.DataResource;
import com.mxpioframework.security.service.DataResourceService;
import com.mxpioframework.security.service.DeptService;
import com.mxpioframework.security.util.SecurityUtils;

@Component
public class CriteriaHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private DataResourceService dataResourceService;

	@Autowired
	private DeptService deptService;

	@Autowired(required = false)
	private Map<String, DataScapeProvider> dataScapeProviderMap;

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
			/*Map<String, List<DataResource>> dataResourceMap = JpaUtil.classify(datas, "path");
			List<DataResource> dataResources = dataResourceMap
					.get(classRequestMapping.value()[0] + requestMapping.value()[0]);*/
			
			Map<String, DataResource> dataResourceMap = Collections.emptyMap();
			for (DataResource obj : datas) {
				String key = obj.getKey();
				dataResourceMap.put(key, obj);
			}
			
			DataResource dataResource = dataResourceMap.get(urlKey+"_"+classRequestMapping.value()[0] + requestMapping.value()[0]);
			
			
			if (/*CollectionUtils.isNotEmpty(dataResources)*/ dataResource != null) {
				// DataResource dataResource = dataResources.get(0);
				if (dataResource.getDataScope() != null) {
					if (com.mxpioframework.security.Constants.DatascopeEnum.DEPT.getCode()
							.equals(dataResource.getDataScope())) {
						Set<String> deptCodes = deptService.getDeptKeysByUser(SecurityUtils.getLoginUsername(), "code");
						c.addCriterion("createDept", Operator.IN, deptCodes);
					} else if (com.mxpioframework.security.Constants.DatascopeEnum.USER.getCode()
							.equals(dataResource.getDataScope())) {
						c.addCriterion("createBy", Operator.EQ, SecurityUtils.getLoginUsername());
					} else if (com.mxpioframework.security.Constants.DatascopeEnum.DEPT_AND_CHILD.getCode()
							.equals(dataResource.getDataScope())) {
						//TODO 过滤子部门权限
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
			}
		}
		return c;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterName().equals("criteria");
	}
}
