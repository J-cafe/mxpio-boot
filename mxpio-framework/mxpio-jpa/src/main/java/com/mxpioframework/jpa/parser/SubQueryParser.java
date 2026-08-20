package com.mxpioframework.jpa.parser;

import java.beans.Introspector;

import org.apache.commons.lang3.StringUtils;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.lin.Linq;
import com.mxpioframework.jpa.query.CriteriaUtils;
import com.mxpioframework.jpa.query.SimpleCriterion;

public class SubQueryParser implements CriterionParser {

	private Class<?> domainClass;
	private Linq linq;
	private String[] foreignKeys;

	// EAV single-level mode
	private String eavAlias;
	private String[] joinProperties;
	private String[] parentProperties;

	// EAV two-level (nested) mode
	private Class<?> middleEntity;
	private String[] middleJoinProps;
	private String[] middleParentProps;
	private Class<?> extAttrEntity;
	private String[] extJoinProps;
	private String[] extParentProps;
	private String attrKeyProp = "attrKey";
	private String attrValueProp = "attrValue";

	// false = alias.key parsed as plain property path (multi-key collect mode)
	private boolean translateAttrKey = true;

	// ---- Normal mode ----

	public SubQueryParser(Linq linq, Class<?> domainClass) {
		this.linq = linq;
		this.domainClass = domainClass;
		this.foreignKeys = new String[]{Introspector.decapitalize(domainClass.getSimpleName())
				+ StringUtils.capitalize(JpaUtil.getIdName(domainClass))};
	}

	public SubQueryParser(Linq linq, Class<?> domainClass, String... foreignKeys) {
		this(linq, domainClass);
		this.foreignKeys = foreignKeys;
	}

	// ---- EAV single-level (single key, backward compatible) ----

	public SubQueryParser(Linq linq, Class<?> domainClass,
						  String eavAlias, String joinProperty, String parentProperty) {
		this(linq, domainClass, eavAlias,
				new String[]{joinProperty}, new String[]{parentProperty});
	}

	// ---- EAV single-level (multi-key) ----

	public SubQueryParser(Linq linq, Class<?> domainClass,
						  String eavAlias, String[] joinProperties, String[] parentProperties) {
		this.linq = linq;
		this.domainClass = domainClass;
		this.eavAlias = eavAlias;
		this.joinProperties = joinProperties;
		this.parentProperties = parentProperties;
	}

	// ---- multi-key collect (non-EAV) mode ----

	// Same shape as single-level EAV, but "alias.key" conditions are parsed as
	// plain property paths on the collected entity (no attrKey/attrValue
	// translation).
	public SubQueryParser(Linq linq, Class<?> domainClass,
						  String alias, String[] joinProperties, String[] parentProperties,
						  boolean translateAttrKey) {
		this(linq, domainClass, alias, joinProperties, parentProperties);
		this.translateAttrKey = false;
	}

	// ---- EAV two-level (single key, backward compatible) ----

	public SubQueryParser(Linq linq,
						  Class<?> middleEntity, String middleJoinProp, String middleParentProp,
						  Class<?> extAttrEntity, String extJoinProp, String extParentProp,
						  String attrKeyProp, String attrValueProp,
						  String alias) {
		this(linq,
				middleEntity, new String[]{middleJoinProp}, new String[]{middleParentProp},
				extAttrEntity, new String[]{extJoinProp}, new String[]{extParentProp},
				attrKeyProp, attrValueProp, alias);
	}

	// ---- EAV two-level (multi-key) ----

	public SubQueryParser(Linq linq,
						  Class<?> middleEntity, String[] middleJoinProps, String[] middleParentProps,
						  Class<?> extAttrEntity, String[] extJoinProps, String[] extParentProps,
						  String attrKeyProp, String attrValueProp,
						  String alias) {
		this.linq = linq;
		this.eavAlias = alias;
		this.middleEntity = middleEntity;
		this.middleJoinProps = middleJoinProps;
		this.middleParentProps = middleParentProps;
		this.extAttrEntity = extAttrEntity;
		this.extJoinProps = extJoinProps;
		this.extParentProps = extParentProps;
		this.attrKeyProp = attrKeyProp;
		this.attrValueProp = attrValueProp;
	}

	// ---- parse ----

	@Override
	public boolean parse(SimpleCriterion criterion) {
		String property = criterion.getFieldName();
		if (!StringUtils.contains(property, '.')) {
			return false;
		}

		// Two-level EAV mode (alias may itself contain dots, e.g. "items.ext")
		if (eavAlias != null && middleEntity != null
				&& property.startsWith(eavAlias + ".")) {
			String keyName = property.substring(eavAlias.length() + 1);
			if (keyName.isEmpty()) {
				return false;
			}
			Linq first = linq.exists(middleEntity);
			for (int i = 0; i < middleJoinProps.length; i++) {
				first.equalProperty(middleJoinProps[i], middleParentProps[i]);
			}
			Linq second = first.exists(extAttrEntity);
			for (int i = 0; i < extJoinProps.length; i++) {
				second.equalProperty(extJoinProps[i], extParentProps[i]);
			}
			second.equal(attrKeyProp, keyName);

			String origField = criterion.getFieldName();
			criterion.setFieldName(attrValueProp);
			try {
				CriteriaUtils.parse(second, criterion);
			} finally {
				criterion.setFieldName(origField);
			}
			second.end();
			first.end();
			return true;
		}

		// Single-level EAV mode / multi-key collect mode
		if (eavAlias != null && property.startsWith(eavAlias + ".")) {
			String keyName = property.substring(eavAlias.length() + 1);
			if (keyName.isEmpty()) {
				return false;
			}
			Linq l = linq.exists(domainClass);
			for (int i = 0; i < joinProperties.length; i++) {
				l.equalProperty(joinProperties[i], parentProperties[i]);
			}
			String origField = criterion.getFieldName();
			if (translateAttrKey) {
				l.equal(attrKeyProp, keyName);
				criterion.setFieldName(attrValueProp);
			} else {
				criterion.setFieldName(keyName);
			}
			try {
				CriteriaUtils.parse(l, criterion);
			} finally {
				criterion.setFieldName(origField);
			}
			l.end();
			return true;
		}

		// Normal mode: match by foreign key naming convention
		if (foreignKeys != null) {
			String alias = StringUtils.substringBefore(property, ".");
			for (String foreignKey : foreignKeys) {
				if (StringUtils.startsWith(alias, foreignKey) || StringUtils.startsWith(foreignKey, alias)) {
					Linq l = linq.exists(domainClass)
							.equalProperty(JpaUtil.getIdName(domainClass), foreignKey);
					CriteriaUtils.parse(l, criterion);
					l.end();
					return true;
				}
			}
		}
		return false;
	}
}
