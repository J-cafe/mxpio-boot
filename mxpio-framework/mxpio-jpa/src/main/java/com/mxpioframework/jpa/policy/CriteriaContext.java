package com.mxpioframework.jpa.policy;

import com.mxpioframework.jpa.query.Criteria;

public interface CriteriaContext {
	Criteria getCriteria();

	<E> void setCurrent(E e);

	<E> E getCurrent();
}
