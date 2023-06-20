package com.mxpioframework.camunda.vo;

import com.mxpioframework.camunda.entity.FormModelDef;

public interface BpmnResource {

	void setStartFormModelDef(FormModelDef formModelDefByKey);

	void setSource(String source);

}
