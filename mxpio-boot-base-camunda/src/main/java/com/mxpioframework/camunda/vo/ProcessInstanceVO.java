package com.mxpioframework.camunda.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.mxpioframework.jpa.annotation.DictAble;
import com.mxpioframework.security.annotation.Dict;
import com.mxpioframework.security.entity.User;
import org.camunda.bpm.engine.history.HistoricProcessInstance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProcessInstanceVO implements Serializable, DictAble {

	private static final long serialVersionUID = 1L;
	
	@Schema(description = "ID")
	private String id;
	
	@Schema(description = "业务ID")
	private String businessKey;

	@Schema(description = "流程标题")
	private String title;

	@Schema(description = "加急")
	private String bpmnSortFlag;

	@Schema(description = "流程定义ID")
	private String processDefinitionId;
	
	@Schema(description = "流程定义Key")
	private String processDefinitionKey;
	
	@Schema(description = "流程定义名称")
	private String processDefinitionName;
	
	@Schema(description = "流程版本")
	private Integer processVersion;
	
	@Schema(description = "开始时间")
	private Date startTime;
	
	@Schema(description = "状态")
	private String state;
	
	@Schema(description = "申请人")
	@Dict(dicCode = "username", dicText = "nickname", dicEntity = User.class)
	private String startUserId;

	public ProcessInstanceVO(HistoricProcessInstance procInst) {
		this.id = procInst.getId();
		this.businessKey = procInst.getBusinessKey();
		this.processDefinitionName = procInst.getProcessDefinitionName();
		this.processDefinitionId = procInst.getProcessDefinitionId();
		this.processDefinitionKey = procInst.getProcessDefinitionKey();
		this.processVersion = procInst.getProcessDefinitionVersion();
		this.startTime = procInst.getStartTime();
		this.state = procInst.getState();
		this.startUserId = procInst.getStartUserId();
	}

	private Map<String, String> textMap;

	public String putText(String key, String value) {
		if (textMap == null) {
			textMap = new HashMap<>();
		}
		return textMap.put(key, value);
	}
}
