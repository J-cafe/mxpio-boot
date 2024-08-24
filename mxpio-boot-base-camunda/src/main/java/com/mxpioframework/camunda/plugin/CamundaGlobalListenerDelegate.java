package com.mxpioframework.camunda.plugin;

import com.mxpioframework.common.util.SpringUtil;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.Operator;
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
import java.util.Set;

@Slf4j
public class CamundaGlobalListenerDelegate implements ExecutionListener, TaskListener {
    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        // log.info("CamundaGlobalListenerDelegate.notify==>"+delegateExecution.getCurrentActivityName());
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("CamundaGlobalListenerDelegate.notify==>"+delegateTask.getName()+"=="+delegateTask.getEventName());
        log.info(delegateTask.getAssignee());
        log.info(delegateTask.getCandidates()==null?"null":delegateTask.getCandidates().size()+"");
        MessageService messageService = SpringUtil.getBean(MessageService.class);
        RoleService roleService = SpringUtil.getBean(RoleService.class);

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
        }
        if(!userIds.isEmpty()){
            messageService.sendMessage(new String[]{"innerMsg"},"admin",userIds.toArray(new String[]{}),"流程处理通知"+delegateTask.getName(),"您有新的流程待处理");
        }
    }
}
