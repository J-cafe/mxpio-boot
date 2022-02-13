package com.mxpioframework.excel.importer.processor.impl;

import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.policy.Context;
import com.mxpioframework.excel.importer.processor.CellPostprocessor;

@Component("importer.emptyCellPostprocessor")
public class EmptyCellPostprocessor implements CellPostprocessor {

	@Override
	public void process(Context context) {

	}

	@Override
	public boolean support(Context context) {
		return false;
	}

}
