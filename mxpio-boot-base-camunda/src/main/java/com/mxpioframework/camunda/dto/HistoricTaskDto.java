package com.mxpioframework.camunda.dto;

import java.io.Serializable;
import java.util.Date;

import org.camunda.bpm.engine.history.HistoricActivityInstance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HistoricTaskDto implements Serializable  {

	public HistoricTaskDto(HistoricActivityInstance activity) {
		this.id = activity.getTaskId();
		this.name = activity.getActivityName();
		this.endTime = activity.getEndTime();
		this.assignee = activity.getAssignee();
		this.canceled = activity.isCanceled();
	}

	private static final long serialVersionUID = 1L;
	
	@Schema(description = "ID")
	private String id;
	
	@Schema(description = "名称")
	private String name;
	
	@Schema(description = "操作人")
	private String assignee;
	
	@Schema(description = "结束时间")
	private Date endTime;
	
	@Schema(description = "签核意见")
	private String comment;
	
	@Schema(description = "是否撤销")
	private boolean canceled;

}
