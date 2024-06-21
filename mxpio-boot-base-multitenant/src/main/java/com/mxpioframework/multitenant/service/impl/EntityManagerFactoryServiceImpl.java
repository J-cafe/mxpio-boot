package com.mxpioframework.multitenant.service.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.SchemaManagementProvider;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder.Builder;
import org.springframework.boot.orm.jpa.hibernate.SpringJtaPlatform;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.weaving.LoadTimeWeaverAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.io.ResourceLoader;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jndi.JndiLocatorDelegate;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.mxpioframework.multitenant.Constants;
import com.mxpioframework.multitenant.HibernateDefaultDdlAutoProvider;
import com.mxpioframework.multitenant.domain.Organization;
import com.mxpioframework.multitenant.listener.EntityManagerFactoryCreateListener;
import com.mxpioframework.multitenant.service.DataSourceService;
import com.mxpioframework.multitenant.service.EntityManagerFactoryService;
import com.mxpioframework.multitenant.service.ScriptService;

@Service("mxpio.multitenant.entityManagerFactoryService")
public class EntityManagerFactoryServiceImpl implements
		EntityManagerFactoryService, BeanClassLoaderAware, BeanFactoryAware, BeanNameAware, ResourceLoaderAware, LoadTimeWeaverAware, InitializingBean {

	private Map<String, EntityManagerFactory> emfMap = new ConcurrentHashMap<String, EntityManagerFactory>();

	@Autowired
	private DataSourceService dataSourceService;

	@Autowired
	private ScriptService scriptService;

	@Value("${mxpio.multitenant.dataScript:}")
	private String dataScript;

	@Autowired
	private EntityManagerFactory emf;

	@Autowired(required = false)
	private JtaTransactionManager jtaTransactionManager;

	private LoadTimeWeaver loadTimeWeaver;

	private ResourceLoader resourceLoader;

	private ClassLoader classLoader;

	private String beanName;

	private final HibernateDefaultDdlAutoProvider defaultDdlAutoProvider;

	private HibernateProperties hibernateProperties;

	private final List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers;

	@Autowired(required = false)
	private List<EntityManagerFactoryCreateListener> listeners;

	private static final Log logger = LogFactory
			.getLog(EntityManagerFactoryServiceImpl.class);

	private static final String JTA_PLATFORM = "hibernate.transaction.jta.platform";

	/**
	 * {@code NoJtaPlatform} implementations for various Hibernate versions.
	 */
	private static final String[] NO_JTA_PLATFORM_CLASSES = {
			"org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform",
			"org.hibernate.service.jta.platform.internal.NoJtaPlatform" };

	/**
	 * {@code WebSphereExtendedJtaPlatform} implementations for various Hibernate
	 * versions.
	 */
	private static final String[] WEBSPHERE_JTA_PLATFORM_CLASSES = {
			"org.hibernate.engine.transaction.jta.platform.internal.WebSphereExtendedJtaPlatform",
			"org.hibernate.service.jta.platform.internal.WebSphereExtendedJtaPlatform", };

	@Autowired
	private JpaProperties properties;

	private BeanFactory beanFactory;

	@Autowired(required = false)
	private PersistenceUnitManager persistenceUnitManager;

	@Value("${mxpio.multitenant.packagesToScan:"
			+ "com.mxpioframework.excel.importer.model,"
			+ "com.mxpioframework.filestorage.entity,"
			+ "com.mxpioframework.camunda.entity,"
			+ "com.mxpioframework.log.entity,"
			+ "com.mxpioframework.quartz.entity,"
			+ "com.mxpioframework.system.entity,"
			+ "com.mxpioframework.security.entity}")
	private String packagesToScan;

	@Value("${mxpio.multitenant.customPackagesToScan:}")
	private String customPackagesToScan;


	public EntityManagerFactoryServiceImpl(
			HibernateProperties hibernateProperties,
			ObjectProvider<List<SchemaManagementProvider>> providers,
			ObjectProvider<PhysicalNamingStrategy> physicalNamingStrategy,
			ObjectProvider<ImplicitNamingStrategy> implicitNamingStrategy,
			ObjectProvider<List<HibernatePropertiesCustomizer>> hibernatePropertiesCustomizers) {
		this.hibernateProperties = hibernateProperties;
		this.defaultDdlAutoProvider = new HibernateDefaultDdlAutoProvider(
				providers.getIfAvailable(Collections::emptyList));
		this.hibernatePropertiesCustomizers = hibernatePropertiesCustomizers
				.getIfAvailable(() -> Collections.emptyList());
	}

	protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	private String[] mergePackagesToScan() {
		String[] packages = null;
		if (StringUtils.hasText(packagesToScan) && StringUtils.hasText(customPackagesToScan)) {
			packages = (packagesToScan + "," + customPackagesToScan).split(",");
		} else if (StringUtils.hasText(packagesToScan)) {
			packages = packagesToScan.split(",");
		}  else if (StringUtils.hasText(customPackagesToScan)) {
			packages = customPackagesToScan.split(",");
		}
		return packages;
	}

	public JpaVendorAdapter getJpaVendorAdapter(DataSource dataSource) {
		/*AbstractJpaVendorAdapter adapter = createJpaVendorAdapter();
		adapter.setShowSql(this.properties.isShowSql());
		adapter.setDatabase(this.properties.determineDatabase(dataSource));
		adapter.setDatabasePlatform(this.properties.getDatabasePlatform());
		adapter.setGenerateDdl(this.properties.isGenerateDdl());
		return adapter;*/

		AbstractJpaVendorAdapter adapter = createJpaVendorAdapter();
		adapter.setShowSql(this.properties.isShowSql());
		if (this.properties.getDatabase() != null) {
			adapter.setDatabase(this.properties.getDatabase());
		}
		if (this.properties.getDatabasePlatform() != null) {
			adapter.setDatabasePlatform(this.properties.getDatabasePlatform());
		}
		adapter.setGenerateDdl(this.properties.isGenerateDdl());
		return adapter;
	}

	public EntityManagerFactoryBuilder getEntityManagerFactoryBuilder(DataSource dataSource) {
		JpaVendorAdapter jpaVendorAdapter = getJpaVendorAdapter(dataSource);
		EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(
				jpaVendorAdapter, properties.getProperties(),
				this.persistenceUnitManager);
		return builder;
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory(Organization organization) {
		return emfMap.get(organization.getId());
	}

	@Override
	public EntityManagerFactory createEntityManagerFactory(Organization organization) {
		DataSource dataSource = dataSourceService.getOrCreateDataSource(organization);
		Map<String, Object> vendorProperties = getVendorProperties(dataSource);
		customizeVendorProperties(vendorProperties);
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = getEntityManagerFactoryBuilder(dataSource).dataSource(dataSource).packages(mergePackagesToScan())
				.properties(vendorProperties).jta(isJta()).build();
		entityManagerFactoryBean.setBeanClassLoader(classLoader);
		entityManagerFactoryBean.setBeanFactory(beanFactory);
		entityManagerFactoryBean.setBeanName(beanName);
		entityManagerFactoryBean.setLoadTimeWeaver(loadTimeWeaver);
		entityManagerFactoryBean.setResourceLoader(resourceLoader);
		entityManagerFactoryBean.setPersistenceUnitName(organization.getId());
		entityManagerFactoryBean.afterPropertiesSet();
		scriptService.runScripts(organization.getId(), dataSource, dataScript, "multitenant-data");
		return entityManagerFactoryBean.getObject();
	}

	protected Map<String, Object> getVendorProperties(DataSource dataSource) {
		/*String defaultDdlMode = this.defaultDdlAutoProvider
				.getDefaultDdlAuto(dataSource);
		Map<String, Object> vendorProperties = new LinkedHashMap<String, Object>();
		vendorProperties.putAll(this.properties.getHibernateProperties(new HibernateSettings().ddlAuto(defaultDdlMode)
				.implicitNamingStrategy(this.implicitNamingStrategy)
				.physicalNamingStrategy(this.physicalNamingStrategy)
				.hibernatePropertiesCustomizers(
						this.hibernatePropertiesCustomizers)));
		return vendorProperties;*/

		Supplier<String> defaultDdlMode = () -> this.defaultDdlAutoProvider.getDefaultDdlAuto(dataSource);
		return new LinkedHashMap<>(this.hibernateProperties
				.determineHibernateProperties(this.properties.getProperties(), new HibernateSettings()
						.ddlAuto(defaultDdlMode).hibernatePropertiesCustomizers(this.hibernatePropertiesCustomizers)));
	}

	protected void customizeVendorProperties(Map<String, Object> vendorProperties) {
		if (!vendorProperties.containsKey(JTA_PLATFORM)) {
			configureJtaPlatform(vendorProperties);
		}
	}

	private void configureJtaPlatform(Map<String, Object> vendorProperties)
			throws LinkageError {
		JtaTransactionManager jtaTransactionManager = getJtaTransactionManager();
		if (jtaTransactionManager != null) {
			if (runningOnWebSphere()) {
				configureWebSphereTransactionPlatform(vendorProperties);
			}
			else {
				configureSpringJtaPlatform(vendorProperties, jtaTransactionManager);
			}
		}
		else {
			vendorProperties.put(JTA_PLATFORM, getNoJtaPlatformManager());
		}
	}

	private boolean runningOnWebSphere() {
		return ClassUtils.isPresent(
				"com.ibm.websphere.jtaextensions." + "ExtendedJTATransaction",
				getClass().getClassLoader());
	}

	private void configureWebSphereTransactionPlatform(
			Map<String, Object> vendorProperties) {
		vendorProperties.put(JTA_PLATFORM, getWebSphereJtaPlatformManager());
	}

	private Object getWebSphereJtaPlatformManager() {
		return getJtaPlatformManager(WEBSPHERE_JTA_PLATFORM_CLASSES);
	}

	private void configureSpringJtaPlatform(Map<String, Object> vendorProperties,
			JtaTransactionManager jtaTransactionManager) {
		try {
			vendorProperties.put(JTA_PLATFORM,
					new SpringJtaPlatform(jtaTransactionManager));
		}
		catch (LinkageError ex) {
			if (!isUsingJndi()) {
				throw new IllegalStateException("Unable to set Hibernate JTA "
						+ "platform, are you using the correct "
						+ "version of Hibernate?", ex);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Unable to set Hibernate JTA platform : " + ex.getMessage());
			}
		}
	}

	private boolean isUsingJndi() {
		try {
			return JndiLocatorDelegate.isDefaultJndiEnvironmentAvailable();
		}
		catch (Error ex) {
			return false;
		}
	}

	private Object getNoJtaPlatformManager() {
		return getJtaPlatformManager(NO_JTA_PLATFORM_CLASSES);
	}

	private Object getJtaPlatformManager(String[] candidates) {
		for (String candidate : candidates) {
			try {
				return Class.forName(candidate).newInstance();
			}
			catch (Exception ex) {
				// Continue searching
			}
		}
		throw new IllegalStateException("Could not configure JTA platform");
	}

	protected JtaTransactionManager getJtaTransactionManager() {
		return this.jtaTransactionManager;
	}

	protected final boolean isJta() {
		return (this.jtaTransactionManager != null);
	}

	@Override
	public void setBeanName(String name) {
		this.beanName = name;

	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;

	}

	@Override
	public void setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver) {
		this.loadTimeWeaver = loadTimeWeaver;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;

	}

	@Override
	public EntityManagerFactory getOrCreateEntityManagerFactory(
			Organization organization) {
		EntityManagerFactory emf = getEntityManagerFactory(organization);
		if (emf == null) {
			emf = createEntityManagerFactory(organization);
			emfMap.put(organization.getId(), emf);
		}
		return emf;
	}

	@Override
	public void generateTables(Organization organization) {
		SingleConnectionDataSource dataSource = dataSourceService.createSingleConnectionDataSource(organization);
		if (dataSource != null) {
			Map<String, Object> vendorProperties = getVendorProperties(dataSource);
			customizeVendorProperties(vendorProperties);
			Builder builder = getEntityManagerFactoryBuilder(dataSource).dataSource(dataSource).packages(packagesToScan.split(","))
					.properties(vendorProperties).jta(isJta());
			LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = builder.build();

		    publishEvent(organization, builder);

			entityManagerFactoryBean.setBeanClassLoader(classLoader);
			entityManagerFactoryBean.setBeanFactory(beanFactory);
			entityManagerFactoryBean.setBeanName(beanName);
			entityManagerFactoryBean.setLoadTimeWeaver(loadTimeWeaver);
			entityManagerFactoryBean.setResourceLoader(resourceLoader);
			entityManagerFactoryBean.afterPropertiesSet();
			entityManagerFactoryBean.destroy();
			dataSource.destroy();
		}
	}

	@Override
	public EntityManagerFactory createTempEntityManagerFactory(
			Organization organization) {
		SingleConnectionDataSource dataSource = dataSourceService.createSingleConnectionDataSource(organization);
		if (dataSource != null) {
			Map<String, Object> vendorProperties = getVendorProperties(dataSource);
			customizeVendorProperties(vendorProperties);
		    Builder builder = getEntityManagerFactoryBuilder(dataSource).dataSource(dataSource).packages(mergePackagesToScan())
					.properties(vendorProperties).jta(isJta());
			LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = builder.build();
			entityManagerFactoryBean.setBeanClassLoader(classLoader);
			entityManagerFactoryBean.setBeanFactory(beanFactory);
			entityManagerFactoryBean.setBeanName(beanName);
			entityManagerFactoryBean.setLoadTimeWeaver(loadTimeWeaver);
			entityManagerFactoryBean.setResourceLoader(resourceLoader);
			entityManagerFactoryBean.afterPropertiesSet();
			return entityManagerFactoryBean.getObject();
		}
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		emfMap.put(Constants.MASTER, emf);
		if (listeners != null) {
			AnnotationAwareOrderComparator.sort(listeners);
		}
	}

	@Override
	public void removeEntityManagerFactory(Organization organization) {
		emfMap.remove(organization.getId());
		dataSourceService.removeDataSource(organization);
	}

	private void publishEvent(Organization organization, Builder builder) {
		if (listeners != null) {
			for (EntityManagerFactoryCreateListener entityManagerFactoryCreateListener : listeners) {
				entityManagerFactoryCreateListener.onCreate(organization, builder);
			}
		}

	}

}
