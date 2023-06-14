package com.mxpioframework.camunda.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.camunda.entity.FormModel;
import com.mxpioframework.camunda.entity.FormModelDef;
import com.mxpioframework.jpa.query.Criteria;

public interface FormModelService {

	List<FormModel> list(Criteria criteria);

	Page<FormModel> listPage(Pageable page, Criteria criteria);

	FormModel deploy(String code);

	void save(FormModel formModel);

	void update(FormModel formModel);

	FormModelDef getFormModelDefByKey(String key);

}
