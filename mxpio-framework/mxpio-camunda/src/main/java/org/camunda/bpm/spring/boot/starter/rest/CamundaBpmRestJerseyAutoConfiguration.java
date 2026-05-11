package org.camunda.bpm.spring.boot.starter.rest;

import org.camunda.bpm.engine.rest.impl.FetchAndLockContextListener;
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jersey.autoconfigure.JerseyAutoConfiguration;
import org.springframework.boot.jersey.autoconfigure.JerseyApplicationPath;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.camunda.bpm.spring.boot.starter.CamundaBpmAutoConfiguration;

/**
 * Camunda REST Jersey auto-configuration adapted for Spring Boot 4.0.x
 * 
 * Original from camunda-bpm-spring-boot-starter-rest 7.24.0
 * Modified:
 * - Changed @AutoConfigureBefore value from old package
 *   org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration
 *   to org.springframework.boot.jersey.autoconfigure.JerseyAutoConfiguration
 * - Import paths updated for Spring Boot 4.0 (jersey packages moved)
 */
@Configuration
@AutoConfigureBefore(JerseyAutoConfiguration.class)
@AutoConfigureAfter(CamundaBpmAutoConfiguration.class)
public class CamundaBpmRestJerseyAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(CamundaJerseyResourceConfig.class)
    public CamundaJerseyResourceConfig createRestConfig() {
        return new CamundaJerseyResourceConfig();
    }

    @Bean
    public FetchAndLockContextListener getFetchAndLockContextListener() {
        return new FetchAndLockContextListener();
    }

    @Bean
    public CamundaBpmRestInitializer camundaBpmRestInitializer(
            JerseyApplicationPath applicationPath,
            CamundaBpmProperties props) {
        return new CamundaBpmRestInitializer(applicationPath, props);
    }
}
