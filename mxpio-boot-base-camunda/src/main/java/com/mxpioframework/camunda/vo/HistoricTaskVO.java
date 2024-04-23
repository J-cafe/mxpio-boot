package com.mxpioframework.camunda.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.mxpioframework.jpa.annotation.DictAble;
import com.mxpioframework.security.annotation.Dict;
import com.mxpioframework.security.entity.User;
import org.camunda.bpm.engine.history.HistoricActivityInstance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HistoricTaskVO implements Serializable, DictAble {

	public HistoricTaskVO(HistoricActivityInstance activity) {
		this.id = activity.getTaskId();
		this.name = activity.getActivityName();
		this.endTime = activity.getEndTime();
		this.assignee = activity.getAssignee();
		this.canceled = activity.isCanceled();
		this.activityType = activity.getActivityType();
		this.activityId = activity.getActivityId();
	}

	private static final long serialVersionUID = 1L;
	
	@Schema(description = "ID")
	private String id;
	
	@Schema(description = "名称")
	private String name;

	@Schema(description = "节点ID")
	private String activityId;

	@Schema(description = "任务节点定义Key")
	private String taskDefinitionKey;

	@Schema(description = "操作人")
	@Dict(dicCode = "username", dicEntity = User.class, dicText = "nickname")
	private String assignee;
	
	@Schema(description = "结束时间")
	private Date endTime;
	
	@Schema(description = "签核意见")
	private String comment;
	
	@Schema(description = "是否撤销")
	private boolean canceled;
	
	@Schema(description = "节点类型")
	private String activityType;

	@Schema(description = "是否有表单")
	private Boolean hasForm;

	private Map<String, String> textMap;

	public String putText(String key, String value) {
		if (textMap == null) {
			textMap = new HashMap<>();
		}
		return textMap.put(key, value);
	}

}
