package com.mxpioframework.excel.importer.processor.impl;

import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.policy.Context;
import com.mxpioframework.excel.importer.processor.CellPreprocessor;

@Component("importer.emptyCellPreprocessor")
public class EmptyCellPreprocessor implements CellPreprocessor {

	@Override
	public void process(Context context) {

	}

	@Override
	public boolean support(Context context) {
		return false;
	}

}
