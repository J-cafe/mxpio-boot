package com.mxpioframework.lowcode.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.lowcode.entity.LowcodeModel;
import com.mxpioframework.lowcode.service.LowcodeModelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/lowcode")
@Tag(name = "DynamicData", description = "低代码动态数据CRUD（对标Dorado DataProvider/DataResolver）")
public class DynamicDataController {

	@Autowired
	private LowcodeModelService modelService;

	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping("/{modelCode}/list")
	@Operation(summary = "动态列表查询（接受Criteria JSON参数）")
	public List<?> list(@PathVariable String modelCode,
			@RequestParam(required = false) String criteriaJson) throws Exception {
		Class<?> entityClass = modelService.getEntityClass(modelCode);
		Criteria criteria = criteriaJson != null
				? objectMapper.readValue(criteriaJson, Criteria.class)
				: Criteria.create();
		return JpaUtil.linq(entityClass).where(criteria).list();
	}

	@GetMapping("/{modelCode}/page")
	@Operation(summary = "动态分页查询")
	public Page<?> page(@PathVariable String modelCode,
			@RequestParam(required = false) String criteriaJson,
			Pageable pageable) throws Exception {
		Class<?> entityClass = modelService.getEntityClass(modelCode);
		Criteria criteria = criteriaJson != null
				? objectMapper.readValue(criteriaJson, Criteria.class)
				: Criteria.create();
		return JpaUtil.linq(entityClass).where(criteria).paging(pageable);
	}

	@GetMapping("/{modelCode}/{id}")
	@Operation(summary = "根据ID获取单条记录")
	public Object getById(@PathVariable String modelCode, @PathVariable String id) {
		Class<?> entityClass = modelService.getEntityClass(modelCode);
		return JpaUtil.linq(entityClass).equal("id", id).findOne();
	}

	@PostMapping("/{modelCode}")
	@Operation(summary = "新增数据")
	public Object insert(@PathVariable String modelCode, @RequestBody Map<String, Object> body)
			throws Exception {
		Class<?> entityClass = modelService.getEntityClass(modelCode);
		Object entity = objectMapper.convertValue(body, entityClass);
		JpaUtil.persist(entity);
		return entity;
	}

	@PutMapping("/{modelCode}/{id}")
	@Operation(summary = "更新数据")
	public Object update(@PathVariable String modelCode, @PathVariable String id,
			@RequestBody Map<String, Object> body) throws Exception {
		Class<?> entityClass = modelService.getEntityClass(modelCode);
		Object existing = JpaUtil.linq(entityClass).equal("id", id).findOne();
		if (existing == null) {
			throw new IllegalArgumentException("记录不存在: " + id);
		}
		objectMapper.updateValue(existing, body);
		JpaUtil.merge(existing);
		return existing;
	}

	@DeleteMapping("/{modelCode}/{id}")
	@Operation(summary = "删除数据")
	public void delete(@PathVariable String modelCode, @PathVariable String id) {
		Class<?> entityClass = modelService.getEntityClass(modelCode);
		Object entity = JpaUtil.linq(entityClass).equal("id", id).findOne();
		if (entity != null) {
			JpaUtil.delete(entity);
		}
	}

	@PostMapping("/{modelCode}/batch")
	@Operation(summary = "批量保存（DataSet提交，对标Dorado DataResolver）")
	public void batchSave(@PathVariable String modelCode,
			@RequestBody BatchSaveRequest batch) throws Exception {
		Class<?> entityClass = modelService.getEntityClass(modelCode);

		if (batch.getToInsert() != null) {
			for (Map<String, Object> item : batch.getToInsert()) {
				Object entity = objectMapper.convertValue(item, entityClass);
				JpaUtil.persist(entity);
			}
		}

		if (batch.getToUpdate() != null) {
			for (Map<String, Object> item : batch.getToUpdate()) {
				Object id = item.get("id");
				if (id == null) continue;
				Object existing = JpaUtil.linq(entityClass).equal("id", id).findOne();
				if (existing != null) {
					objectMapper.updateValue(existing, item);
					JpaUtil.merge(existing);
				}
			}
		}

		if (batch.getToDelete() != null) {
			for (String id : batch.getToDelete()) {
				Object entity = JpaUtil.linq(entityClass).equal("id", id).findOne();
				if (entity != null) {
					JpaUtil.delete(entity);
				}
			}
		}
	}

	public static class BatchSaveRequest {
		private List<Map<String, Object>> toInsert;
		private List<Map<String, Object>> toUpdate;
		private List<String> toDelete;

		public List<Map<String, Object>> getToInsert() { return toInsert; }
		public void setToInsert(List<Map<String, Object>> toInsert) { this.toInsert = toInsert; }
		public List<Map<String, Object>> getToUpdate() { return toUpdate; }
		public void setToUpdate(List<Map<String, Object>> toUpdate) { this.toUpdate = toUpdate; }
		public List<String> getToDelete() { return toDelete; }
		public void setToDelete(List<String> toDelete) { this.toDelete = toDelete; }
	}
}
