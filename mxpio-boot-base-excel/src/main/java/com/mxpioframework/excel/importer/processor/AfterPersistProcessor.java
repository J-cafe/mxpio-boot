package com.mxpioframework.excel.importer.processor;

import com.mxpioframework.excel.importer.policy.Context;

public interface AfterPersistProcessor {
    void process(Context context);
    boolean support(Context context);
}
