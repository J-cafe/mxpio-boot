package com.mxpioframework.flowable.entity;

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
@Table(name = "MB_FLOW")
@ApiModel(value="流程")
public class Flow extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "FLOW_CODE_", updatable = false)
	@ApiModelProperty(value = "流程编号")
	private String flowCode;
	
	@Column(name = "FLOW_TITLE_")
	@ApiModelProperty(value = "流程标题")
	private String flowTitle;
	
	@Column(name = "FLOW_STATUS_")
	@ApiModelProperty(value = "流程状态")
	private String flowStatus;
	
	@Column(name = "FLOW_VERSION_")
	@ApiModelProperty(value = "流程版本")
	private String flowVersion;
	
	@Column(name = "FLOW_TYPE_")
	@ApiModelProperty(value = "流程类别")
	private String flowType;
	
	@Column(name = "FLOW_ENTITY_")
	@ApiModelProperty(value = "数据实体")
	private String flowEntity;
	
	@Column(name = "FLOW_FORM_")
	@ApiModelProperty(value = "表单模板")
	private String flowForm;
	
	@Column(name = "FLOW_REPORT_")
	@ApiModelProperty(value = "打印模板")
	private String flowReport;
	
	@Column(name = "FLOW_BIZ_TITLE_")
	@ApiModelProperty(value = "业务标题")
	private String flowBizTitle;
	
	@Column(name = "FLOW_BIZ_SUMMARY_")
	@ApiModelProperty(value = "概要")
	private String flowBizSummary;

}
