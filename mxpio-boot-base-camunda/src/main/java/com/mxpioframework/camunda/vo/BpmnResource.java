package com.mxpioframework.camunda.vo;

import com.mxpioframework.camunda.entity.FormModelDef;

public interface BpmnResource {

	/**
	 * 设置流程的起始表单模型定义
	 *
	 * @param formModelDefByKey 表单模型定义
	 */
	void setStartFormModelDef(FormModelDef formModelDefByKey);

	/**
	 * 设置流程的资源文件路径
	 *
	 * @param source 资源文件路径
	 */
	void setSource(String source);

}
