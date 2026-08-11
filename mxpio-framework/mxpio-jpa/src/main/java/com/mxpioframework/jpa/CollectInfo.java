package com.mxpioframework.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.mxpioframework.common.util.BeanReflectionUtils;

@SuppressWarnings("rawtypes")
public class CollectInfo {

	private static final String COMPOSITE_KEY_DELIMITER = "||";

	private Class<?> entityClass;
	private Class<?> relationClass;
	private String relationProperty;
	private String relationOtherProperty;
	private String otherProperty;
	private String[] otherProperties;     // multi-key: matching columns on child side
	private String[] properties;
	private Set set;
	private List<Object[]> compositeKeys; // multi-key: collected FK tuples

	// EAV mode
	private String extAttrMapProperty;
	private String extAttrKeyProp = "attrKey";
	private String extAttrValueProp = "attrValue";
	private String[] extAttrKeys;

	// ---------- multi-key helpers ----------

	public boolean isMultiKey() {
		return otherProperties != null && otherProperties.length > 0;
	}

	public static String buildCompositeKey(Object[] values) {
		return Arrays.stream(values)
				.map(String::valueOf)
				.collect(Collectors.joining(COMPOSITE_KEY_DELIMITER));
	}

	public static String extractCompositeKey(Object entity, String[] propertyNames) {
		Object[] values = new Object[propertyNames.length];
		for (int i = 0; i < propertyNames.length; i++) {
			values[i] = BeanReflectionUtils.getPropertyValue(entity, propertyNames[i]);
		}
		return buildCompositeKey(values);
	}

	// ---------- getters / setters ----------

	public List<Object[]> getCompositeKeys() {
		return compositeKeys;
	}

	public void setCompositeKeys(List<Object[]> compositeKeys) {
		this.compositeKeys = compositeKeys;
	}

	public void addCompositeKey(Object[] values) {
		if (compositeKeys == null) {
			compositeKeys = new ArrayList<>();
		}
		compositeKeys.add(values);
	}

	public String[] getOtherProperties() {
		return otherProperties;
	}

	public void setOtherProperties(String[] otherProperties) {
		this.otherProperties = otherProperties;
	}

	public String getExtAttrMapProperty() {
		return extAttrMapProperty;
	}

	public void setExtAttrMapProperty(String extAttrMapProperty) {
		this.extAttrMapProperty = extAttrMapProperty;
	}

	public String getExtAttrKeyProp() {
		return extAttrKeyProp;
	}

	public void setExtAttrKeyProp(String extAttrKeyProp) {
		this.extAttrKeyProp = extAttrKeyProp;
	}

	public String getExtAttrValueProp() {
		return extAttrValueProp;
	}

	public void setExtAttrValueProp(String extAttrValueProp) {
		this.extAttrValueProp = extAttrValueProp;
	}

	public String[] getExtAttrKeys() {
		return extAttrKeys;
	}

	public void setExtAttrKeys(String[] extAttrKeys) {
		this.extAttrKeys = extAttrKeys;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public Set getSet() {
		return set;
	}

	public void setSet(Set set) {
		this.set = set;
	}

	public Class<?> getRelationClass() {
		return relationClass;
	}

	public void setRelationClass(Class<?> relationClass) {
		this.relationClass = relationClass;
	}

	public String getRelationProperty() {
		return relationProperty;
	}

	public void setRelationProperty(String relationProperty) {
		this.relationProperty = relationProperty;
	}

	public String getRelationOtherProperty() {
		return relationOtherProperty;
	}

	public void setRelationOtherProperty(String relationOtherProperty) {
		this.relationOtherProperty = relationOtherProperty;
	}

	public String[] getProperties() {
		return properties;
	}

	public void setProperties(String[] properties) {
		this.properties = properties;
	}

	@SuppressWarnings("unchecked")
	public void add(Object obj) {
		if (set == null) {
			set = new HashSet();
		}
		set.add(obj);
	}

	public String getOtherProperty() {
		return otherProperty;
	}

	public void setOtherProperty(String otherProperty) {
		this.otherProperty = otherProperty;
	}

}
