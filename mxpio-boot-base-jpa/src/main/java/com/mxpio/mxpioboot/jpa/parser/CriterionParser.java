package com.mxpio.mxpioboot.jpa.parser;

import com.mxpio.mxpioboot.jpa.query.SimpleCriterion;

public interface CriterionParser {
	boolean parse(SimpleCriterion criterion);
}
