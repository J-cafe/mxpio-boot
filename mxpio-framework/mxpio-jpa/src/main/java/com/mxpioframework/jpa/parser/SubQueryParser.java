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
	private String joinProperty;
	private String parentProperty;

	// EAV two-level (nested) mode
	private Class<?> middleEntity;
	private String middleJoinProp;
	private String middleParentProp;
	private Class<?> extAttrEntity;
	private String extJoinProp;
	private String extParentProp;
	private String attrKeyProp = "attrKey";
	private String attrValueProp = "attrValue";

	public SubQueryParser(Linq linq, Class<?> domainClass) {
		this.linq = linq;
		this.domainClass = domainClass;
		this.foreignKeys = new String[] { Introspector.decapitalize(domainClass.getSimpleName())
				+ StringUtils.capitalize(JpaUtil.getIdName(domainClass)) };
	}

	public SubQueryParser(Linq linq, Class<?> domainClass, String... foreignKeys) {
		this(linq, domainClass);
		this.foreignKeys = foreignKeys;
	}

	/**
	 * EAV mode constructor. Matches criteria alias {@code eavAlias} and joins
	 * with {@code joinProperty = parent.parentProperty} instead of the default
	 * PK = FK join.
	 *
	 * @param linq          主查询 Linq
	 * @param domainClass   子查询实体类（如扩展属性表）
	 * @param eavAlias      条件别名（如 "ext"）→ 匹配 "ext.xxx"
	 * @param joinProperty  子查询侧的关联属性（如扩展表的 "taskId" FK）
	 * @param parentProperty 父查询侧的关联属性（如主表的 "id" PK）
	 */
	public SubQueryParser(Linq linq, Class<?> domainClass,
						  String eavAlias, String joinProperty, String parentProperty) {
		this.linq = linq;
		this.domainClass = domainClass;
		this.eavAlias = eavAlias;
		this.joinProperty = joinProperty;
		this.parentProperty = parentProperty;
	}

	/**
	 * Two-level nested EAV mode constructor. Matches {@code alias.key} and
	 * generates nested EXISTS: outer-entity → ext-attr table.
	 *
	 * <pre>
	 * EXISTS (SELECT m FROM middleEntity m WHERE m.middleJoinProp = parent.middleParentProp
	 *   AND EXISTS (SELECT e FROM extAttrEntity e WHERE e.extJoinProp = m.extParentProp
	 *     AND e.attrKeyProp = keyName AND e.attrValueProp [op] value))
	 * </pre>
	 *
	 * @param linq             the main query Linq
	 * @param middleEntity     middle entity (e.g. Task.class)
	 * @param middleJoinProp   FK on middle pointing to the query entity (e.g. "projectId")
	 * @param middleParentProp PK of the query entity (e.g. "id")
	 * @param extAttrEntity    ext-attr entity (e.g. TaskExtAttr.class)
	 * @param extJoinProp      FK on ext-attr pointing to middle (e.g. "taskId")
	 * @param extParentProp    PK of middle entity (e.g. "id")
	 * @param attrKeyProp      attr-key column name on ext-attr table
	 * @param attrValueProp    attr-value column name on ext-attr table
	 * @param alias            criteria prefix to match (e.g. "tasks.ext")
	 */
	public SubQueryParser(Linq linq,
						  Class<?> middleEntity, String middleJoinProp, String middleParentProp,
						  Class<?> extAttrEntity, String extJoinProp, String extParentProp,
						  String attrKeyProp, String attrValueProp,
						  String alias) {
		this.linq = linq;
		this.eavAlias = alias;
		this.middleEntity = middleEntity;
		this.middleJoinProp = middleJoinProp;
		this.middleParentProp = middleParentProp;
		this.extAttrEntity = extAttrEntity;
		this.extJoinProp = extJoinProp;
		this.extParentProp = extParentProp;
		this.attrKeyProp = attrKeyProp;
		this.attrValueProp = attrValueProp;
	}

	@Override
	public boolean parse(SimpleCriterion criterion) {
		String property = criterion.getFieldName();
		if (!StringUtils.contains(property, '.')) {
			return false;
		}

		String alias = StringUtils.substringBefore(property, ".");

		// Two-level EAV mode: "alias.key" → nested EXISTS
		if (eavAlias != null && eavAlias.equals(alias) && middleEntity != null) {
			String keyName = property.substring(alias.length() + 1);
			if (keyName.isEmpty()) {
				return false;
			}
			Linq first = linq.exists(middleEntity)
					.equalProperty(middleJoinProp, middleParentProp);
			Linq second = first.exists(extAttrEntity)
					.equalProperty(extJoinProp, extParentProp)
					.equal(attrKeyProp, keyName);

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

		// Single-level EAV mode: "alias.key" → EXISTS
		if (eavAlias != null && eavAlias.equals(alias)) {
			Linq l = linq.exists(domainClass).equalProperty(joinProperty, parentProperty);
			CriteriaUtils.parse(l, criterion);
			l.end();
			return true;
		}

		// Normal mode: match by foreign key naming convention
		for (String foreignKey : foreignKeys) {
			if (StringUtils.startsWith(alias, foreignKey) || StringUtils.startsWith(foreignKey, alias)) {
				Linq l = linq.exists(domainClass).equalProperty(JpaUtil.getIdName(domainClass), foreignKey);
				CriteriaUtils.parse(l, criterion);
				l.end();
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the join property name for EAV mode (the FK column on the subquery side).
	 */
	public String getJoinProperty() {
		return joinProperty;
	}
}
