package com.mxpioframework.flowable.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_FLOW")
@Schema(description="流程")
public class Flow extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "FLOW_CODE_", updatable = false)
	@Schema(description = "流程编号")
	private String flowCode;
	
	@Column(name = "FLOW_TITLE_")
	@Schema(description = "流程标题")
	private String flowTitle;
	
	@Column(name = "FLOW_STATUS_")
	@Schema(description = "流程状态")
	private String flowStatus;
	
	@Column(name = "FLOW_VERSION_")
	@Schema(description = "流程版本")
	private String flowVersion;
	
	@Column(name = "FLOW_TYPE_")
	@Schema(description = "流程类别")
	private String flowType;
	
	@Column(name = "FLOW_ENTITY_")
	@Schema(description = "数据实体")
	private String flowEntity;
	
	@Column(name = "FLOW_FORM_")
	@Schema(description = "表单模板")
	private String flowForm;
	
	@Column(name = "FLOW_REPORT_")
	@Schema(description = "打印模板")
	private String flowReport;
	
	@Column(name = "FLOW_BIZ_TITLE_")
	@Schema(description = "业务标题")
	private String flowBizTitle;
	
	@Column(name = "FLOW_BIZ_SUMMARY_")
	@Schema(description = "概要")
	private String flowBizSummary;

}
