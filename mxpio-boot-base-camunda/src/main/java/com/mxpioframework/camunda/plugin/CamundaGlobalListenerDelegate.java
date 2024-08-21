package com.mxpioframework.camunda.plugin;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;

public class CamundaGlobalListenerDelegate implements ExecutionListener, TaskListener {
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

    }

    @Override
    public void notify(DelegateTask delegateTask) {

    }
}
