package com.mxpioframework.system.generate.entity;

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
@Table(name = "MB_GENERATE_SYSTEM")
@ApiModel(value="系统")
public class GenSystem extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_")
	@ApiModelProperty(value = "ID")
	private String id;
	
	@Column(name = "SYSTEM_CODE_", unique = true)
	@ApiModelProperty(value = "系统编码")
	private String systemCode;
	
	@Column(name = "SYSTEM_NAME_")
	@ApiModelProperty(value = "系统名称")
	private String systemName;
	
	@Column(name = "ROOT_PACKAGE_")
	@ApiModelProperty(value = "主包名")
	private String rootPackage;
	
	@Column(name = "SYSTEM_VERSION_")
	@ApiModelProperty(value = "版本")
	private String systemVersion;
	
	@Column(name = "SYSTEM_STATUS_")
	@ApiModelProperty(value = "状态")
	private String systemStatus;
	
	@Column(name = "SYSTEM_DESC_")
	@ApiModelProperty(value = "系统描述")
	private String systemDesc;
	
}
