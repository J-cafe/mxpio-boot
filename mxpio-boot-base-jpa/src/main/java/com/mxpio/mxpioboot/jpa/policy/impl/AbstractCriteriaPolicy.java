package com.mxpio.mxpioboot.jpa.policy.impl;

import java.util.List;

import com.mxpio.mxpioboot.jpa.policy.CriteriaContext;
import com.mxpio.mxpioboot.jpa.policy.CriteriaPolicy;
import com.mxpio.mxpioboot.jpa.query.Criterion;
import com.mxpio.mxpioboot.jpa.query.Junction;
import com.mxpio.mxpioboot.jpa.query.JunctionType;
import com.mxpio.mxpioboot.jpa.query.Order;
import com.mxpio.mxpioboot.jpa.query.SimpleCriterion;

public abstract class AbstractCriteriaPolicy implements CriteriaPolicy {

	protected void parseOrders(CriteriaContext context) {
		List<Order> orders = context.getCriteria().getOrders();
		for (Order order : orders) {
			context.setCurrent(order);
			parseOrder(context);
		}
	}
	
	abstract protected void parseOrder(CriteriaContext context);
	
	protected void parseCriterions(CriteriaContext context) {
		List<Criterion> criterions = context.getCurrent();
		if (criterions != null) {
			for (Criterion criterion : criterions) {
				context.setCurrent(criterion);
				parseCriterion(context);
			}
		}
	}
	
	protected void parseCriterion(CriteriaContext context) {
		Criterion criterion = context.getCurrent();
		if (criterion instanceof SimpleCriterion) {
			parseSimpleCriterion(context);
		} else if (criterion instanceof Junction) {
			if(JunctionType.OR.equals(((Junction) criterion).getType())) {
				parseOrCriterion(context);
			}else if(JunctionType.AND.equals(((Junction) criterion).getType())) {
				parseAndCriterion(context);
			}
		}
	}
	
	abstract protected void parseAndCriterion(CriteriaContext context);

	abstract protected void parseOrCriterion(CriteriaContext context);

	abstract protected void parseSimpleCriterion(CriteriaContext context);

}
