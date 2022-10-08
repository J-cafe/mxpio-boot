package com.mxpioframework.security.vo;

import java.util.Set;

import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description="数据资源")
public class DataVo {

	private Set<RequestMethod> requestMethods;
	
	private String className;
	
	private String classMethod;
	
	private Set<String> httpUrls;
	
	private boolean hasCriteria;
	
}
