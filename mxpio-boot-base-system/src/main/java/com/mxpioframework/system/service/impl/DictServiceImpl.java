package com.mxpioframework.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mxpioframework.system.service.DictCacheService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.common.util.BeanReflectionUtils;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.entity.BaseEntity;
import com.mxpioframework.security.entity.Dict;
import com.mxpioframework.security.entity.DictItem;
import com.mxpioframework.system.service.DictService;

import javax.annotation.Resource;

@Service("mxpio.system.dictService")
public class DictServiceImpl extends BaseServiceImpl<Dict> implements DictService {

	@Resource(name = "mxpio.system.dictCacheService")
	private DictCacheService cacheService;

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
	public List<DictItem> getItemsByCode(String code, Criteria c) {
		return JpaUtil.linq(DictItem.class).exists(Dict.class).equal("dictCode", code)
				.equalProperty("id", "dictId").end().where(c).asc("itemSort").list();
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, String> getDictMappingByCode(String code) {
	    return getItemsByCode(code, Criteria.create()).stream().collect(Collectors.toMap(DictItem::getItemValue, DictItem::getItemText,
	      (existing, replacement) -> existing));
	}

	@Override
	@Transactional(readOnly = true)
	public DictItem getItemByCode(String code, String value) {
        return JpaUtil.linq(DictItem.class).equal("itemValue", value).exists(Dict.class).equal("dictCode", code).equalProperty("id", "dictId").end().findOne();
	}

	@Override
	@Transactional(readOnly = true)
	public String getTextByCode(String code, String value) {
		DictItem item = getItemByCode(code, value);
		return item.getItemValue();
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteItem(String id) {
		DictItem item = JpaUtil.linq(DictItem.class).idEqual(id).findOne();
		JpaUtil.delete(item);
		Dict dict = JpaUtil.getOne(Dict.class, item.getDictId());
		Map<String, String> dictMapping = this.getDictMappingByCode(dict.getDictCode());
		cacheService.put("DictCache:" + dict.getDictCode(), dictMapping);//刷新缓存值
	}

	@Override
	@Transactional(readOnly = false)
	public void saveItem(DictItem item,Dict dict) {
		JpaUtil.save(item);
		String code = dict.getDictCode();
		Map<String, String> dictMapping = this.getDictMappingByCode(code);
		cacheService.put("DictCache:" + code, dictMapping);//刷新缓存值
	}

	@Override
	@Transactional(readOnly = false)
	public void updateItem(DictItem item,String code) {
		JpaUtil.update(item);
		Map<String, String> dictMapping = this.getDictMappingByCode(code);
		cacheService.put("DictCache:" + code, dictMapping);//刷新缓存值
	}

	@Override
	@Transactional(readOnly = false)
	public String getEntityDictText(String code, Class<? extends BaseEntity> clazz, String dicText, String value) {
		try{
			Object entity = JpaUtil.linq(clazz).equal(code, value).findOne();
			Object result = BeanReflectionUtils.getProperty(entity, dicText);
			return result == null ? null : result.toString();
		}catch (Exception e) {
			return null;
		}

	}

	/**
	 * 对于动态数据字典，根据value反查text
	 * @param dicCode 字典编码
	 * @param clazz 来源实体
	 * @param dicText 字典文本字段
	 * @param textValue 字段文本值
	 * @return 文本值
	 */
	@Override
	@Transactional
	public String getEntityDictValueByText(String dicCode, Class<? extends BaseEntity> clazz, String dicText, String textValue){
		try{
			List<Object> entityList = JpaUtil.linq(clazz).equal(dicText, textValue).list();
			if(CollectionUtils.isEmpty(entityList)){
				return null;
			}
			Object entity = entityList.get(0);
			Object result = BeanReflectionUtils.getProperty(entity, dicCode);
			return result == null ? null : result.toString();
		}catch (Exception e) {
			return null;
		}
	}

}
