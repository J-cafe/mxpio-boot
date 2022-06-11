package com.mxpioframework.common.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {
	
	@Value("${mxpio.swagger.title:MxpIO-Boot}")
	private String swaggerTitle;
	
	@Value("${mxpio.swagger.desc:MxpIO-Boot API}")
	private String swaggerDesc;
	
	@Value("${mxpio.swagger.version:1.0.7}")
	private String swaggerVersion;
	
	@Value("${mxpio.swagger.author:MxpIO}")
	private String swaggerAuthor;
	
	@Value("${mxpio.swagger.email:i@mxpio.com}")
	private String swaggerEmail;
	
	@Value("${mxpio.swagger.homepage:www.mxpio.com}")
	private String swaggerHomepage;
	
	@Value("${mxpio.swagger.license:MIT License}")
	private String swaggerLicense;
	
	@Value("${mxpio.swagger.license.url:https://gitee.com/i_mxpio/mxpio-boot/blob/master/LICENSE}")
	private String swaggerLicenseUrl;
	
	@Value("${mxpio.swagger.basepackage:com.mxpio}")
	private String swaggerBasePackage;
	
	/**
	 *
	 * 显示swagger-ui.html文档展示页，还必须注入swagger资源：
	 * 
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).pathMapping("/").select()
			.apis(RequestHandlerSelectors.basePackage(swaggerBasePackage)).paths(PathSelectors.any()).build()
			.apiInfo(
				new ApiInfoBuilder()
					.title(swaggerTitle)
					.description(swaggerDesc)
					.version(swaggerVersion)
					.contact(new Contact(swaggerAuthor, swaggerHomepage, swaggerEmail))
					.license(swaggerLicense)
					.licenseUrl(swaggerLicenseUrl)
					.build()
			);
	}
}
