package com.mxpioframework.jpa.extattr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.apache.commons.lang3.ArrayUtils;

import com.mxpioframework.jpa.CollectInfo;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.lin.Linq;
import com.mxpioframework.jpa.query.Junction;
import com.mxpioframework.jpa.query.JunctionType;

/**
 * Batch utility for EAV extension-attribute collection and backfill.
 * Used for two-level scenarios where {@code Linq.collectExtAttr}'s automatic
 * backfill only covers top-level query results.
 */
public class ExtAttrHelper {

	private ExtAttrHelper() {
	}

	// ==================== single-key ====================

	public static <T> Map<String, Map<String, String>> collectAsMap(
			List<T> entities, Class<?> extAttrEntity, String ownerIdProperty,
			Function<T, String> idExtractor) {
		return collectAsMap(entities, extAttrEntity, ownerIdProperty,
				"attrKey", "attrValue", null, idExtractor);
	}

	public static <T> Map<String, Map<String, String>> collectAsMap(
			List<T> entities, Class<?> extAttrEntity, String ownerIdProperty,
			Function<T, String> idExtractor, String... keys) {
		return collectAsMap(entities, extAttrEntity, ownerIdProperty,
				"attrKey", "attrValue", keys, idExtractor);
	}

	public static <T> void backfillExtAttrs(
			List<T> entities, Class<?> extAttrEntity, String ownerIdProperty,
			Function<T, String> idExtractor,
			BiConsumer<T, Map<String, String>> extAttrSetter) {
		Map<String, Map<String, String>> attrMap = collectAsMap(
				entities, extAttrEntity, ownerIdProperty, idExtractor);
		for (T entity : entities) {
			extAttrSetter.accept(entity, attrMap.getOrDefault(
					idExtractor.apply(entity), Collections.emptyMap()));
		}
	}

	public static <T> void backfillExtAttrs(
			List<T> entities, Class<?> extAttrEntity, String ownerIdProperty,
			Function<T, String> idExtractor,
			BiConsumer<T, Map<String, String>> extAttrSetter, String... keys) {
		Map<String, Map<String, String>> attrMap = collectAsMap(
				entities, extAttrEntity, ownerIdProperty, idExtractor, keys);
		for (T entity : entities) {
			extAttrSetter.accept(entity, attrMap.getOrDefault(
					idExtractor.apply(entity), Collections.emptyMap()));
		}
	}

	// ==================== multi-key ====================

	public static <T> Map<String, Map<String, String>> collectAsMapMultiKey(
			List<T> entities, Class<?> extAttrEntity,
			String[] ownerIdProperties, Function<T, String> compositeKeyExtractor) {
		return collectAsMapMultiKey(entities, extAttrEntity, ownerIdProperties,
				"attrKey", "attrValue", null, compositeKeyExtractor);
	}

	public static <T> Map<String, Map<String, String>> collectAsMapMultiKey(
			List<T> entities, Class<?> extAttrEntity,
			String[] ownerIdProperties, Function<T, String> compositeKeyExtractor,
			String... keys) {
		return collectAsMapMultiKey(entities, extAttrEntity, ownerIdProperties,
				"attrKey", "attrValue", keys, compositeKeyExtractor);
	}

	public static <T> Map<String, Map<String, String>> collectAsMapMultiKey(
			List<T> entities, Class<?> extAttrEntity,
			String[] ownerIdProperties,
			String extAttrKeyProp, String extAttrValueProp, String[] keys,
			Function<T, String> compositeKeyExtractor) {

		Set<String> ownerCompositeKeys = new HashSet<>();
		for (T entity : entities) {
			String ck = compositeKeyExtractor.apply(entity);
			if (ck != null) {
				ownerCompositeKeys.add(ck);
			}
		}
		if (ownerCompositeKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		List<?> rows = queryMultiKey(extAttrEntity, ownerIdProperties,
				ownerCompositeKeys, extAttrKeyProp, keys);

		Map<String, Map<String, String>> result = new HashMap<>();
		for (Object row : rows) {
			String ownerCk = CollectInfo.extractCompositeKey(row, ownerIdProperties);
			String k = (String) getProperty(row, extAttrKeyProp);
			String v = (String) getProperty(row, extAttrValueProp);
			result.computeIfAbsent(ownerCk, x -> new HashMap<>()).put(k, v);
		}
		return result;
	}

	public static <T> void backfillExtAttrsMultiKey(
			List<T> entities, Class<?> extAttrEntity,
			String[] ownerIdProperties,
			Function<T, String> compositeKeyExtractor,
			BiConsumer<T, Map<String, String>> extAttrSetter) {
		Map<String, Map<String, String>> attrMap = collectAsMapMultiKey(
				entities, extAttrEntity, ownerIdProperties, compositeKeyExtractor);
		for (T entity : entities) {
			extAttrSetter.accept(entity, attrMap.getOrDefault(
					compositeKeyExtractor.apply(entity), Collections.emptyMap()));
		}
	}

	public static <T> void backfillExtAttrsMultiKey(
			List<T> entities, Class<?> extAttrEntity,
			String[] ownerIdProperties,
			Function<T, String> compositeKeyExtractor,
			BiConsumer<T, Map<String, String>> extAttrSetter, String... keys) {
		Map<String, Map<String, String>> attrMap = collectAsMapMultiKey(
				entities, extAttrEntity, ownerIdProperties, compositeKeyExtractor, keys);
		for (T entity : entities) {
			extAttrSetter.accept(entity, attrMap.getOrDefault(
					compositeKeyExtractor.apply(entity), Collections.emptyMap()));
		}
	}

	// ==================== internal ====================

	@SuppressWarnings("unchecked")
	private static <T> Map<String, Map<String, String>> collectAsMap(
			List<T> entities, Class<?> extAttrEntity, String ownerIdProperty,
			String extAttrKeyProp, String extAttrValueProp, String[] keys,
			Function<T, String> idExtractor) {

		Set<String> ownerIds = new HashSet<>();
		for (T entity : entities) {
			String id = idExtractor.apply(entity);
			if (id != null) {
				ownerIds.add(id);
			}
		}
		if (ownerIds.isEmpty()) {
			return Collections.emptyMap();
		}

		Linq linq = JpaUtil.linq(extAttrEntity).in(ownerIdProperty, ownerIds);
		if (ArrayUtils.isNotEmpty(keys)) {
			linq.in(extAttrKeyProp, (Object[]) keys);
		}
		List<?> rows = linq.list();

		Map<String, Map<String, String>> result = new HashMap<>();
		for (Object row : rows) {
			String ownerId = (String) getProperty(row, ownerIdProperty);
			String k = (String) getProperty(row, extAttrKeyProp);
			String v = (String) getProperty(row, extAttrValueProp);
			result.computeIfAbsent(ownerId, x -> new HashMap<>()).put(k, v);
		}
		return result;
	}

	private static List<?> queryMultiKey(Class<?> extAttrEntity,
										 String[] ownerIdProperties, Set<String> compositeKeys,
										 String extAttrKeyProp, String[] keys) {

		Linq linq = JpaUtil.linq(extAttrEntity);
		if (ArrayUtils.isNotEmpty(keys)) {
			linq.in(extAttrKeyProp, (Object[]) keys);
		}

		// Build OR-of-ANDs for composite FK matching
		List<String[]> parsedKeys = new ArrayList<>();
		for (String ck : compositeKeys) {
			parsedKeys.add(ck.split("\\|\\|"));
		}

		if (parsedKeys.size() == 1) {
			String[] key = parsedKeys.get(0);
			for (int i = 0; i < ownerIdProperties.length; i++) {
				linq.equal(ownerIdProperties[i], key[i]);
			}
		} else {
			Junction orAll = new Junction(JunctionType.OR);
			for (String[] key : parsedKeys) {
				Junction andGroup = new Junction(JunctionType.AND);
				for (int i = 0; i < ownerIdProperties.length; i++) {
					andGroup.add(linq.criteriaBuilder().equal(
							linq.root().get(ownerIdProperties[i]), key[i]));
				}
				orAll.add(andGroup);
			}
			linq.add(orAll);
		}
		return linq.list();
	}

	private static Object getProperty(Object obj, String propertyName) {
		try {
			java.beans.PropertyDescriptor[] pds =
					java.beans.Introspector.getBeanInfo(obj.getClass())
							.getPropertyDescriptors();
			for (java.beans.PropertyDescriptor descriptor : pds) {
				if (descriptor.getName().equals(propertyName)) {
					return descriptor.getReadMethod().invoke(obj);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to read property '" + propertyName + "'", e);
		}
		return null;
	}
}
