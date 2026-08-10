package com.mxpioframework.jpa;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class CollectInfo {
	private Class<?> entityClass;
	private Class<?> relationClass;
	private String relationProperty;
	private String relationOtherProperty;
	private String otherProperty;
	private String[] properties;
	private Set set;

	// EAV mode: if non-null, buildMetadata aggregates rows into Map<String,Map<String,String>>
	// and BackfillFilter fills the named property on the entity
	private String extAttrMapProperty;
	private String extAttrKeyProp = "attrKey";
	private String extAttrValueProp = "attrValue";
	private String[] extAttrKeys;  // if non-null, only load these keys

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
