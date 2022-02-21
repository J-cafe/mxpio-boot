package com.mxpioframework.expression.model;

import com.googlecode.aviator.Expression;
import com.mxpioframework.expression.context.ElContext;

public class MXExpression {

	private Expression expression;
	
	private ElContext context;
	
	public Object evaluate() {
		return expression.execute(context);
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	public ElContext getContext() {
		return context;
	}

	public void setContext(ElContext context) {
		this.context = context;
	}
	
}
