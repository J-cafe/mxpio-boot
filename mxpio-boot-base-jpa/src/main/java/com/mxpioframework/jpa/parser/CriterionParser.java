package com.mxpioframework.jpa.parser;

import com.mxpioframework.jpa.query.SimpleCriterion;

public interface CriterionParser {
	boolean parse(SimpleCriterion criterion);
}
