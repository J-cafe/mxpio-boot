package com.mxpio.mxpioboot.jpa.policy;

import javax.persistence.criteria.CriteriaQuery;

public interface CriteriaContext<T> {
	CriteriaQuery<T> getCriteria();
	void setCurrent(T e);
	T getCurrent();
}
