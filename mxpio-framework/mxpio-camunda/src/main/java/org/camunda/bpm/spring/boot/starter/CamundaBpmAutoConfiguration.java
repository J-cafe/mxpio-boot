package org.camunda.bpm.spring.boot.starter;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.spring.boot.starter.event.ProcessApplicationEventPublisher;
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.camunda.bpm.spring.boot.starter.property.ManagementProperties;
import org.camunda.bpm.spring.boot.starter.util.CamundaBpmVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

/**
 * Camunda Spring Boot Auto-Configuration adapted for Spring Boot 4.0.x
 * 
 * Original from camunda-bpm-spring-boot-starter 7.24.0
 * Modified: removed @AutoConfigureAfter(HibernateJpaAutoConfiguration.class)
 * which was removed in Spring Boot 4.0 (Spring Framework 7.0)
 */
@Configuration
@ConditionalOnProperty(prefix = "camunda.bpm", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties({ CamundaBpmProperties.class, ManagementProperties.class })
@Import({
    CamundaBpmConfiguration.class,
    CamundaBpmActuatorConfiguration.class,
    CamundaBpmPluginConfiguration.class,
    CamundaBpmTelemetryConfiguration.class,
    org.camunda.bpm.engine.spring.SpringProcessEngineServicesConfiguration.class
})
public class CamundaBpmAutoConfiguration {

    @Bean
    public CamundaBpmVersion camundaBpmVersion() {
        return new CamundaBpmVersion();
    }

    @Bean
    public ProcessApplicationEventPublisher processApplicationEventPublisher(
            ApplicationEventPublisher publisher) {
        return new ProcessApplicationEventPublisher(publisher);
    }

    @Configuration
    static class ProcessEngineConfigurationImplDependingConfiguration {

        @Autowired
        protected ProcessEngineConfigurationImpl processEngineConfigurationImpl;

        @Bean
        public ProcessEngineFactoryBean processEngineFactoryBean() {
            ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
            factoryBean.setProcessEngineConfiguration(processEngineConfigurationImpl);
            return factoryBean;
        }

        @Bean
        @Primary
        public CommandExecutor commandExecutorTxRequired() {
            return processEngineConfigurationImpl.getCommandExecutorTxRequired();
        }

        @Bean
        public CommandExecutor commandExecutorTxRequiresNew() {
            return processEngineConfigurationImpl.getCommandExecutorTxRequiresNew();
        }

        @Bean
        public CommandExecutor commandExecutorSchemaOperations() {
            return processEngineConfigurationImpl.getCommandExecutorSchemaOperations();
        }
    }
}
