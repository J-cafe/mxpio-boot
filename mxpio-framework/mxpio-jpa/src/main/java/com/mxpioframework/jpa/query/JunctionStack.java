package com.mxpioframework.jpa.query;

import java.util.ArrayList;
import java.util.List;

public class JunctionStack {
	
	private JunctionType type;
	
	private List<Object> criterions;
	
	public static JunctionStack create(JunctionType type) {
		JunctionStack junctionStack = new JunctionStack();
		junctionStack.criterions = new ArrayList<Object>();
		junctionStack.setType(type);
		return junctionStack;
	}

	public JunctionType getType() {
		return type;
	}

	public void setType(JunctionType type) {
		this.type = type;
	}

	public List<Object> getCriterions() {
		return criterions;
	}

	public void setCriterions(List<Object> criterions) {
		this.criterions = criterions;
	}

}
