package com.mxpioframework.module.datav.entity;

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
@Table(name = "MB_DATAV_COMPONENT")
@Schema(description="DataV组件")
public class DatavComponent extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Generator
	@Column(name = "ID_", updatable = false)
	@Schema(description = "ID")
	private String id;
	
	@Column(name = "NAME_")
	@Schema(description = "组件名称")
	private String name;
	
	@Column(name = "TYPE_")
	@Schema(description = "组件类型")
	private String type;
	
}
