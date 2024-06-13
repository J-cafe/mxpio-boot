package com.mxpioframework.quartz.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_QUARTZ_JOB")
@Schema(description="定时任务")
public class QuartzJob extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Generator
	@Column(name = "ID_", updatable = false)
	@Schema(description = "ID")
	private String id;

	@Column(name = "JOB_TYPE_")
	@Schema(description = "任务类型（Class或SpringBean）")
	private String jobType;

	@Column(name = "JOB_CLASS_NAME_")
	@Schema(description = "任务类名/BeanId")
	private String jobClassName;

	@Column(name = "JOB_METHOD_NAME_")
	@Schema(description = "方法名")
	private String jobMethodName;

	@Column(name = "JOB_CRON_")
	@Schema(description = "CRON表达式")
	private String jobCron;

	@Column(name = "JOB_PARAMS_")
	@Schema(description = "参数")
	private String jobParams;

	@Column(name = "JOB_DESC_")
	@Schema(description = "描述")
	private String desc;

	@Column(name = "JOB_STATUS_")
	@Schema(description = "状态")
	private String status;

}
