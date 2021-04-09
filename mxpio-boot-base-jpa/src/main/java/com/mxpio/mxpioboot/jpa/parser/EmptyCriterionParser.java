package com.mxpio.mxpioboot.jpa.parser;

import com.mxpio.mxpioboot.jpa.query.SimpleCriterion;

public class EmptyCriterionParser implements CriterionParser {

	@Override
	public boolean parse(SimpleCriterion criterion) {
		return false;
	}

}
