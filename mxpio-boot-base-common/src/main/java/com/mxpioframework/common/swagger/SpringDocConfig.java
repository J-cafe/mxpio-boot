package com.mxpioframework.common.swagger;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SpringDocConfig {

	@Value("${mxpio.swagger.title:MxpIO-Boot}")
	private String swaggerTitle;
	
	@Value("${mxpio.swagger.desc:MxpIO-Boot API}")
	private String swaggerDesc;
	
	@Value("${mxpio.swagger.version:1.0.12-beta.8}")
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
	
	@Bean
	public OpenAPI openApi() {
		return new OpenAPI().info(info());
	}

	private License license() {
		return new License().name(swaggerLicense).url(swaggerLicenseUrl);
	}

	private Contact contact() {
		return new Contact().name(swaggerAuthor).email(swaggerEmail).url(swaggerHomepage);
	}

	private Info info() {
		return new Info().title(swaggerTitle).description(swaggerDesc).version(swaggerVersion).license(license())
				.contact(contact());
	}
	
	@Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("boot")
                .packagesToScan("com.mxpioframework")
                .build();
    }
}
