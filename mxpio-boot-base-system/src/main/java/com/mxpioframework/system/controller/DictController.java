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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "DictController", tags = { "字典接口" })
@RestController
@RequestMapping("/sys/dict/")
public class DictController {
	
	@Autowired
	private DictServiceImpl dictSerivce;

	@GetMapping("tree/list")
	@ApiOperation(value = "字典树列表", notes = "获取字典树列表", httpMethod = "GET")
	public Result<List<Dict>> list(String criteria) throws UnsupportedEncodingException {
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		
		List<Dict> dicts = dictSerivce.listWithItems(c);
		return Result.OK(dicts);
	}
	
	@GetMapping("{code}/list")
	@ApiOperation(value = "字典列表", notes = "根据code获取字典列表", httpMethod = "GET")
	public Result<List<DictItem>> items(@PathVariable(name = "code", required = true) String code) {
		
		List<DictItem> items = dictSerivce.getItemsByCode(code);
		return Result.OK(items);
	}
	
	@GetMapping("{code}/default")
	@ApiOperation(value = "默认字典项", notes = "根据code获取默认字典项", httpMethod = "GET")
	public Result<DictItem> getDefaultItem(@PathVariable(name = "code", required = true) String code) {
		
		DictItem item = dictSerivce.getDefaultItemByCode(code);
		return Result.OK(item);
	}
	
	@GetMapping("{code}/default/value")
	@ApiOperation(value = "默认字典值", notes = "根据code获取默认字典值", httpMethod = "GET")
	public Result<String> getDefaultItemValue(@PathVariable(name = "code", required = true) String code) {
		
		String itemValue = dictSerivce.getDefaultValueByCode(code);
		return Result.OK(itemValue);
	}
	
	@GetMapping("{code}/default/text")
	@ApiOperation(value = "默认字典文本", notes = "根据code获取默认字典文本", httpMethod = "GET")
	public Result<String> getDefaultItemText(@PathVariable(name = "code", required = true) String code) {
		
		String itemText = dictSerivce.getDefaultTextByCode(code);
		return Result.OK(itemText);
	}
	
	@GetMapping("tree/page")
	@ApiOperation(value = "字典列表", notes = "获取字典列表(分页)", httpMethod = "GET")
	public Result<Page<Dict>> page(String criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws UnsupportedEncodingException {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		Page<Dict> page = dictSerivce.listPageWithItems(c, pageAble);
		return Result.OK(page);
	}
	
	@GetMapping("tree/{code}")
	@ApiOperation(value = "根据code获取字典", notes = "根据code获取字典", httpMethod = "GET")
	public Result<Dict> getByCode(@PathVariable(name = "code", required = true) String code) {
		Dict dict = dictSerivce.getByCode(code);
		return Result.OK(dict);
	}
	
	@PostMapping("add")
	@ApiOperation(value = "新增字典", notes = "新增字典", httpMethod = "POST")
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
	@ApiOperation(value = "新增字典项", notes = "新增字典项", httpMethod = "POST")
	public Result<DictItem> addItem(@PathVariable(name = "code", required = true) String code
			,@RequestBody DictItem item) {
		Dict dict = dictSerivce.getByCode(code);
		item.setDictId(dict.getId());
		dictSerivce.saveItem(item);
		return Result.OK(item);
	}
	
	@PutMapping("{code}/edit")
	@ApiOperation(value = "更新字典项", notes = "更新字典项", httpMethod = "PUT")
	public Result<DictItem> editItem(@PathVariable(name = "code", required = true) String code
			,@RequestBody DictItem item) {
		dictSerivce.updateItem(item);
		return Result.OK(item);
	}
	
	@PutMapping("edit")
	@ApiOperation(value = "编辑字典", notes = "编辑字典（全量）", httpMethod = "PUT")
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
	@ApiOperation(value = "删除字典", notes = "删除字典", httpMethod = "DELETE")
	public Result<Dict> remove(@PathVariable(name = "id", required = true) String id) {
		String ids[] = id.split(";");
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
	@ApiOperation(value = "删除字典项", notes = "删除字典项", httpMethod = "DELETE")
	public Result<DictItem> removeItem(@PathVariable(name = "id", required = true) String id) {
		String ids[] = id.split(";");
		for(String key : ids){
			dictSerivce.deleteItem(key);
		}
		return Result.OK();
	}
	
}
