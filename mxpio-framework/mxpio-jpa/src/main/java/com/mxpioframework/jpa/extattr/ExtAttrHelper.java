package com.mxpioframework.jpa.extattr;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.apache.commons.lang3.ArrayUtils;

import com.mxpioframework.jpa.JpaUtil;

/**
 * Batch utility for EAV extension-attribute collection and backfill.
 * Used for two-level scenarios where {@code Linq.collectExtAttr}'s automatic
 * backfill only covers top-level query results.
 *
 * <h3>Example (two-level)</h3>
 * <pre>{@code
 * Page<Project> page = JpaUtil.linq(Project.class)
 *     .collect("projectId", Task.class, "id")
 *     .collectExtAttr(Task.class, "projectId", "taskId",
 *                     TaskExtAttr.class, "tasks.ext", "extAttrs")
 *     .paging(pageable);
 *
 * List<Task> allTasks = page.getContent().stream()
 *     .flatMap(p -> p.getTasks().stream()).collect(toList());
 *
 * // Load only specific keys
 * ExtAttrHelper.backfillExtAttrs(allTasks, TaskExtAttr.class, "taskId",
 *     Task::getId, Task::setExtAttrs, "color", "priority");
 * }</pre>
 */
public class ExtAttrHelper {

	private ExtAttrHelper() {
	}

	/**
	 * Batch-query and group by owner ID. Loads all keys.
	 */
	public static <T> Map<String, Map<String, String>> collectAsMap(
			List<T> entities,
			Class<?> extAttrEntity,
			String ownerIdProperty,
			Function<T, String> idExtractor) {
		return collectAsMap(entities, extAttrEntity, ownerIdProperty,
				"attrKey", "attrValue", null, idExtractor);
	}

	/**
	 * Batch-query and group by owner ID. Loads only the specified keys.
	 */
	public static <T> Map<String, Map<String, String>> collectAsMap(
			List<T> entities,
			Class<?> extAttrEntity,
			String ownerIdProperty,
			Function<T, String> idExtractor,
			String... keys) {
		return collectAsMap(entities, extAttrEntity, ownerIdProperty,
				"attrKey", "attrValue", keys, idExtractor);
	}

	/**
	 * Batch-query with custom key/value columns. Loads only the specified keys.
	 */
	public static <T> Map<String, Map<String, String>> collectAsMap(
			List<T> entities,
			Class<?> extAttrEntity,
			String ownerIdProperty,
			String extAttrKeyProp,
			String extAttrValueProp,
			String[] keys,
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

		com.mxpioframework.jpa.lin.Linq linq = JpaUtil.linq(extAttrEntity)
				.in(ownerIdProperty, ownerIds);
		if (ArrayUtils.isNotEmpty(keys)) {
			linq.in(extAttrKeyProp, (Object[]) keys);
		}
		List<?> rows = linq.list();

		Map<String, Map<String, String>> result = new HashMap<>();
		for (Object row : rows) {
			String ownerId = (String) getProperty(row, ownerIdProperty);
			String key = (String) getProperty(row, extAttrKeyProp);
			String value = (String) getProperty(row, extAttrValueProp);
			result.computeIfAbsent(ownerId, k -> new HashMap<>()).put(key, value);
		}
		return result;
	}

	/**
	 * Batch-query and backfill. Loads all keys.
	 */
	public static <T> void backfillExtAttrs(
			List<T> entities,
			Class<?> extAttrEntity,
			String ownerIdProperty,
			Function<T, String> idExtractor,
			BiConsumer<T, Map<String, String>> extAttrSetter) {
		backfillExtAttrs(entities, extAttrEntity, ownerIdProperty,
				"attrKey", "attrValue", null, idExtractor, extAttrSetter);
	}

	/**
	 * Batch-query and backfill. Loads only the specified keys.
	 */
	public static <T> void backfillExtAttrs(
			List<T> entities,
			Class<?> extAttrEntity,
			String ownerIdProperty,
			Function<T, String> idExtractor,
			BiConsumer<T, Map<String, String>> extAttrSetter,
			String... keys) {
		backfillExtAttrs(entities, extAttrEntity, ownerIdProperty,
				"attrKey", "attrValue", keys, idExtractor, extAttrSetter);
	}

	/**
	 * Batch-query and backfill with custom key/value columns and key filter.
	 */
	public static <T> void backfillExtAttrs(
			List<T> entities,
			Class<?> extAttrEntity,
			String ownerIdProperty,
			String extAttrKeyProp,
			String extAttrValueProp,
			String[] keys,
			Function<T, String> idExtractor,
			BiConsumer<T, Map<String, String>> extAttrSetter) {

		Map<String, Map<String, String>> attrMap = collectAsMap(
				entities, extAttrEntity, ownerIdProperty,
				extAttrKeyProp, extAttrValueProp, keys, idExtractor);

		for (T entity : entities) {
			String id = idExtractor.apply(entity);
			extAttrSetter.accept(entity, attrMap.getOrDefault(id, Collections.emptyMap()));
		}
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
