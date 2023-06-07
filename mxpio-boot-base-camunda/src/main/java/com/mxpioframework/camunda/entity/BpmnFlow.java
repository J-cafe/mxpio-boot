package com.mxpioframework.camunda.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_BPMN_FLOW")
@Schema(description="流程信息")
@ToString
public class BpmnFlow extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CODE_")
	@Schema(description = "流程编码")
	private String code;
	
	@Column(name = "NAME_")
	@Schema(description = "流程名称")
	private String name;
	
	@Column(name = "STATUS_")
	@Schema(description = "发布状态")
	private String status;
	
	@Lob
	@Column(name = "XML_")
	@Schema(description = "流程定义")
	private String xml;
	
	@Lob
	@Column(name = "FORM_MODEL_")
	@Schema(description = "表单定义")
	private String formModel;

}
