package com.mxpioframework.security.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.common.Constants;
import com.mxpioframework.security.entity.DataResource;
import com.mxpioframework.security.service.DataResourceService;
import com.mxpioframework.security.util.ApplicationContextProvider;
import com.mxpioframework.security.vo.DataVo;

@Service
public class DataResourceServiceImpl extends BaseServiceImpl<DataResource> implements DataResourceService {
	
	@Override
	public List<DataVo> findAll() {
		RequestMappingHandlerMapping mapping = ApplicationContextProvider.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
		List<DataVo> dataVos = new ArrayList<>();
		for(Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()){
			RequestMappingInfo info = m.getKey();
	        HandlerMethod method = m.getValue();
	        RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
	        MethodParameter[] parameters = method.getMethodParameters();
	        boolean hasCriteria = false;
	        for(MethodParameter parameter : parameters){
	        	if(Criteria.class.equals(parameter.getExecutable().getParameterTypes()[parameter.getParameterIndex()])){
	        		hasCriteria = true;
	        		break;
	        	}
	        }
        	DataVo dataVo = DataVo.builder()
    	        	.requestMethods(methodsCondition.getMethods())
    	        	.className(method.getMethod().getDeclaringClass().getName())
    	        	.classMethod(method.getMethod().getName())
    	        	.httpUrls(info.getPatternsCondition().getPatterns())
    	        	.hasCriteria(hasCriteria)
    	        	.build();
    	        dataVos.add(dataVo);
		}
		return dataVos;
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(cacheNames = Constants.DATA_RESOURCE_CACHE_KEY, keyGenerator = Constants.KEY_GENERATOR_BEAN_NAME)
	public Map<String, DataResource> findAllByCatch() {
		List<DataResource> list = JpaUtil.linq(DataResource.class).list();
		return JpaUtil.index(list, "path");
	}

	@Override
	@Transactional(readOnly = true)
	public List<DataResource> getByUrlId(String urlId) {
		List<DataResource> list = JpaUtil.linq(DataResource.class).equal("parentResId", urlId).list();
		return list;
	}

}
