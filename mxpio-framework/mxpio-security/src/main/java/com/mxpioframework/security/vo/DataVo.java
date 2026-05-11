package com.mxpioframework.security.vo;

import java.lang.reflect.Method;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description="数据资源")
public class DataVo {

	private String title;
	
	private Set<RequestMethod> requestMethods;
	
	private String className;
	
	private String classMethod;
	
	private Set<String> httpUrls;
	
	private boolean hasCriteria;
	
	@JsonIgnore
	private Method method;
	
	@JsonIgnore
	private Class<?> beanClass;
	
}
