package com.mxpioframework.jpa.filter.impl;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.CollectionUtils;

import com.mxpioframework.common.util.BeanReflectionUtils;
import com.mxpioframework.jpa.CollectInfo;
import com.mxpioframework.jpa.filter.Filter;
import com.mxpioframework.jpa.policy.LinqContext;

import net.sf.cglib.beans.BeanMap;

public class BackfillFilter implements Filter {

	private List<CollectInfo> collectInfos;
	private Map<Class<?>, List<PropertyDescriptor>> propertyMap = new HashMap<>();
	private Filter filter;

	public BackfillFilter(Filter filter, List<CollectInfo> collectInfos) {
		this.collectInfos = collectInfos;
		this.filter = filter;
	}

	@Override
	public boolean invoke(LinqContext linqContext) {
		Object entity = linqContext.getEntity();
		BeanMap beanMap = BeanMap.create(entity);

		for (CollectInfo collectInfo : collectInfos) {
			Class<?> cls = collectInfo.getEntityClass();

			// Resolve the lookup key: composite for multi-key, single value otherwise
			Object lookupKey;
			if (collectInfo.isMultiKey()) {
				String[] props = collectInfo.getProperties();
				Object[] values = new Object[props.length];
				for (int i = 0; i < props.length; i++) {
					values[i] = beanMap.get(props[i]);
				}
				lookupKey = CollectInfo.buildCompositeKey(values);
			} else if (collectInfo.getProperties() != null && collectInfo.getProperties().length > 0) {
				lookupKey = beanMap.get(collectInfo.getProperties()[0]);
			} else {
				continue;
			}

			// EAV mode
			if (collectInfo.getExtAttrMapProperty() != null) {
				@SuppressWarnings("unchecked")
				Map<Object, Map<String, String>> extMap = (Map<Object, Map<String, String>>)
						linqContext.getMetadata().get(collectInfo.getExtAttrMapProperty());
				if (extMap != null) {
					Map<String, String> attrs = extMap.get(lookupKey);
					if (attrs != null) {
						BeanReflectionUtils.setPropertyValue(entity,
								collectInfo.getExtAttrMapProperty(), attrs);
					}
				}
				continue;
			}

			// Normal entity backfill
			if (cls != null) {
				List<PropertyDescriptor> pds = getPropertyMap(entity).get(cls);
				if (CollectionUtils.isEmpty(pds)) {
					Object value = linqContext.get(cls, lookupKey);
					if (value != null) {
						BeanReflectionUtils.setPropertyValue(entity,
								Introspector.decapitalize(cls.getSimpleName()), value);
					}
				} else if (pds.size() == 1) {
					doFill(linqContext, entity, cls, lookupKey, pds.get(0));
				} else {
					for (PropertyDescriptor pd : pds) {
						doFill(linqContext, entity, cls, lookupKey, pd);
					}
				}
			}
		}

		return filter == null ? true : filter.invoke(linqContext);
	}

	private void doFill(LinqContext linqContext, Object entity, Class<?> cls,
						Object lookupKey, PropertyDescriptor pd) {
		Object value;
		if (Collection.class.isAssignableFrom(pd.getPropertyType())) {
			value = linqContext.getList(cls, lookupKey);
		} else {
			value = linqContext.get(cls, lookupKey);
		}
		if (value != null) {
			try {
				pd.getWriteMethod().invoke(entity, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Map<Class<?>, List<PropertyDescriptor>> getPropertyMap(Object entity) {
		if (propertyMap.isEmpty()) {
			Class<?> entityClass = entity.getClass();
			PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(entityClass);
			if (ArrayUtils.isNotEmpty(pds)) {
				for (PropertyDescriptor pd : pds) {
					for (CollectInfo collectInfo : collectInfos) {
						Class<?> cls = collectInfo.getEntityClass();
						if (cls == null) {
							continue;
						}
						if (pd.getPropertyType().isAssignableFrom(cls)) {
							addPd2PropertyMap(cls, pd);
						} else if (Collection.class.isAssignableFrom(pd.getPropertyType())) {
							Type[] pts = ((ParameterizedType) pd.getReadMethod().getGenericReturnType())
									.getActualTypeArguments();
							if (pts != null && pts.length > 0) {
								Type pt = pts[0];
								if (pt instanceof Class && ((Class<?>) pt).isAssignableFrom(cls)) {
									addPd2PropertyMap(cls, pd);
								}
							}
						}
					}
				}
			}
		}
		return propertyMap;
	}

	private void addPd2PropertyMap(Class<?> cls, PropertyDescriptor pd) {
        List<PropertyDescriptor> list = propertyMap.computeIfAbsent(cls, k -> new ArrayList<>(4));
        list.add(pd);
	}
}
