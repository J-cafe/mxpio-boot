package com.mxpioframework.excel.importer.policy;

import com.mxpioframework.excel.importer.model.ImporterSolution;

public interface AutoCreateMappingRulePolicy {
	ImporterSolution apply(ImporterSolution importerSolution);
}
