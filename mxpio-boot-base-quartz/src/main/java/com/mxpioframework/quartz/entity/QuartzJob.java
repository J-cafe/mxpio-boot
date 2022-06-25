package com.mxpioframework.quartz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_QUARTZ_JOB")
@ApiModel(value="定时任务")
public class QuartzJob extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_", updatable = false)
	@ApiModelProperty(value = "ID")
	private String id;
	
	@Column(name = "JOB_CLASS_NAME_")
	@ApiModelProperty(value = "任务类名")
	private String jobClassName;
	
	@Column(name = "JOB_CRON_")
	@ApiModelProperty(value = "CRON表达式")
	private String jobCron;
	
	@Column(name = "JOB_PARAMS_")
	@ApiModelProperty(value = "参数")
	private String jobParams;
	
	@Column(name = "JOB_DESC_")
	@ApiModelProperty(value = "描述")
	private String desc;
	
	@Column(name = "JOB_STATUS_")
	@ApiModelProperty(value = "状态")
	private String status;

}
