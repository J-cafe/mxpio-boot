package com.mxpioframework.camunda.plugin;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.pvm.process.TransitionImpl;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.engine.impl.util.xml.Element;

@Slf4j
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

    // BpmnParseListener implementation
    // /

    @Override
    public void parseProcess(Element processElement, ProcessDefinitionEntity processDefinition) {
        addStartEventListener(processDefinition);
        addEndEventListener(processDefinition);
    }

    @Override
    public void parseStartEvent(Element startEventElement, ScopeImpl scope, ActivityImpl startEventActivity) {
        addStartEventListener(startEventActivity);
        addEndEventListener(startEventActivity);
    }

    @Override
    public void parseExclusiveGateway(Element exclusiveGwElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseInclusiveGateway(Element inclusiveGwElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseParallelGateway(Element parallelGwElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseScriptTask(Element scriptTaskElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseServiceTask(Element serviceTaskElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseBusinessRuleTask(Element businessRuleTaskElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseTask(Element taskElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseManualTask(Element manualTaskElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
        UserTaskActivityBehavior activityBehavior = (UserTaskActivityBehavior) activity.getActivityBehavior();
        TaskDefinition taskDefinition = activityBehavior.getTaskDefinition();
        addTaskCreateListeners(taskDefinition);
        addTaskAssignmentListeners(taskDefinition);
        addTaskCompleteListeners(taskDefinition);
        addTaskUpdateListeners(taskDefinition);
        addTaskDeleteListeners(taskDefinition);
    }

    @Override
    public void parseEndEvent(Element endEventElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseSubProcess(Element subProcessElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseCallActivity(Element callActivityElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseSequenceFlow(Element sequenceFlowElement, ScopeImpl scopeElement, TransitionImpl transition) {
        addTakeEventListener(transition);
    }

    @Override
    public void parseSendTask(Element sendTaskElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseMultiInstanceLoopCharacteristics(Element activityElement,
                                                      Element multiInstanceLoopCharacteristicsElement, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseReceiveTask(Element receiveTaskElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseEventBasedGateway(Element eventBasedGwElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseTransaction(Element transactionElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseIntermediateThrowEvent(Element intermediateEventElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseIntermediateCatchEvent(Element intermediateEventElement, ScopeImpl scope, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }

    @Override
    public void parseBoundaryEvent(Element boundaryEventElement, ScopeImpl scopeElement, ActivityImpl activity) {
        addStartEventListener(activity);
        addEndEventListener(activity);
    }
}
