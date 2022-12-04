package com.mxpioframework.multitenant.service.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.mxpioframework.multitenant.Constants;
import com.mxpioframework.multitenant.script.DynamicResourceDatabasePopulator;
import com.mxpioframework.multitenant.script.ScriptVarRegister;
import com.mxpioframework.multitenant.service.DatabaseNameService;
import com.mxpioframework.multitenant.service.ScriptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ScriptServiceImpl implements ScriptService {
	
	@Autowired
	private ConfigurableApplicationContext applicationContext;
	
	/*@Autowired
	private DataSourceProperties properties;*/
	
	@Autowired(required = false)
	private List<ScriptVarRegister> scriptVarRegisters;
	
	@Autowired
	private DatabaseNameService databaseNameService;
	
	@Value("${spring.sql.init.platform:all}")
	private String platform;
	
	@Value("${spring.sql.init.continue-on-error:true}")
	private boolean continueOnError;
	
	@Value("${spring.sql.init.separator:;}")
	private String separator;
	
	@Value("${spring.sql.init.encoding:UTF-8}")
	private Charset encoding;
	
	
	@Override
	public void runScripts(String organizationId, DataSource dataSource, String locations, String fallback) {
		Map<String, Object> vars = new HashMap<>();
		String databaseName = databaseNameService.getDatabaseName(organizationId);
		vars.put("organizationId", organizationId);
		vars.put("databaseName", databaseName);
		if (scriptVarRegisters != null) {
			for (ScriptVarRegister scriptVarRegister : scriptVarRegisters) {
				scriptVarRegister.register(vars);
			}
		}
		List<Resource> scripts = getScripts(locations, fallback);
		runScripts(scripts, dataSource, vars);
	}

	private List<Resource> getScripts(String locations, String fallback) {
		if (StringUtils.hasLength(locations)) {
			// String platform = this.properties.getPlatform();
			locations = "classpath*:" + fallback + "-" + platform + ".sql,";
			locations += "classpath*:" + fallback + ".sql";
		}
		return getResources(locations);
	}

	private List<Resource> getResources(String locations) {
		List<Resource> resources = new ArrayList<Resource>();
		for (String location : StringUtils.commaDelimitedListToStringArray(locations)) {
			try {
				for (Resource resource : this.applicationContext.getResources(location)) {
					if (resource.exists()) {
						resources.add(resource);
					}
				}
			}
			catch (IOException ex) {
				throw new IllegalStateException(
						"Unable to load resource from " + location, ex);
			}
		}
		return resources;
	}

	private void runScripts(List<Resource> resources, DataSource dataSource, Map<String, Object> vars) {
		if (resources.isEmpty()) {
			return;
		}
		DynamicResourceDatabasePopulator populator = new DynamicResourceDatabasePopulator();
		// populator.setContinueOnError(this.properties.isContinueOnError());
		//populator.setSeparator(this.properties.getSeparator());
//		if (this.properties.getSqlScriptEncoding() != null) {
//			populator.setSqlScriptEncoding(this.properties.getSqlScriptEncoding().name());
//		}
		populator.setContinueOnError(continueOnError);
		populator.setSeparator(separator);
		if (encoding != null) {
			populator.setSqlScriptEncoding(encoding.name());
		}
		for (Resource resource : resources) {
			populator.addScript(resource);
		}
		populator.setVars(vars);
		DatabasePopulatorUtils.execute(populator, dataSource);
	}

	@Override
	public void runScripts(DataSource dataSource, String locations, String fallback) {
		runScripts(Constants.MASTER, dataSource, locations, fallback);
		
	}



}
