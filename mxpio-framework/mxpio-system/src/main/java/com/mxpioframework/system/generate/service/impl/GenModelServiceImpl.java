package com.mxpioframework.system.generate.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mxpioframework.common.util.FreemarkerHelper;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.system.generate.entity.GenModel;
import com.mxpioframework.system.generate.entity.GenProperty;
import com.mxpioframework.system.generate.service.GenModelService;
import com.mxpioframework.system.service.impl.BaseServiceImpl;

@Service("mxpio.system.genModelService")
public class GenModelServiceImpl extends BaseServiceImpl<GenModel> implements GenModelService {

	@Override
	public String generateFilesByModelCode(String modelCode) throws IOException {
		
		Map<String,Object> data = new HashMap<>();
		GenModel model = JpaUtil.linq(GenModel.class).collect("modelId", GenProperty.class, "id").equal("modelCode", modelCode).findOne();
		data.put("rootPackage", model.getRootPackage().replace('.', '\\'));
		data.put("genModel", model);
		data.put("packageName", model.getPackageName().replace('.', '\\'));
		
		String targetRoot = "E:\\generate\\" + UUID.randomUUID();
		
		String modelJavaPath = targetRoot + File.separator + data.get("rootPackage") + File.separator + data.get("packageName") + File.separator;
		//String modelVuePath = targetRoot + File.separator + "ui";
		//controller
		File controller = new File(modelJavaPath + "controller");
		controller.mkdirs();
		File controllerFile = new File(modelJavaPath + "controller" + File.separator + modelCode + "Controller.java");
		controllerFile.createNewFile();
		Writer controllerWriter = new FileWriter(controllerFile);
		FreemarkerHelper.process("/controller/Controller.java.ftl", data, controllerWriter);
		controllerWriter.close();
		
		//service
		File service = new File(modelJavaPath + "service");
		service.mkdirs();
		File serviceFile = new File(modelJavaPath + "service" + File.separator + modelCode + "Service.java");
		serviceFile.createNewFile();
		Writer serviceWriter = new FileWriter(serviceFile);
		FreemarkerHelper.process("/service/Service.java.ftl", data, serviceWriter);
		serviceWriter.close();
		//impl
		File impl = new File(modelJavaPath + "service" + File.separator + "impl");
		impl.mkdirs();
		File implFile = new File(modelJavaPath + "service" + File.separator + "impl" + File.separator + modelCode + "ServiceImpl.java");
		implFile.createNewFile();
		Writer implWriter = new FileWriter(implFile);
		FreemarkerHelper.process("/impl/ServiceImpl.java.ftl", data, implWriter);
		implWriter.close();
		
		//entity
		File entity = new File(modelJavaPath + "entity");
		entity.mkdirs();
		File entityFile = new File(modelJavaPath + "entity" + File.separator + modelCode + ".java");
		entityFile.createNewFile();
		Writer entityWriter = new FileWriter(entityFile);
		FreemarkerHelper.process("/entity/Entity.java.ftl", data, entityWriter);
		entityWriter.close();
		return targetRoot;
	}


}
