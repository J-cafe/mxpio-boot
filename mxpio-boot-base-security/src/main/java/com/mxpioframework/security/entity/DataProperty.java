package com.mxpioframework.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_DB_DATAPROPERTY")
@Schema(description="实体属性")
public class DataProperty extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Schema(description = "ID")
	@Column(name = "ID_")
	private String id;
	
	@Schema(description = "类型编码")
	@Column(name = "TYPE_CODE_")
	private String typeCode;
	
	@Schema(description = "名称")
	@Column(name = "NAME_")
	private String name;

}
