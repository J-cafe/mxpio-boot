package com.mxpioframework.expression.handler;

import com.googlecode.aviator.AviatorEvaluator;
import com.mxpioframework.expression.context.ElContext;
import com.mxpioframework.expression.model.MXExpression;

public class ExpressionHandler {
	
	public ElContext context = new ElContext();
	
	public MXExpression compile(String expression) {
		MXExpression mxe = new MXExpression();
		mxe.setContext(context);
		mxe.setExpression(AviatorEvaluator.compile(expression));
		return mxe;
	}

	public ElContext getContext() {
		return context;
	}

}
