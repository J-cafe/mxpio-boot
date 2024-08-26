package com.mxpioframework.camunda.plugin;

import com.mxpioframework.camunda.CamundaConstant;
import com.mxpioframework.common.util.SpringUtil;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.message.service.MessageService;
import com.mxpioframework.security.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.IdentityLink;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class CamundaGlobalListenerDelegate implements ExecutionListener, TaskListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        if(ExecutionListener.EVENTNAME_START.equals(delegateExecution.getEventName())){
            MessageService messageService = SpringUtil.getBean(MessageService.class);
            Map<String, Object> props = delegateExecution.getVariables();

            RuntimeService runtimeService = delegateExecution.getProcessEngine().getRuntimeService();
            // 获取流程实例
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(delegateExecution.getProcessInstanceId())
                    .singleResult();
            if(processInstance != null){
                // messageService.sendMessage(new String[]{"innerMsg"},"admin",new String[]{props.get(CamundaConstant.BPMN_START_USER).toString()},"流程完成通知:"+props.get(CamundaConstant.BPMN_TITLE),"流程【"+props.get(CamundaConstant.BPMN_TITLE)+"】已完成");
                ActivityInstance[] instances = runtimeService.getActivityInstance(processInstance.getId()).getChildActivityInstances();
                if(instances != null && instances.length == 1 && instances[0].getActivityType().endsWith("EndEvent")){
                    messageService.sendMessage(new String[]{"innerMsg"},"admin",new String[]{props.get(CamundaConstant.BPMN_START_USER).toString()},"流程完成通知:"+props.get(CamundaConstant.BPMN_TITLE),"流程【"+props.get(CamundaConstant.BPMN_TITLE)+"】已完成");
                }
            }
        }
        // log.info("CamundaGlobalListenerDelegate.notify==>"+delegateExecution.getEventName()+"++"+delegateExecution.getCurrentActivityName());
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        MessageService messageService = SpringUtil.getBean(MessageService.class);
        RoleService roleService = SpringUtil.getBean(RoleService.class);

        Map<String, Object> props = delegateTask.getVariables();

        List<String> userIds = new ArrayList<>();

        if(TaskListener.EVENTNAME_CREATE.equals(delegateTask.getEventName())) {
            if (StringUtils.isEmpty(delegateTask.getAssignee())) {
                Set<IdentityLink> ids = delegateTask.getCandidates();
                for (IdentityLink id : ids) {
                    userIds.addAll(JpaUtil.collect(roleService.getUsersWithin(null, id.getGroupId().substring(5)),"username"));
                }
            } else {
                userIds.add(delegateTask.getAssignee());
            }
        }else if(TaskListener.EVENTNAME_UPDATE.equals(delegateTask.getEventName())&&delegateTask.getAssignee()!=null){
            userIds.add(delegateTask.getAssignee());
        }
        if(!userIds.isEmpty()){
            messageService.sendMessage(new String[]{"innerMsg"},"admin",userIds.toArray(new String[]{}),"流程处理通知:"+props.get(CamundaConstant.BPMN_TITLE),"您有新的流程待处理");
        }
    }
}
