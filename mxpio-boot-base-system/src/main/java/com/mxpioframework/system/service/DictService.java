package com.mxpioframework.system.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.system.entity.Dict;
import com.mxpioframework.system.entity.DictItem;

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
	 * @param code
	 * @return
	 */
	public DictItem getDefaultItemByCode(String code);
	
	/**
	 * 根据code获取字典值
	 * @param code
	 * @return
	 */
	public String getDefaultValueByCode(String code);
	
	/**
	 * 根据code获取字典文本
	 * @param code
	 * @return
	 */
	public String getDefaultTextByCode(String code);
	
	/**
	 * 根据code获取字典列表
	 * @param code
	 * @return
	 */
	public List<DictItem> getItemsByCode(String code);

}