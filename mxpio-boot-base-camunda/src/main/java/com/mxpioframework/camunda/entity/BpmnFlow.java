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

	@Column(name = "BIZ_TYPE_")
	@Schema(description = "业务分类")
	@Dict(dicCode = "MB_BPMN_BIZ_TYPE")
	private String bizType;

	@Column(name = "STATUS_")
	@Schema(description = "发布状态")
	@Dict(dicCode = "MB_BPMN_DEPLOY_CODE")
	private String status;

	@Column(name = "TITLE_")
	@Schema(description = "标题格式")
	private String title;

	@Column(name = "VISIBLE_")
	@Schema(description = "是否显示")
	private Boolean visible;

	@Lob
	@Column(name = "XML_",columnDefinition = "longtext")
	@Schema(description = "流程定义")
	private String xml;

	@Column(name = "LAST_DEF_ID_")
	@Schema(description = "当前定义ID")
	private String lastDefId;

	@Column(name = "LAST_DEF_VERSION_")
	@Schema(description = "当前定义版本")
	private Integer lastDefVersion;

}
