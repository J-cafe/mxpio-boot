package com.mxpioframework.camunda.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.camunda.entity.FormModel;
import com.mxpioframework.camunda.entity.FormModelDef;
import com.mxpioframework.camunda.enums.BpmnEnums;
import com.mxpioframework.camunda.service.FormModelService;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;

@Service("mxpio.camunda.formModelService")
public class FormModelServiceImpl implements FormModelService {

	@Override
	@Transactional(readOnly = true)
	public List<FormModel> list(Criteria criteria) {
		return JpaUtil.linq(FormModel.class).where(criteria).list();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<FormModel> listPage(Pageable page, Criteria criteria) {
		return JpaUtil.linq(FormModel.class).where(criteria).paging(page);
	}

	@Override
	@Transactional
	public FormModel deploy(String code) {
		FormModel formModel = JpaUtil.linq(FormModel.class).idEqual(code).findOne();
		Long count = JpaUtil.linq(FormModelDef.class).equal("code", formModel.getCode()).count();
		FormModelDef def = new FormModelDef();
		def.setCode(formModel.getCode());
		def.setModel(formModel.getModel());
		def.setName(formModel.getName());
		def.setVersion((int) (count + 1));
		def.setKey(formModel.getCode() + ":" + def.getVersion());
		JpaUtil.save(def);
		formModel.setLastDefKey(def.getKey());
		formModel.setLastDefVersion(def.getVersion());
		formModel.setStatus(BpmnEnums.DeployStatusEnums.DEPLOY.getCode());
		this.update(formModel);
		return formModel;
	}

	@Override
	@Transactional
	public void save(FormModel formModel) {
		JpaUtil.save(formModel);
	}

	@Override
	@Transactional
	public void update(FormModel formModel) {
		JpaUtil.update(formModel);
	}

	@Override
	@Transactional(readOnly = true)
	public FormModelDef getFormModelDefByKey(String key) {
		return JpaUtil.linq(FormModelDef.class).idEqual(key).findOne();
	}

}
