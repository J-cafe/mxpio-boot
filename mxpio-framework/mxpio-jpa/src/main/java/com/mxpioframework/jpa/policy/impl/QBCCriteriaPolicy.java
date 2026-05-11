package com.mxpioframework.jpa.policy.impl;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.stereotype.Component;

import com.mxpioframework.jpa.lin.Linq;
import com.mxpioframework.jpa.parser.CriterionParser;
import com.mxpioframework.jpa.policy.CriteriaContext;
import com.mxpioframework.jpa.query.CriteriaUtils;
import com.mxpioframework.jpa.query.Junction;
import com.mxpioframework.jpa.query.Order;
import com.mxpioframework.jpa.query.SimpleCriterion;

@Component
public class QBCCriteriaPolicy extends AbstractCriteriaPolicy {

	@Override
	public void apply(CriteriaContext context) {
		QBCCriteriaContext c = (QBCCriteriaContext) context;
		if (c.getCriteria() != null) {
			c.setCurrent(c.getCriteria().getCriterions());
			parseCriterions(c);
			parseOrders(c);
		}
	}

	@Override
	protected void parseOrder(CriteriaContext context) {
		QBCCriteriaContext c = (QBCCriteriaContext) context;
		Order order = c.getCurrent();
		Linq linq = c.getLinq();
		if (order.isDesc()) {
			linq.desc(order.getFieldName());
		} else {
			linq.asc(order.getFieldName());
		}
	}

	@Override
	protected void parseAndCriterion(CriteriaContext context) {
		QBCCriteriaContext c = (QBCCriteriaContext) context;
		Junction and = c.getCurrent();
		c.getLinq().and();
		c.setCurrent(and.getCriterions());
		parseCriterions(c);
		c.getLinq().end();
	}

	@Override
	protected void parseOrCriterion(CriteriaContext context) {
		QBCCriteriaContext c = (QBCCriteriaContext) context;
		Junction or = c.getCurrent();
		c.getLinq().or();
		c.setCurrent(or.getCriterions());
		parseCriterions(c);
		c.getLinq().end();
	}

	@Override
	protected void parseSimpleCriterion(CriteriaContext context) {
		QBCCriteriaContext criteriaContext = (QBCCriteriaContext) context;
		List<CriterionParser> criterionParsers = criteriaContext.getCriterionParsers();
		SimpleCriterion criterion = criteriaContext.getCurrent();
		String property = criterion.getFieldName();
		Object value = criterion.getValue();
		Class<?> cls = criteriaContext.getEntityClass();
		boolean result = false;
		for (CriterionParser criterionParser : criterionParsers) {
			result = criterionParser.parse(criterion);
			if (result) {
				break;
			}
		}

		if (!result) {
			if (cls != null) {
				Field field = FieldUtils.getField(cls, property, true);
				if (Enum.class.isAssignableFrom(field.getType()) && value instanceof String) {
					Class<?> type = field.getType();
					Enum<?>[] items = (Enum<?>[]) type.getEnumConstants();
					if (items != null) {
						for (Enum<?> item : items) {
							if (item.name().equals(value)) {
								criterion.setValue(item);
								break;
							}
						}
					}
				}

			}
			Linq linq = criteriaContext.getLinq();

			CriteriaUtils.parse(linq, criterion);
		}
	}

}
