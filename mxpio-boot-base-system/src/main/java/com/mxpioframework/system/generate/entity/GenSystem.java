package com.mxpioframework.system.generate.entity;

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
@Table(name = "MB_GENERATE_SYSTEM")
@Schema(description="系统")
public class GenSystem extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_")
	@Schema(description = "ID")
	private String id;
	
	@Column(name = "SYSTEM_CODE_", unique = true)
	@Schema(description = "系统编码")
	private String systemCode;
	
	@Column(name = "SYSTEM_NAME_")
	@Schema(description = "系统名称")
	private String systemName;
	
	@Column(name = "ROOT_PACKAGE_")
	@Schema(description = "主包名")
	private String rootPackage;
	
	@Column(name = "SYSTEM_VERSION_")
	@Schema(description = "版本")
	private String systemVersion;
	
	@Column(name = "SYSTEM_STATUS_")
	@Schema(description = "状态")
	private String systemStatus;
	
	@Column(name = "SYSTEM_DESC_")
	@Schema(description = "系统描述")
	private String systemDesc;
	
}
