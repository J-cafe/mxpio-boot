package com.mxpio.mxpioboot.jpa.policy;

import com.mxpio.mxpioboot.jpa.query.Criteria;

public interface CriteriaContext {
	Criteria getCriteria();
	<E> void setCurrent(E e);
	<E> E getCurrent();
}
