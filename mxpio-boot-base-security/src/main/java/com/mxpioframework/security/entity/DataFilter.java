package com.mxpioframework.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_DATA_FILTER")
@Schema(description="数据过滤对象")
@ToString
public class DataFilter extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID_", length = 64)
	@Generator
	@Schema(description = "ID")
	private String id;
	
	@Column(name = "DATA_SOURCE_ID_")
	@Schema(description = "数据资源ID")
	private String dataSourceId;
	
	@Column(name = "DATA_SCOPE_", length = 512)
	@Schema(description = "权限范围")
	private String dataScope;
	
	@Column(name = "PRE_PROCESS_")
	@Schema(description = "前置处理器")
	private String preProcess;
	
	@Column(name = "SERVICE_", length = 512)
	@Schema(description = "服务")
	private String service;
	
	@Column(name = "DESCRIPTION_", length = 255)
	@Schema(description = "描述")
	private String description;
	
}
