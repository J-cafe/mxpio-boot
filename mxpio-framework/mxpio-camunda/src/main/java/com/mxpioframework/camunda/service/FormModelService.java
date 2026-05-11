package com.mxpioframework.camunda.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.camunda.entity.FormModel;
import com.mxpioframework.camunda.entity.FormModelDef;
import com.mxpioframework.jpa.query.Criteria;

public interface FormModelService {

	/**
	 * 根据条件查询表单模型列表
	 *
	 * @param criteria 查询条件
	 * @return 返回符合查询条件的表单模型列表
	 */
	List<FormModel> list(Criteria criteria);

	/**
	 * 根据条件查询表单模型列表
	 *
	 * @param criteria 查询条件
	 * @return 返回符合查询条件的表单模型列表
	 */
	Page<FormModel> listPage(Pageable page, Criteria criteria);

	/**
	 * 根据表单模型编码部署表单模型
	 *
	 * @param code 表单模型编码
	 * @return 返回部署的表单模型
	 */
	FormModel deploy(String code);

	/**
	 * 保存表单模型
	 *
	 * @param formModel 表单模型
	 */
	void save(FormModel formModel);

	/**
	 * 更新表单模型
	 *
	 * @param formModel 表单模型
	 */
	void update(FormModel formModel);

	/**
	 * 根据表单模型定义的key获取表单模型定义
	 *
	 * @param key 表单模型定义的key
	 * @return 返回符合查询条件的表单模型定义
	 */
	FormModelDef getFormModelDefByKey(String key);

}
