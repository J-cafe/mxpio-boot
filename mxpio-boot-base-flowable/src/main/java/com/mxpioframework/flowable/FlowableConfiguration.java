package com.mxpioframework.flowable;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * 模块配置类
 */
@Slf4j
@Configuration
@AutoConfigurationPackage
@ComponentScan
public class FlowableConfiguration {
	
	@Bean
    public CommandLineRunner init(final RepositoryService repositoryService,
                                  final RuntimeService runtimeService,
                                  final TaskService taskService) {

        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
            	log.info("===========Flowable 信息===========");
                log.info("流程定义数量:{}",repositoryService.createProcessDefinitionQuery().count());
                log.info("任务数量:{}",taskService.createTaskQuery().count());
                log.info("==================================");
            }
        };
    }

}
