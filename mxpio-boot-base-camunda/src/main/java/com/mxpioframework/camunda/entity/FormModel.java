package com.mxpioframework.camunda.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import com.mxpioframework.security.annotation.Dict;
import com.mxpioframework.security.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_BPMN_FORM_MODEL")
@Schema(description="流程表单信息")
@ToString
public class FormModel extends BaseEntity {

	@Id
	@Column(name = "CODE_")
	@Schema(description = "表单编码")
	private String code;

	@Column(name = "NAME_")
	@Schema(description = "表单名称")
	private String name;

	@Column(name = "STATUS_")
	@Schema(description = "发布状态")
	@Dict(dicCode = "MB_BPMN_DEPLOY_CODE")
	private String status;

	@Column(name = "DESC_")
	@Schema(description = "表单描述")
	private String desc;

	@Lob
	@Column(name = "MODEL_",columnDefinition = "longtext")
	@Schema(description = "表单内容")
	private String model;

	@Column(name = "LAST_DEF_KEY_")
	@Schema(description = "当前定义KEY")
	private String lastDefKey;

	@Column(name = "LAST_DEF_VERSION_")
	@Schema(description = "当前定义版本")
	private Integer lastDefVersion;

}
