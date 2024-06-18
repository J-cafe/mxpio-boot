package com.mxpioframework.camunda.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_BPMN_FORM_MODEL_DEF")
@Schema(description="流程表单定义信息")
@ToString
public class FormModelDef extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "KEY_")
	@Schema(description = "KEY")
	private String key;

	@Column(name = "CODE_")
	@Schema(description = "表单编码")
	private String code;

	@Column(name = "NAME_")
	@Schema(description = "表单名称")
	private String name;

	@Column(name = "VERSION_")
	@Schema(description = "表单版本")
	private Integer version;

	@Lob
	@Column(name = "MODEL_",columnDefinition = "longtext")
	@Schema(description = "表单内容")
	private String model;

}
