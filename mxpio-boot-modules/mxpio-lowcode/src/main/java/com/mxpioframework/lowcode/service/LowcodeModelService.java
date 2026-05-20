package com.mxpioframework.lowcode.service;

import java.util.List;

import com.mxpioframework.lowcode.entity.LowcodeModel;
import com.mxpioframework.lowcode.entity.LowcodeProperty;

public interface LowcodeModelService {

	LowcodeModel getByCode(String modelCode);

	List<LowcodeModel> listAll();

	LowcodeModel save(LowcodeModel model);

	LowcodeModel update(String modelCode, LowcodeModel model);

	void delete(String modelCode);

	LowcodeModel importFromEntity(String entityClassName);

	List<String> getAvailableEntities();

	void reorderProperties(LowcodeModel model, List<LowcodeProperty> properties);

	Class<?> getEntityClass(String modelCode);
}
