package com.mxpioframework.camunda.plugin;

import com.mxpioframework.message.service.MessageService;
import org.camunda.bpm.application.impl.event.ProcessApplicationEventListenerPlugin;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CamundaGlobalListenerPlugin extends ProcessApplicationEventListenerPlugin {

    @Autowired
    private MessageService messageService;

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        List<BpmnParseListener> preParseListeners = processEngineConfiguration.getCustomPreBPMNParseListeners();
        if(preParseListeners == null) {
            preParseListeners = new ArrayList<>();
            processEngineConfiguration.setCustomPreBPMNParseListeners(preParseListeners);
        }
        preParseListeners.add(new CamundaGlobalListener());
    }
}
