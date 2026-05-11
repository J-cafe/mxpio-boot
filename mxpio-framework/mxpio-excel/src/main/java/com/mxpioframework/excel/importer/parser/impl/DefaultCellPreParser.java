package com.mxpioframework.excel.importer.parser.impl;

import org.springframework.stereotype.Component;

import com.mxpioframework.excel.importer.parser.CellPreParser;
import com.mxpioframework.excel.importer.policy.Context;

@Component("importer.defaultCellPreParser")
public class DefaultCellPreParser implements CellPreParser {

	@Override
	public String getName() {
		return "默认前置解析器";
	}

	@Override
	public void parse(Context context) {
		context.setValue(context.getCurrentCell().getValue());
	}

}
