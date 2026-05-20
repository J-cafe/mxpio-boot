package com.mxpioframework.lowcode.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.lowcode.entity.LowcodeModel;
import com.mxpioframework.lowcode.entity.LowcodeProperty;
import com.mxpioframework.lowcode.entity.LowcodeProperty.ShowType;
import com.mxpioframework.lowcode.service.LowcodeModelService;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Service("mxpio.lowcode.modelService")
@Transactional
public class LowcodeModelServiceImpl implements LowcodeModelService {

	private final Map<String, LowcodeModel> modelCache = new ConcurrentHashMap<>();
	private final Map<String, Class<?>> entityClassCache = new ConcurrentHashMap<>();

	@Override
	@Transactional(readOnly = true)
	public LowcodeModel getByCode(String modelCode) {
		return modelCache.computeIfAbsent(modelCode, code -> {
			LowcodeModel model = JpaUtil.linq(LowcodeModel.class)
					.equal("modelCode", code)
					.findOne();
			if (model != null) {
				model.getProperties().size();
			}
			return model;
		});
	}

	@Override
	public Class<?> getEntityClass(String modelCode) {
		return entityClassCache.computeIfAbsent(modelCode, code -> {
			LowcodeModel model = getByCode(code);
			if (model == null) {
				throw new IllegalArgumentException("模型不存在: " + code);
			}
			try {
				return Class.forName(model.getEntityClass());
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException("实体类未找到: " + model.getEntityClass(), e);
			}
		});
	}

	private void invalidateCache(String modelCode) {
		modelCache.remove(modelCode);
		entityClassCache.remove(modelCode);
	}

	@Override
	@Transactional(readOnly = true)
	public List<LowcodeModel> listAll() {
		return JpaUtil.linq(LowcodeModel.class).list();
	}

	@Override
	public LowcodeModel save(LowcodeModel model) {
		Assert.hasText(model.getModelCode(), "模型编码不能为空");
		if (model.getProperties() != null) {
			for (LowcodeProperty prop : model.getProperties()) {
				prop.setModel(model);
			}
		}
		JpaUtil.persistAndFlush(model);
		invalidateCache(model.getModelCode());
		return model;
	}

	@Override
	public LowcodeModel update(String modelCode, LowcodeModel updated) {
		LowcodeModel existing = getByCode(modelCode);
		Assert.notNull(existing, "模型不存在: " + modelCode);

		existing.setModelName(updated.getModelName());
		existing.setDescription(updated.getDescription());

		if (updated.getProperties() != null) {
			existing.getProperties().clear();
			for (LowcodeProperty prop : updated.getProperties()) {
				prop.setModel(existing);
				existing.getProperties().add(prop);
			}
		}

		JpaUtil.persistAndFlush(existing);
		invalidateCache(modelCode);
		return existing;
	}

	@Override
	public void delete(String modelCode) {
		LowcodeModel model = getByCode(modelCode);
		if (model != null) {
			JpaUtil.delete(model);
			invalidateCache(modelCode);
		}
	}

	@Override
	public LowcodeModel importFromEntity(String entityClassName) {
		try {
			Class<?> entityClass = Class.forName(entityClassName);
			Assert.isTrue(entityClass.isAnnotationPresent(Entity.class),
					"类 " + entityClassName + " 不是JPA实体（缺少@Entity注解）");

			LowcodeModel model = new LowcodeModel();
			model.setEntityClass(entityClassName);

			Table tableAnn = entityClass.getAnnotation(Table.class);
			String tableName = tableAnn != null ? tableAnn.name() : toUnderscore(entityClass.getSimpleName());
			model.setTableName(tableName);
			model.setModelCode(tableName);

			String schemaDesc = entityClass.getAnnotation(Schema.class) != null
					? entityClass.getAnnotation(Schema.class).description() : null;
			model.setModelName(schemaDesc != null ? schemaDesc : entityClass.getSimpleName());

			List<LowcodeProperty> props = new ArrayList<>();
			int sortOrder = 0;
			for (Field field : entityClass.getDeclaredFields()) {
				if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) continue;
				if (field.isAnnotationPresent(org.springframework.data.annotation.Transient.class)) continue;

				LowcodeProperty prop = new LowcodeProperty();
				prop.setPropertyCode(field.getName());
				prop.setSortOrder(sortOrder++);

				Column colAnn = field.getAnnotation(Column.class);
				if (colAnn != null) {
					prop.setColumnName(StringUtils.hasText(colAnn.name()) ? colAnn.name() : toUnderscore(field.getName()));
					prop.setColumnLength(colAnn.length() > 0 ? colAnn.length() : 255);
					prop.setNullableFlag(colAnn.nullable());
				} else {
					prop.setColumnName(toUnderscore(field.getName()));
				}

				Schema fieldSchema = field.getAnnotation(Schema.class);
				if (fieldSchema != null && StringUtils.hasText(fieldSchema.description())) {
					prop.setPropertyName(fieldSchema.description());
				} else {
					prop.setPropertyName(field.getName());
				}

				prop.setColumnType(mapJavaType(field.getType()));
				prop.setShowType(inferShowType(field));
				prop.setPkFlag(field.isAnnotationPresent(Id.class));
				prop.setQueryableFlag(true);
				prop.setListableFlag(true);
				prop.setEditableFlag(!field.isAnnotationPresent(Id.class));
				prop.setRequiredFlag(colAnn != null && !colAnn.nullable());

				if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToMany.class)) {
					Class<?> refType = field.isAnnotationPresent(ManyToOne.class)
							? field.getAnnotation(ManyToOne.class).targetEntity()
							: Object.class;
					if (refType != Object.class && refType.isAnnotationPresent(Entity.class)) {
						Table refTable = refType.getAnnotation(Table.class);
						prop.setRefModelCode(refTable != null ? refTable.name() : null);
					}
				}

				props.add(prop);
			}
			model.setProperties(props);
			return model;
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("实体类未找到: " + entityClassName, e);
		}
	}

	@Override
	public List<String> getAvailableEntities() {
		List<String> entities = new ArrayList<>();
		ClassPathScanningCandidateComponentProvider scanner =
				new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
		for (var bd : scanner.findCandidateComponents("com.mxpioframework")) {
			entities.add(bd.getBeanClassName());
		}
		return entities;
	}

	@Override
	public void reorderProperties(LowcodeModel model, List<LowcodeProperty> properties) {
		model.getProperties().clear();
		for (int i = 0; i < properties.size(); i++) {
			LowcodeProperty prop = properties.get(i);
			prop.setSortOrder(i);
			prop.setModel(model);
			model.getProperties().add(prop);
		}
		JpaUtil.persistAndFlush(model);
	}

	private String mapJavaType(Class<?> javaType) {
		if (javaType == String.class || javaType == Character.class || javaType == char.class) return "String";
		if (javaType == Long.class || javaType == long.class) return "Long";
		if (javaType == Integer.class || javaType == int.class) return "Integer";
		if (javaType == Boolean.class || javaType == boolean.class) return "Boolean";
		if (javaType == Double.class || javaType == double.class || javaType == Float.class || javaType == float.class) return "Decimal";
		if (java.util.Date.class.isAssignableFrom(javaType) || java.time.temporal.Temporal.class.isAssignableFrom(javaType)) return "Date";
		if (javaType == java.math.BigDecimal.class) return "Decimal";
		return "String";
	}

	private ShowType inferShowType(Field field) {
		Class<?> type = field.getType();
		if (type == Boolean.class || type == boolean.class) return ShowType.CHECKBOX;
		if (java.util.Date.class.isAssignableFrom(type) || java.time.temporal.Temporal.class.isAssignableFrom(type)) {
			return type == java.sql.Date.class || type == java.time.LocalDate.class ? ShowType.DATE : ShowType.DATETIME;
		}
		if (Number.class.isAssignableFrom(type) || type == int.class || type == long.class || type == double.class) return ShowType.NUMBER;
		return ShowType.TEXT;
	}

	private static String toUnderscore(String camelCase) {
		return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
	}
}
