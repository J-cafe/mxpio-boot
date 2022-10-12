package com.mxpioframework.security.access.filter;

import java.util.Map;
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
import com.mxpioframework.jpa.query.Operator;
import com.mxpioframework.security.DataAuthenticationException;
import com.mxpioframework.security.decision.manager.SecurityDecisionManager;
import com.mxpioframework.security.entity.DataResource;
import com.mxpioframework.security.service.DataResourceService;
import com.mxpioframework.security.service.DeptService;
import com.mxpioframework.security.util.SecurityUtils;

@Component
public class CriteriaHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
	
	@Autowired
	private SecurityDecisionManager securityDecisionManager;
	
	@Autowired
	private DataResourceService dataResourceService;
	
	@Autowired
	private DeptService deptService;

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request,
			WebDataBinderFactory webDataBinderFactory) throws Exception {
		String criteria = (String) request.getAttribute("criteria", NativeWebRequest.SCOPE_REQUEST);
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		// HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
		Map<String, DataResource> dataResourceMap = dataResourceService.findAllByCatch();
		RequestMapping requestMapping = parameter.getMethodAnnotation(RequestMapping.class);
		RequestMapping classRequestMapping = parameter.getContainingClass().getDeclaredAnnotation(RequestMapping.class);
		
		DataResource dataResource = null;
		if(requestMapping != null){
			dataResource = dataResourceMap.get(classRequestMapping.value()[0] + requestMapping.value()[0]);
		}
		if(dataResource != null){
			if(!decide(dataResource, c)){
				throw new DataAuthenticationException("权限异常");
			}
		}
		return c;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterName().equals("criteria");
	}
	
	private boolean decide(DataResource dataResource, Criteria c) {
		if (securityDecisionManager.decide(dataResource)) {
			if(dataResource.getDataScope() != null){
				if(com.mxpioframework.security.Constants.DatascopeEnum.DEPT.getCode().equals(dataResource.getDataScope())){
					Set<String> deptIds = deptService.getDeptIdsByUser(SecurityUtils.getLoginUsername());
					c.addCriterion("deptId", Operator.IN, deptIds);
				}else if(com.mxpioframework.security.Constants.DatascopeEnum.USER.getCode().equals(dataResource.getDataScope())) {
					c.addCriterion("createBy", Operator.EQ, SecurityUtils.getLoginUsername());
				}
			}
			return true;
		}
		return false;
	}

}
