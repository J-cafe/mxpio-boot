package com.mxpioframework.jpa.policy.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.mxpioframework.common.util.BeanReflectionUtils;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.MxpioEntity;
import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.jpa.policy.CrudContext;
import com.mxpioframework.jpa.policy.CrudPolicy;
import com.mxpioframework.jpa.policy.GeneratorPolicy;

public class DirtyTreeCrudPolicy implements CrudPolicy {
	private CrudPolicy crudPolicy;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void apply(CrudContext context) {
		Object obj = context.getEntity();
		List<Field> fields = null;
		List<Map<String, Object>> generatorPolicies = null;
		Collection target = null;
		if (obj != null) {
			if (obj instanceof Collection) {
				target = (Collection) obj;
			} else {
				target = new ArrayList();
				target.add(obj);
			}
			CrudType crudType = context.getCrudType();
			Map<CrudType,List<Map<String, Object>>> generatorPoliciesMap = new HashMap<>();
			for (Object entity : target) {
				if(entity instanceof MxpioEntity) {
					CrudType entityStatus = ((MxpioEntity) entity).getCrudType();
					if(entityStatus != null) {
						context.setCrudType(entityStatus);
					}else {
						context.setCrudType(crudType);
					}
					context.setSaveTransient(((MxpioEntity) entity).isSaveTransient());
				}
				context.setEntity(entity);
				if (fields == null) {
					fields = getPersistentFields(context);
				}
				generatorPolicies = generatorPoliciesMap.get(context.getCrudType());
				if (generatorPolicies == null) {
					try {
						generatorPolicies = getNeedGeneratorFields(entity, context.getCrudType());
						generatorPoliciesMap.put(context.getCrudType(), generatorPolicies);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					}
				}
				applyPersistentEntity(context, generatorPolicies);
				crudPolicy.apply(context);
				if(context.isSaveTransient()) {
					applyPersistentPropertyValue(context, fields);
				}
			}
		}
	}

	protected void applyPersistentEntity(CrudContext context, List<Map<String, Object>> generatorPolicies) {
		for (Map<String, Object> map : generatorPolicies) {
			Field field = (Field) map.get("field");
			GeneratorPolicy policy = (GeneratorPolicy) map.get("policy");
			policy.apply(context.getEntity(), field.getName());
		}

	}

	protected void applyPersistentPropertyValue(CrudContext context, List<Field> fields) {
		Object entity = context.getEntity();
		Object parent = context.getParent();
		CrudType entityCrudType = context.getCrudType();
		context.setParent(entity);
		for (Field field : fields) {
			Object value = BeanReflectionUtils.getProperty(entity, field.getName());
			CrudType crudType = (CrudType) BeanReflectionUtils.getProperty(entity, "crudType");
			context.setEntity(value);
			if(crudType != null) {
				context.setCrudType(crudType);
			}
			apply(context);
		}
		context.setCrudType(entityCrudType);
		context.setParent(parent);
		context.setEntity(entity);
	}

	@SuppressWarnings("rawtypes")
	protected List<Field> getPersistentFields(CrudContext context) {
		Object entity = context.getEntity();
		List<Field> result = new ArrayList<Field>();
		List<Field> fields = BeanReflectionUtils.loadClassFields(BeanReflectionUtils.getClass(entity));
		if (fields != null) {
			for (Field field : fields) {
				Class<?> propertyClass = field.getType();
				if (Collection.class.isAssignableFrom(propertyClass)) {
					field.setAccessible(true);
					Collection c = (Collection) BeanReflectionUtils.getProperty(entity, field.getName());
					if (!CollectionUtils.isEmpty(c)) {
						propertyClass = c.iterator().next().getClass();
					}
				}
				if (JpaUtil.isEntityClass(propertyClass)) {
					result.add(field);
				}
			}
		}
		return result;
	}

	protected List<Map<String, Object>> getNeedGeneratorFields(Object entity, CrudType crudType)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Field> fields = BeanReflectionUtils.loadClassFields(BeanReflectionUtils.getClass(entity));

		if (fields != null) {
			for (Field field : fields) {
				Generator generator = field.getAnnotation(Generator.class);
				Map<String, Object> map = new HashMap<String, Object>();
				if (generator != null) {
					GeneratorPolicy policy = generator.policy().getDeclaredConstructor().newInstance();
					if (crudType.equals(policy.getType()) || (CrudType.SAVE_OR_UPDATE.equals(policy.getType())
							&& !crudType.equals(CrudType.DELETE))) {
						map.put("field", field);
						map.put("policy", policy);
						result.add(map);
					} else {
						continue;
					}
				}
			}
		}
		return result;
	}

	public void setCrudPolicy(CrudPolicy crudPolicy) {
		this.crudPolicy = crudPolicy;
	}
}