package com.mxpioframework.common.jdk;

import org.burningwave.core.assembler.StaticComponentContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * fuck jdk modules
 */
@Component
public class JdkModulesExportProcess implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        StaticComponentContainer.Modules.exportAllToAll();
    }
}
