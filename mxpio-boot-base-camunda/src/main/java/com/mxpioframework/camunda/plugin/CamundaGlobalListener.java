package com.mxpioframework.camunda.plugin;

import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.pvm.process.TransitionImpl;
import org.camunda.bpm.engine.impl.task.TaskDefinition;

public class CamundaGlobalListener extends AbstractBpmnParseListener {

    public final static ExecutionListener EXECUTION_LISTENER = new CamundaGlobalListenerDelegate();
    public final static TaskListener TASK_LISTENER = new CamundaGlobalListenerDelegate();

    protected void addEndEventListener(ScopeImpl activity) {
        //activity.addExecutionListener(ExecutionListener.EVENTNAME_END, EXECUTION_LISTENER);
        activity.addListener(ExecutionListener.EVENTNAME_END, EXECUTION_LISTENER);
    }

    protected void addStartEventListener(ScopeImpl activity) {
        //activity.addExecutionListener(ExecutionListener.EVENTNAME_START, EXECUTION_LISTENER);
        activity.addListener(ExecutionListener.EVENTNAME_START, EXECUTION_LISTENER);
    }

    protected void addTakeEventListener(TransitionImpl transition) {
        //transition.addExecutionListener(EXECUTION_LISTENER);
        transition.addListener(ExecutionListener.EVENTNAME_TAKE, EXECUTION_LISTENER);

    }

    protected void addTaskAssignmentListeners(TaskDefinition taskDefinition) {
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_ASSIGNMENT, TASK_LISTENER);
    }

    protected void addTaskCreateListeners(TaskDefinition taskDefinition) {
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_CREATE, TASK_LISTENER);
    }

    protected void addTaskCompleteListeners(TaskDefinition taskDefinition) {
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_COMPLETE, TASK_LISTENER);
    }

    protected void addTaskUpdateListeners(TaskDefinition taskDefinition) {
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_UPDATE, TASK_LISTENER);
    }

    protected void addTaskDeleteListeners(TaskDefinition taskDefinition) {
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_DELETE, TASK_LISTENER);
    }
}
