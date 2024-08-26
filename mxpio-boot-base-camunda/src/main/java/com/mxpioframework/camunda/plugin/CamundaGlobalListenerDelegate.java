package com.mxpioframework.camunda.plugin;

import com.mxpioframework.camunda.CamundaConstant;
import com.mxpioframework.common.util.SpringUtil;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.message.service.MessageService;
import com.mxpioframework.security.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.task.IdentityLink;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class CamundaGlobalListenerDelegate implements ExecutionListener, TaskListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        // log.info("CamundaGlobalListenerDelegate.notify==>"+delegateExecution.getEventName()+"++"+delegateExecution.getCurrentActivityName());
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        MessageService messageService = SpringUtil.getBean(MessageService.class);
        RoleService roleService = SpringUtil.getBean(RoleService.class);

        Map<String, Object> props = delegateTask.getVariables();

        List<String> userIds = new ArrayList<>();

        if("create".equals(delegateTask.getEventName())) {
            if (StringUtils.isEmpty(delegateTask.getAssignee())) {
                Set<IdentityLink> ids = delegateTask.getCandidates();
                for (IdentityLink id : ids) {
                    userIds.addAll(JpaUtil.collect(roleService.getUsersWithin(null, id.getGroupId().substring(5)),"username"));
                }
            } else {
                userIds.add(delegateTask.getAssignee());
            }
        }else if("update".equals(delegateTask.getEventName())&&delegateTask.getAssignee()!=null){
            userIds.add(delegateTask.getAssignee());
        }else if("complete".equals(delegateTask.getEventName())){
            /*
            // 获取流程引擎服务
            ProcessEngine processEngine = delegateTask.getProcessEngine();
            RuntimeService runtimeService = processEngine.getRuntimeService();

            // 获取流程实例ID
            String processInstanceId = delegateTask.getProcessInstanceId();

            // 检查流程实例是否已经完成
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (processInstance == null) {
                messageService.sendMessage(new String[]{"innerMsg"},"admin",new String[]{props.get(CamundaConstant.BPMN_START_USER).toString()},"流程完成通知:"+props.get(CamundaConstant.BPMN_TITLE),"流程【"+props.get(CamundaConstant.BPMN_TITLE)+"】已完成");
            }*/
        }
        if(!userIds.isEmpty()){
            Map<String, Object> map = delegateTask.getVariablesLocal();
            for(Map.Entry<String, Object> entry : map.entrySet()){
                log.info(entry.getKey()+"===>"+entry.getValue());
            }
            messageService.sendMessage(new String[]{"innerMsg"},"admin",userIds.toArray(new String[]{}),"流程处理通知:"+props.get(CamundaConstant.BPMN_TITLE),"您有新的流程待处理");
        }
    }
}
