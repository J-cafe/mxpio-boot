package com.mxpioframework.common.swagger;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SpringDocConfig {

	@Autowired
	private SwaggerProperties swaggerProperties;

	@Bean
	public OpenAPI openApi() {
		return new OpenAPI().info(info());
	}

	private License license() {
		return new License().name(swaggerProperties.getLicense()).url(swaggerProperties.getLicenseUrl());
	}

	private Contact contact() {
		return new Contact().name(swaggerProperties.getAuthor()).email(swaggerProperties.getEmail()).url(swaggerProperties.getHomepage());
	}

	private Info info() {
		return new Info().title(swaggerProperties.getTitle()).description(swaggerProperties.getDesc()).version(swaggerProperties.getVersion()).license(license())
				.contact(contact());
	}

	@Bean
	public GroupedOpenApi camundaOpenApi() {
	    return GroupedOpenApi.builder()
	            .group("Camunda")
	            .packagesToScan("org.camunda.bpm.engine.rest")
	            .build();
	}

	@Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("Boot")
                .packagesToScan("com.mxpioframework")
                .build();
    }

    @Bean
    public OpenApiCustomizer globalOpenApiCustomizer() {
        return openApi -> {
            // 获取当前分组名
            String groupName = openApi.getExtensions().get("group").toString();

            // 根据不同分组做不同配置
            if ("Camunda".equals(groupName)) {
                openApi.info(new Info()
                        .title("Camunda API")
                        .version("7.24.0"));
            }
        };
    }

}
