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

	@Override
	public boolean parse(SimpleCriterion criterion) {
		String property = criterion.getFieldName();
		if (StringUtils.contains(property, '.')) {
			String alias = StringUtils.substringBefore(property, ".");

			for (String foreignKey : foreignKeys) {
				if (StringUtils.startsWith(alias, foreignKey) || StringUtils.startsWith(foreignKey, alias)) {
					Linq l = linq.exists(domainClass).equalProperty(JpaUtil.getIdName(domainClass), foreignKey);
					CriteriaUtils.parse(l, criterion);
					l.end();
					return true;
				}
			}
		}
		return false;
	}
}
