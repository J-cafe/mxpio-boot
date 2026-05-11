package org.camunda.bpm.spring.boot.starter.rest;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;

import org.camunda.bpm.engine.rest.filter.CacheControlFilter;
import org.camunda.bpm.engine.rest.filter.EmptyBodyFilter;
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jersey.autoconfigure.JerseyApplicationPath;
import org.springframework.boot.web.servlet.ServletContextInitializer;

import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;

/**
 * Camunda REST initializer adapted for Spring Boot 4.0.x
 * 
 * Original from camunda-bpm-spring-boot-starter-rest 7.24.0
 * Modified:
 * - Changed JerseyApplicationPath import from
 *   org.springframework.boot.autoconfigure.web.servlet.JerseyApplicationPath
 *   to org.springframework.boot.jersey.autoconfigure.JerseyApplicationPath
 */
public class CamundaBpmRestInitializer implements ServletContextInitializer {

    private static final Logger log = LoggerFactory.getLogger(CamundaBpmRestInitializer.class);

    private static final EnumSet<DispatcherType> DISPATCHER_TYPES = EnumSet.of(
            DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE);

    private ServletContext servletContext;
    private JerseyApplicationPath applicationPath;
    private final CamundaBpmProperties properties;

    public CamundaBpmRestInitializer(JerseyApplicationPath applicationPath, CamundaBpmProperties properties) {
        this.applicationPath = applicationPath;
        this.properties = properties;
    }

    @Override
    public void onStartup(ServletContext servletContext) {
        this.servletContext = Objects.requireNonNull(servletContext);
        Map<String, String> fetchAndLockInitParams = properties
                .getRestApi()
                .getFetchAndLock()
                .getInitParams();
        fetchAndLockInitParams.forEach((key, value) ->
                servletContext.setInitParameter(key, value));
        String urlMapping = applicationPath.getUrlMapping();
        registerFilter("EmptyBodyFilter", EmptyBodyFilter.class, urlMapping);
        registerFilter("CacheControlFilter", CacheControlFilter.class, urlMapping);
    }

    private FilterRegistration registerFilter(String filterName,
            Class<? extends Filter> filterClass, String... urlPatterns) {
        return registerFilter(filterName, filterClass, null, urlPatterns);
    }

    private FilterRegistration registerFilter(String filterName,
            Class<? extends Filter> filterClass,
            Map<String, String> initParameters, String... urlPatterns) {
        FilterRegistration registration = servletContext.getFilterRegistration(filterName);
        if (registration != null) {
            return registration;
        }
        FilterRegistration.Dynamic dynamic = servletContext.addFilter(filterName, filterClass);
        dynamic.addMappingForUrlPatterns(DISPATCHER_TYPES, true, urlPatterns);
        if (initParameters != null) {
            dynamic.setInitParameters(initParameters);
        }
        log.debug("Filter {} for URL {} registered.", filterName, (Object) urlPatterns);
        return dynamic;
    }
}
