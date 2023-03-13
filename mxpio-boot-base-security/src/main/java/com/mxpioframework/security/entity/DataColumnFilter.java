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
@Table(name = "MB_DATA_COLUMN_FILTER")
@Schema(description="字段过滤对象")
@ToString
public class DataColumnFilter extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_", length = 64)
	@Generator
	@Schema(description = "ID")
	private String id;
	
	@Column(name = "DATA_RESOURCE_ID_")
	@Schema(description = "数据资源ID")
	private String dataResourceId;
	
	@Column(name = "COLUMN_")
	@Schema(description = "字段")
	private String column;
	
	@Column(name = "COLUMN_SCHEMA_")
	@Schema(description = "字段描述")
	private String columnSchema;
	
}
