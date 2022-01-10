package com.mxpioframework.jpa.query;

import java.util.ArrayList;
import java.util.List;

/**
 * 联合条件
 */
public class Junction implements Criterion {
	private JunctionType type;
	private List<Object> criterions = new ArrayList<Object>();

	public Junction(JunctionType type) {
		this.type = type;
	}

	public void add(Object criterion) {
		this.criterions.add(criterion);
	}

	public List<Object> getCriterions() {
		return this.criterions;
	}

	public JunctionType getType() {
		return type;
	}

	public void setType(JunctionType type) {
		this.type = type;
	}

	public void setCriterions(List<Object> criterions) {
		this.criterions = criterions;
	}
}
