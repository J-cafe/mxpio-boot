package com.mxpioframework.system.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.entity.Dict;
import com.mxpioframework.security.entity.DictItem;

public interface DictService {

	/**
	 * 根据code获取字典
	 * @param code
	 * @return
	 */
	public Dict getByCode(String code);

	/**
	 * 根据Id获取字典
	 * @param id
	 * @return
	 */
	public Dict getById(String id);

	/**
	 * 获取分页字典列表
	 * @param criteria
	 * @param pageAble
	 * @return
	 */
	public Page<Dict> listPageWithItems(Criteria criteria, Pageable pageAble);

	/**
	 * 获取字典列表
	 * @param c
	 * @return
	 */
	public List<Dict> listWithItems(Criteria c);

	/**
	 * 根据code获取字典项
	 * @param code 字典code
	 * @return
	 */
	public DictItem getDefaultItemByCode(String code);

	/**
	 * 根据code获取字典值
	 * @param code 字典code
	 * @return
	 */
	public String getDefaultValueByCode(String code);

	/**
	 * 根据code获取字典文本
	 * @param code 字典code
	 * @return
	 */
	public String getDefaultTextByCode(String code);

	/**
	 * 根据code获取字典列表
	 * @param code 字典code
	 * @return
	 */
	public List<DictItem> getItemsByCode(String code);

	/**
	 * 根据字典code以及字典项的值获取字典项
	 * @param code
	 * @param value
	 * @return
	 */
	public DictItem getItemByCode(String code, String value);

	/**
	 * 根据字典code以及字典项的值获取字典项的文本
	 * @param code 字典code
	 * @param value 字典项值
	 * @return 字典项文本
	 */
	public String getTextByCode(String code, String value);

	/**
	 * 根据字典ID删除字典项
	 * @param id
	 */
	public void deleteItem(String id);

	/**
	 * 保存字典项
	 * @param item
	 */
	public void saveItem(DictItem item);

	public void updateItem(DictItem item);

	/**
	 * 表字典翻译
	 * @param code
	 * @param clazz
	 * @param dicText
	 * @param value
	 * @return
	 */
	public String getEntityDictText(String code, Class<? extends BaseEntity> clazz, String dicText, String value);

    Map<String, String> getDictMappingByCode(String code);

}
