package com.mxpioframework.camunda.plugin;

import com.mxpioframework.camunda.CamundaConstant;
import com.mxpioframework.common.util.SpringUtil;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.message.service.MessageService;
import com.mxpioframework.security.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.engine.task.Task;

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
            String businessKey = null;
            if (props.containsKey("businessKey")){
                businessKey = props.get("businessKey").toString();
            }
            RuntimeService runtimeService = delegateExecution.getProcessEngine().getRuntimeService();
            // 获取流程实例
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(delegateExecution.getProcessInstanceId())
                    .singleResult();
            if(processInstance != null){
                ActivityInstance[] instances = runtimeService.getActivityInstance(processInstance.getId()).getChildActivityInstances();
                if(instances != null && instances.length == 1 && instances[0].getActivityType().endsWith("EndEvent")){
                    messageService.sendMessage(new String[]{"innerMsg"},"admin",new String[]{props.get(CamundaConstant.BPMN_START_USER).toString()},"流程完成通知:"+props.get(CamundaConstant.BPMN_TITLE),"流程【"+props.get(CamundaConstant.BPMN_TITLE)+"】已完成",businessKey);
                }
            }
        }
        // log.info("CamundaGlobalListenerDelegate.notify==>"+delegateExecution.getEventName()+"++"+delegateExecution.getCurrentActivityName());
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        // 获取Service
        RoleService roleService = SpringUtil.getBean(RoleService.class);
        HistoryService historyService = delegateTask.getProcessEngineServices().getHistoryService();
        // 获取流程变量
        Map<String, Object> props = delegateTask.getVariables();

        List<String> assignees = new ArrayList<>();
        String businessKey = null;
        if (props.containsKey("businessKey")){
            businessKey = props.get("businessKey").toString();
        }
        // log.info(props.get(CamundaConstant.BPMN_TITLE) + ":" + delegateTask.getName() + ":" + delegateTask.getEventName());

        if(TaskListener.EVENTNAME_CREATE.equals(delegateTask.getEventName())){
            if (StringUtils.isEmpty(delegateTask.getAssignee())) {
                Set<IdentityLink> ids = delegateTask.getCandidates();
                for (IdentityLink id : ids) {
                    if (StringUtils.isBlank(id.getGroupId())){
                        if(StringUtils.isNotBlank(id.getUserId())){
                            assignees.add(id.getUserId());
                        }
                    }else{
                        assignees.addAll(JpaUtil.collect(roleService.getUsersWithin(null, id.getGroupId().substring(5)),"username"));
                    }
                }
            } else {
                assignees.add(delegateTask.getAssignee());
            }
            if(!assignees.isEmpty()){
                sendMessage(new String[]{"innerMsg"},"admin",assignees.toArray(new String[]{}),"流程处理通知:"+props.get(CamundaConstant.BPMN_TITLE),"您有新的流程待处理",businessKey);
            }
        }else if(TaskListener.EVENTNAME_DELETE.equals(delegateTask.getEventName())){
            sendMessage(new String[]{"innerMsg"},"admin",new String[]{props.get(CamundaConstant.BPMN_START_USER).toString()},"流程未通过通知:"+props.get(CamundaConstant.BPMN_TITLE),"流程【"+props.get(CamundaConstant.BPMN_TITLE)+"】审批未通过",businessKey);
        }else if(TaskListener.EVENTNAME_UPDATE.equals(delegateTask.getEventName()) && delegateTask.getAssignee() != null){
            HistoricTaskInstance historicTask = historyService.createHistoricTaskInstanceQuery()
                    .taskId(delegateTask.getId())
                    .orderByHistoricTaskInstanceEndTime().desc()
                    .singleResult();
            if(!delegateTask.getAssignee().equals(historicTask.getAssignee())){
                sendMessage(new String[]{"innerMsg"},"admin",new String[]{delegateTask.getAssignee()},"流程处理通知:"+props.get(CamundaConstant.BPMN_TITLE),"您有新的流程待处理",businessKey);
            }
        }
    }

    private void sendMessage(String[] chanels, String from, String[] to, String title, String content,String businessKey){
        try {
            MessageService messageService = SpringUtil.getBean(MessageService.class);
            messageService.sendMessage(chanels,from,to,title,content,businessKey);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }
}
