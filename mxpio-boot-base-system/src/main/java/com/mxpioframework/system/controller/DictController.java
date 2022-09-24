package com.mxpioframework.system.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.policy.CrudContext;
import com.mxpioframework.jpa.policy.impl.SmartCrudPolicyAdapter;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.CriteriaUtils;
import com.mxpioframework.security.entity.Dict;
import com.mxpioframework.security.entity.DictItem;
import com.mxpioframework.system.service.impl.DictServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "DictController", description = "字典接口")
@RestController("mxpio.system.dictController")
@RequestMapping("/sys/dict/")
public class DictController {
	
	@Autowired
	private DictServiceImpl dictSerivce;

	@GetMapping("tree/list")
	@Operation(summary = "字典树列表", description = "获取字典树列表", method = "GET")
	public Result<List<Dict>> list(String criteria) throws UnsupportedEncodingException {
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		
		List<Dict> dicts = dictSerivce.listWithItems(c);
		return Result.OK(dicts);
	}
	
	@GetMapping("{code}/list")
	@Operation(summary = "字典列表", description = "根据code获取字典列表", method = "GET")
	public Result<List<DictItem>> items(@PathVariable(name = "code", required = true) String code,
			@RequestParam(value = "criteria", required = false) String criteria) {
		Criteria c = CriteriaUtils.json2Criteria(criteria);		
		List<DictItem> items = dictSerivce.getItemsByCode(code, c);
		return Result.OK(items);
	}
	
	@GetMapping("{code}/default")
	@Operation(summary = "默认字典项", description = "根据code获取默认字典项", method = "GET")
	public Result<DictItem> getDefaultItem(@PathVariable(name = "code", required = true) String code) {
		
		DictItem item = dictSerivce.getDefaultItemByCode(code);
		return Result.OK(item);
	}
	
	@GetMapping("{code}/default/value")
	@Operation(summary = "默认字典值", description = "根据code获取默认字典值", method = "GET")
	public Result<String> getDefaultItemValue(@PathVariable(name = "code", required = true) String code) {
		
		String itemValue = dictSerivce.getDefaultValueByCode(code);
		return Result.OK(itemValue);
	}
	
	@GetMapping("{code}/default/text")
	@Operation(summary = "默认字典文本", description = "根据code获取默认字典文本", method = "GET")
	public Result<String> getDefaultItemText(@PathVariable(name = "code", required = true) String code) {
		
		String itemText = dictSerivce.getDefaultTextByCode(code);
		return Result.OK(itemText);
	}
	
	@GetMapping("tree/page")
	@Operation(summary = "字典列表", description = "获取字典列表(分页)", method = "GET")
	public Result<Page<Dict>> page(String criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws UnsupportedEncodingException {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		Page<Dict> page = dictSerivce.listPageWithItems(c, pageAble);
		return Result.OK(page);
	}
	
	@GetMapping("tree/{code}")
	@Operation(summary = "根据code获取字典", description = "根据code获取字典", method = "GET")
	public Result<Dict> getByCode(@PathVariable(name = "code", required = true) String code) {
		try{
			Dict dict = dictSerivce.getByCode(code);
			return Result.OK(dict);
		}catch (Exception e) {
			return Result.OK(null);
		}
	}
	
	@PostMapping("add")
	@Operation(summary = "新增字典", description = "新增字典", method = "POST")
	public Result<Dict> add(@RequestBody Dict dict) {
		dictSerivce.save(dict, new SmartCrudPolicyAdapter(){
			@Override
			public boolean beforeInsert(CrudContext context) {
				Object entity = context.getEntity();
				if(entity instanceof DictItem){
					Dict parent = context.getParent();
					((DictItem) entity).setDictId(parent.getId());
				}
				return true;
			}
		});
		return Result.OK(dict);
	}
	
	@PostMapping("{code}/add")
	@Operation(summary = "新增字典项", description = "新增字典项", method = "POST")
	public Result<DictItem> addItem(@PathVariable(name = "code", required = true) String code
			,@RequestBody DictItem item) {
		Dict dict = dictSerivce.getByCode(code);
		item.setDictId(dict.getId());
		dictSerivce.saveItem(item);
		return Result.OK(item);
	}
	
	@PutMapping("{code}/edit")
	@Operation(summary = "更新字典项", description = "更新字典项", method = "PUT")
	public Result<DictItem> editItem(@PathVariable(name = "code", required = true) String code
			,@RequestBody DictItem item) {
		dictSerivce.updateItem(item);
		return Result.OK(item);
	}
	
	@PutMapping("edit")
	@Operation(summary = "编辑字典", description = "编辑字典（全量）", method = "PUT")
	public Result<Dict> edit(@RequestBody Dict dict) {
		dictSerivce.update(dict, new SmartCrudPolicyAdapter(){
			@Override
			public boolean beforeUpdate(CrudContext context) {
				Object entity = context.getEntity();
				if(entity instanceof DictItem){
					Dict parent = context.getParent();
					((DictItem) entity).setDictId(parent.getId());
				}
				return true;
			}
		});
		return Result.OK(dict);
	}
	
	@DeleteMapping("remove/{id}")
	@Operation(summary = "删除字典", description = "删除字典", method = "DELETE")
	public Result<Dict> remove(@PathVariable(name = "id", required = true) String id) {
		String ids[] = id.split(",");
		for(String key : ids){
			dictSerivce.delete(Dict.class, key, new SmartCrudPolicyAdapter() {
				@Override
				public boolean beforeDelete(CrudContext context) {
					Object o = context.getEntity();
					if(o instanceof Dict) {
						try{
							JpaUtil.lind(DictItem.class).equal("dictId", ((Dict) o).getId()).delete();
						}catch (Exception e) {
							return false;
						}
					}
					return true;
				}
			});
		}
		return Result.OK();
	}
	
	@DeleteMapping("remove/item/{id}")
	@Operation(summary = "删除字典项", description = "删除字典项", method = "DELETE")
	public Result<DictItem> removeItem(@PathVariable(name = "id", required = true) String id) {
		String ids[] = id.split(",");
		for(String key : ids){
			dictSerivce.deleteItem(key);
		}
		return Result.OK();
	}
	
}
