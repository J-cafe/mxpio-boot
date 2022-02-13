package com.mxpioframework.excel.importer.policy;

import com.mxpioframework.excel.importer.model.ImporterSolution;

public interface AutoCreateMappingRulePolicy {
	void apply(ImporterSolution importerSolution);
}
