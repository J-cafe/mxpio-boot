package com.mxpioframework.jpa.parser;

import com.mxpioframework.jpa.query.SimpleCriterion;

public class EmptyCriterionParser implements CriterionParser {

	@Override
	public boolean parse(SimpleCriterion criterion) {
		return false;
	}

}
