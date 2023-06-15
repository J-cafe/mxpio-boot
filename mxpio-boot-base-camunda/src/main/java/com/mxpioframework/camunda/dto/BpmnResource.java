package com.mxpioframework.camunda.dto;

import com.mxpioframework.camunda.entity.FormModelDef;

public interface BpmnResource {

	void setStartFormModelDef(FormModelDef formModelDefByKey);

	void setSource(String source);

}
