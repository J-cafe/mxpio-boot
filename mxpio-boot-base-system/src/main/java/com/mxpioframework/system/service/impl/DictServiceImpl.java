package com.mxpioframework.system.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.system.entity.Dict;
import com.mxpioframework.system.entity.DictItem;
import com.mxpioframework.system.service.DictService;

@Service
public class DictServiceImpl extends BaseServiceImpl<Dict> implements DictService {

	@Override
	@Transactional(readOnly = true)
	public Dict getByCode(String code) {
		return JpaUtil.linq(Dict.class).collect("dictId", DictItem.class, "id").equal("dictCode", code).findOne();
	}

	@Override
	@Transactional(readOnly = true)
	public Dict getById(String id) {
		return JpaUtil.linq(Dict.class).collect("dictId", DictItem.class, "id").idEqual(id).findOne();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Dict> listPageWithItems(Criteria criteria, Pageable pageAble) {
		return JpaUtil.linq(Dict.class).collect("dictId", DictItem.class, "id").where(criteria).paging(pageAble);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Dict> listWithItems(Criteria c) {
		return JpaUtil.linq(Dict.class).collect("dictId", DictItem.class, "id").where(c).list();
	}

	@Override
	public DictItem getDefaultItemByCode(String code) {
		return JpaUtil.linq(DictItem.class).exists(Dict.class).equal("dictCode", code)
				.equalProperty("dictDefaultValue", "itemValue").equalProperty("id", "dictId").end().findOne();
	}

	@Override
	public String getDefaultValueByCode(String code) {
		return getDefaultItemByCode(code).getItemValue();
	}

	@Override
	public String getDefaultTextByCode(String code) {
		return getDefaultItemByCode(code).getItemText();
	}

	@Override
	@Transactional(readOnly = true)
	public List<DictItem> getItemsByCode(String code) {
		return JpaUtil.linq(DictItem.class).exists(Dict.class).equal("dictCode", code)
				.equalProperty("id", "dictId").end().asc("itemSort").list();
	}

	@Override
	@Transactional(readOnly = true)
	public String getTextByCode(String code, String value) {
		DictItem item = JpaUtil.linq(DictItem.class).equal("itemValue", value).exists(Dict.class).equal("dictCode", code).equalProperty("id", "dictId").end().findOne();
		return item.getItemValue();
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteItem(String id) {
		DictItem item = JpaUtil.linq(DictItem.class).idEqual(id).findOne();
		JpaUtil.delete(item);
	}

	@Override
	@Transactional(readOnly = false)
	public void saveItem(DictItem item) {
		JpaUtil.save(item);
	}

}
