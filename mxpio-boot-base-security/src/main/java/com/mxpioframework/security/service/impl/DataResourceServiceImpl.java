package com.mxpioframework.security.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.druid.util.StringUtils;
import com.mxpioframework.common.exception.MBootException;
import com.mxpioframework.security.service.RbacCacheService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.MethodParameter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.common.Constants;
import com.mxpioframework.security.decision.manager.SecurityDecisionManager;
import com.mxpioframework.security.entity.DataResource;
import com.mxpioframework.security.entity.Permission;
import com.mxpioframework.security.entity.ResourceType;
import com.mxpioframework.security.service.DataResourceService;
import com.mxpioframework.security.service.UserService;
import com.mxpioframework.security.util.ApplicationContextProvider;
import com.mxpioframework.security.vo.DataVo;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.util.pattern.PathPattern;

@Service("mxpio.security.dataResourceService")
public class DataResourceServiceImpl extends BaseServiceImpl<DataResource> implements DataResourceService {

	@Value("${mxpio.systemAnonymous}")
	private String systemAnonymous;

	@Value("${mxpio.customAnonymous}")
	private String customAnonymous;

	@Autowired
	private SecurityDecisionManager securityDecisionManager;

	@Autowired
	private UserService userService;

	@Autowired
	private RbacCacheService rbacCacheService;

	@Override
	public List<DataVo> findAllApi(boolean onlyCriteria, String path) {
		RequestMappingHandlerMapping mapping = getRequestMappingHandlerMapping();
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
		List<DataVo> dataVos = new ArrayList<>();
		Map<String,DataVo> patternsMap = new HashMap<>();
		for(Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()){
			RequestMappingInfo info = m.getKey();
	        HandlerMethod method = m.getValue();
			if(path != null && !StringUtils.equals(getRequestPath(info),path)){
				continue;
			}

			if(patternsMap.get(getRequestPath(info)) != null){
				continue;
			}

	        RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
	        MethodParameter[] parameters = method.getMethodParameters();
	        boolean hasCriteria = false;
	        for(MethodParameter parameter : parameters){
	        	if(Criteria.class.equals(parameter.getExecutable().getParameterTypes()[parameter.getParameterIndex()])){
	        		hasCriteria = true;
	        		break;
	        	}
	        }
	        if(!onlyCriteria || hasCriteria){
	        	String title = null;
		        Operation operation = method.getMethodAnnotation(Operation.class);
		        if(operation != null){
		        	title = operation.summary();
		        }
	        	DataVo dataVo = DataVo.builder()
	        			.title(title)
	    	        	.requestMethods(methodsCondition.getMethods())
	    	        	.className(method.getMethod().getDeclaringClass().getName())
	    	        	.classMethod(method.getMethod().getName())
	    	        	.httpUrls(getRequestPathSet(info))
	    	        	.hasCriteria(hasCriteria)
	    	        	.method(method.getMethod())
	    	        	.beanClass(method.getBeanType())
	    	        	.build();
	    	    dataVos.add(dataVo);
	    	    patternsMap.put(getRequestPath(info), dataVo);
	        }
		}
		return dataVos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DataResource> getByUrlId(String urlId) {
		List<DataResource> list = JpaUtil.linq(DataResource.class).equal("parentId", urlId).list();
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DataResource> findByUsername(String username) {
		List<DataResource> datas = rbacCacheService.findAllDataResource();
		List<DataResource> result = new ArrayList<>();
		for(DataResource data : datas){
			if (decide(username, data, userService.isAdministrator())) {
				result.add(data);
			}
		}
		return result;
	}

	private boolean decide(String username, DataResource data, boolean administrator) {
		if (administrator || securityDecisionManager.decide(username, data)) {
			return true;
		}
		return false;
	}

	private RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		String clazzName = "org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping";
		Map<String,RequestMappingHandlerMapping> requestMappingHandlerMappingMap = ApplicationContextProvider.getBeansOfType(RequestMappingHandlerMapping.class);
		for(Map.Entry<String,RequestMappingHandlerMapping> entry:requestMappingHandlerMappingMap.entrySet()){
			if(clazzName.equals(entry.getValue().getClass().getName())){
				return entry.getValue();
			}
		}
		throw new MBootException("获取RequestMapping异常");
	}

	private String getRequestPath(RequestMappingInfo info){
		Set<String> set =getRequestPathSet(info);
		if(CollectionUtils.isNotEmpty(set)){
			return set.iterator().next();
		}
		return null;
	}

	private Set<String> getRequestPathSet(RequestMappingInfo info){
		if(info.getPatternsCondition()!=null){
			return info.getPatternsCondition().getPatterns();

		}
		else if(info.getPathPatternsCondition()!=null){
			Set<PathPattern> patternsSet = info.getPathPatternsCondition().getPatterns();
			return patternsSet.stream().map(PathPattern::getPatternString).collect(Collectors.toSet());
		}
		return null;
	}

}
